package com.bignerdranch.android.fyp.network;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by David on 11/2/2016.
 */
public class BackgroundWorker extends AsyncTask<String, Void, String>{

    ProgressBar mProgressBar;
    Context mContext;
    AlertDialog mAlertDialog;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public BackgroundWorker (AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        //http://10.0.2.2:1234/php/android/  emulator
        //http://192.168.1.146:1234/php/android/   real device
        String url_to_connect = "http://10.0.2.2:1234/php/android/";
        String login_url = "login.php";
        String password_reset_url = "passwordReset.php";
        String insert_question_url = "insertQuestion.php";
        String update_question_url = "updateQuestion.php";
        String count_question_comment_url = "countCommentRow.php";
        String insert_commemt_url = "insertComment.php";
        String post_data = "";

         try {

             if (type.equals("login")) {
                 url_to_connect = url_to_connect + login_url;
                 post_data = URLEncoder.encode("student_name", "UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8")+"&"
                         +URLEncoder.encode("student_password","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8");
             } else if (type.equals("passwordReset")){
                 url_to_connect = url_to_connect + password_reset_url;
                 post_data = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8");
             } else if (type.equals("insertQuestion")) {
                 url_to_connect = url_to_connect + insert_question_url;
                 post_data = URLEncoder.encode("question_author", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                         URLEncoder.encode("question_title", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                         URLEncoder.encode("question_content", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8") + "&" +
                         URLEncoder.encode("question_subject", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8") + "&" +
                         URLEncoder.encode("question_date", "UTF-8") + "=" + URLEncoder.encode(params[5], "UTF-8") ;
             } else if (type.equals("updateQuestion")) {
                 url_to_connect = url_to_connect + update_question_url;
                 // id is for update purpose
                 post_data = URLEncoder.encode("questionId", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                         URLEncoder.encode("question_title", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                         URLEncoder.encode("question_content", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8") + "&" +
                         URLEncoder.encode("question_subject", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8") + "&" +
                         URLEncoder.encode("question_date", "UTF-8") + "=" + URLEncoder.encode(params[5], "UTF-8") ;
             } else if (type.equals("countPostCommentRow")) {
                 url_to_connect = url_to_connect + count_question_comment_url;
                 post_data = URLEncoder.encode("postId", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
             } else if(type.equals("insertComment")) {
                 url_to_connect = url_to_connect + insert_commemt_url;
                 post_data = URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                         URLEncoder.encode("commentNum", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                         URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8") + "&" +
                         URLEncoder.encode("userId", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8") + "&" +
                         URLEncoder.encode("postId", "UTF-8") + "=" + URLEncoder.encode(params[5], "UTF-8") ;
             }

              URL url = new URL(url_to_connect);
              HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
              httpURLConnection.setRequestMethod("POST");
              httpURLConnection.setDoOutput(true);
              httpURLConnection.setDoInput(true);
              OutputStream outputStream = httpURLConnection.getOutputStream();
              BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

              bufferedWriter.write(post_data);
              bufferedWriter.flush();
              bufferedWriter.close();
              outputStream.close();
              InputStream inputStream = httpURLConnection.getInputStream();
              BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
              String result = "";
              String line = "";
              while((line = bufferedReader.readLine())!= null) {
                  result += line;
              }
              bufferedReader.close();
              inputStream.close();
              httpURLConnection.disconnect();
              return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    @Override
    protected void onPreExecute() {
        //mAlertDialog = new AlertDialog.Builder(mContext).create();
        //mAlertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
   /*
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    } */

}
