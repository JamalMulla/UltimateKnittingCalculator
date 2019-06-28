package com.jmulla.ukc;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;



class FeedbackDialog {
  private EditText feedback;
  private boolean success;

  void showDialog(Activity activity){

    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    // Get the layout inflater
    LayoutInflater inflater = activity.getLayoutInflater();


    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    View mView = inflater.inflate(R.layout.fullscreen_feedback_dialog, null);
    feedback = mView.findViewById(R.id.et_feedback);
    builder.setView(mView)
        // Add action buttons
        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int id) {
            Thread feedbackThread = new Thread(new ClientThread(feedback.getText().toString()));
            feedbackThread.start();
            try {
              feedbackThread.join();
              if (success){
                Toast.makeText(activity, "Thank you for the feedback", Toast.LENGTH_SHORT).show();
              }else {
                Toast.makeText(activity, "Couldn't send feedback. Check your internet connection", Toast.LENGTH_SHORT).show();

              }
            } catch (InterruptedException e) {
              e.printStackTrace();
            }

          }
        })
        .setNegativeButton("Cancel", (dialog, id) -> {});


    AlertDialog alertDialog = builder.create();
    alertDialog.show();

  }

  class ClientThread implements Runnable {

    private String feedback;

    ClientThread(String feedback) {

      this.feedback = feedback;
    }

    public void run() {
      try {
        Socket socket = new Socket("139.59.164.236", 6666);
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        //Feedback f = new Feedback("No name", "Test", "No email");
        String feedbackString = feedback;
        System.out.println(feedbackString);
        os.writeUTF(feedbackString);
        os.flush(); // Send off the data
        socket.close();
        success = true;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  private class Feedback implements Serializable {
    private String name;
    private String feedback;
    private String email;


    Feedback(String name, String feedback, String email){
      this.name = name;
      this.feedback = feedback;
      this.email = email;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getFeedback() {
      return feedback;
    }

    public void setFeedback(String feedback) {
      this.feedback = feedback;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }
  }
}