package com.jmulla.ukc;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


class FeedbackDialog {
  private EditText feedbackName;
  private EditText feedback;
  private EditText feedbackEmail;

  void showDialog(Activity activity){

    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    // Get the layout inflater
    LayoutInflater inflater = activity.getLayoutInflater();


    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    View mView = inflater.inflate(R.layout.fullscreen_feedback_dialog, null);
    feedbackName = mView.findViewById(R.id.et_feedback_name);
    feedback = mView.findViewById(R.id.et_feedback);
    feedbackEmail = mView.findViewById(R.id.et_feedback_email);

    AlertDialog alertDialog = builder.setView(mView)
        // Add action buttons
        .setPositiveButton("Send", null)
        .setNegativeButton("Cancel", null)
        .create();



    alertDialog.setOnShowListener(dialogInterface -> {

      Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
      button.setOnClickListener(view -> {
        if (feedback.getText().toString().isEmpty()){
          Toast.makeText(activity, "Feedback can't be empty", Toast.LENGTH_SHORT).show();
          return;
        }
        ClientThread sender = new ClientThread(feedback.getText().toString(), activity);
        String feedbackName = this.feedbackName.getText().toString();
        if(!feedbackName.isEmpty()){
          sender.setFeedbackName(feedbackName);
        }

        String feedbackEmail = this.feedbackEmail.getText().toString();
        if(!feedbackEmail.isEmpty()){
          sender.setFeedbackEmail(feedbackEmail);
        }


        Thread feedbackThread = new Thread(sender);
        alertDialog.dismiss();
        feedbackThread.start();


      });
    });
    alertDialog.show();
  }

  class ClientThread implements Runnable {

    private String feedbackName;
    private String feedback;
    private String feedbackEmail;
    private Activity activity;


    public ClientThread(String feedback, Activity activity) {
      this.feedback = feedback;
      this.feedbackName = "N/A";
      this.feedbackEmail = "N/A";
      this.activity = activity;
    }


    public void run() {
      try {
        JSONObject jsonFeedback = new JSONObject();
        try {
          jsonFeedback.put("feedback_name", this.feedbackName);
          jsonFeedback.put("feedback_body", this.feedback);
          jsonFeedback.put("feedback_email", this.feedbackEmail);
        } catch (JSONException e) {
          e.printStackTrace();
          return;
        }


        Socket socket = new Socket("139.59.164.236", 6666);
        try (OutputStreamWriter out = new OutputStreamWriter(
            socket.getOutputStream(), StandardCharsets.UTF_8)) {
          out.write(jsonFeedback.toString());
        }

        socket.close();
        activity.runOnUiThread(new Runnable() {
          public void run() {
            Toast.makeText(activity.getBaseContext(), "Thank you for the feedback", Toast.LENGTH_SHORT).show();
          }
        });

      } catch (IOException e) {
        activity.runOnUiThread(new Runnable() {
          public void run() {
            Toast.makeText(activity.getBaseContext(), "Couldn't send feedback. Check your internet connection", Toast.LENGTH_SHORT).show();
          }
        });
      }
    }

    void setFeedbackName(String feedbackName) {
      this.feedbackName = feedbackName;
    }

    void setFeedbackEmail(String feedbackEmail) {
      this.feedbackEmail = feedbackEmail;
    }
  }

}