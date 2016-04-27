package com.mobile4623.easy.adogption;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PetSendMessage extends AppCompatActivity {

    // UI elements
    Button btnCancel, btnSend;
    EditText messageContent;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SENDER = "account";
    private static final String TAG_RECEIVER = "receiver";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_LOGIN = "login";
    private static final String TAG_UID = "uid";

    String account; // account id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_send_message);

        // get account info from shared preferences
        SharedPreferences preferences = getSharedPreferences(TAG_LOGIN, MODE_PRIVATE);
        account = preferences.getString("login", "defaultStringIfNothingFound");

        btnCancel = (Button) findViewById(R.id.btnContactCancel);
        btnSend = (Button) findViewById(R.id.btnContactSend);
        messageContent = (EditText) findViewById(R.id.contact_message);


        // Open contact owner activity
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String msg = messageContent.getText().toString();

                // Send Message
                new onSendMessage().execute(account, );
            }
        });

        // Back button click event
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Return to Pet Search Details
                finish();

            }
        });


    }

    /**
     * Background Async Task to save message
     * */
    class onSendMessage extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PetSendMessage.this);
            pDialog.setMessage("Saving pet ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {


            String sender = args[0];
            String receiver = args[1];
            String message = args[2];


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_SENDER,sender));
            params.add(new BasicNameValuePair(TAG_RECEIVER, receiver));
            params.add(new BasicNameValuePair(TAG_MESSAGE, message));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(
                    WebConstants.URL_CREATE_PET, "POST", params);


            //debug test
            try {

                Log.i("JSON Parser", json.getString(TAG_SUCCESS));
            }catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once message updated
            pDialog.dismiss();
        }
    }

}