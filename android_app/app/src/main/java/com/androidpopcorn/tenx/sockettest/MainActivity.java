package com.androidpopcorn.tenx.sockettest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";



    private EditText etMessage, etName;
    private Button btnButton;
    private TextView tvOut;


        //TODO update your server url after hosting with node localhost:port (i use ngrok - ubuntu) and change this variable
        private final String serverurl = "";

    private com.github.nkzawa.socketio.client.Socket mSocket;


    // initialize the socket with server url
    {
        try {
            Log.d(TAG, "instance initializer: initializing socket");

            mSocket = IO.socket(serverurl);

        }catch (URISyntaxException e){

            e.printStackTrace();

            Log.d(TAG, "instance initializer: "+e.getMessage());

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        etMessage = findViewById(R.id.et_message);
        etName = findViewById(R.id.et_name);
        btnButton = findViewById(R.id.btn_login);
        tvOut = findViewById(R.id.tv_out);


//subscribe to event 'chat message' to recieve the emitted data and attatch a listener to update the view
        mSocket.on("chat message", onNewMessage);

        btnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });


// connect to socket
        mSocket.connect();

    }


    private void attemptSend() {

        //get text from the two edit texts and emit it as a json object with an event 'chat message'
        //anyone subscribed to 'chat message' will recieve this jsonobject


        String message = etMessage.getText().toString().trim();
        String name = etName.getText().toString();
        if (TextUtils.isEmpty(message) || TextUtils.isEmpty(name)) {
            return;
        }

        etMessage.setText("");
        JSONObject jsonObject = new JSONObject();
  try {
      jsonObject.put("message", message);
      jsonObject.put("username", name);
      mSocket.emit("chat message", jsonObject);
  }catch (JSONException e){
      e.printStackTrace();
  }

    }


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG, "call: OnNewMessage called");

//            run this on the ui thread to update the view
            
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

             try {
                 String message = data.getString("message");
                 String username = data.getString("username");
                 addMessage(username, message);
             }catch (JSONException e){
                 e.printStackTrace();
             }
                }
            });
        }
    };

    //disconnect from the socket when app is closed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("chat message", onNewMessage);
    }


    //function to update the text view

    private void addMessage(String username, String message) {
        String out = tvOut.getText().toString() + "\n" + username +" : " + message;
        tvOut.setText(out);
    }


}
