package com.example.johnlcy.milkywayapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.nexmo.sdk.NexmoClient;
import com.nexmo.sdk.core.client.ClientBuilderException;
import com.nexmo.sdk.verify.client.VerifyClient;
import com.nexmo.sdk.verify.event.UserObject;
import com.nexmo.sdk.verify.event.VerifyClientListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.GraphRequest.TAG;

/**
 * Created by johnlcy on 9/21/2016.
 */
public class VerificationCodeFragment extends Fragment {

    private EditText numBox1 = null;
    private EditText numBox2 = null;
    private EditText numBox3 = null;
    private TextView dash1 = null;
    private EditText numBox4 = null;
    private EditText numBox5 = null;
    private EditText numBox6 = null;
    private TextView dash2 = null;
    private EditText numBox7 = null;
    private EditText numBox8 = null;
    private EditText numBox9 = null;
    private EditText numBox10 = null;
    private String numB1;
    private String numB2;
    private String numB3;
    private String numB4;
    private String numB5;
    private String numB6;
    private String numB7;
    private String numB8;
    private String numB9;
    private String numB10;
    private String requestID;
    private Bundle data;

    //public static final String NexmoAppId = "d7afdc8a-3885-470c-9f57-43768a2f3148";
    //public static final String NexmoSharedSecretKey = "04bea64c0366481";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verificationcode, container, false);
        numBox1 = (EditText) view.findViewById(R.id.pNum1);
        numBox2 = (EditText) view.findViewById(R.id.pNum2);
        numBox3 = (EditText) view.findViewById(R.id.pNum3);
        dash1 = (TextView) view.findViewById(R.id.dash1);
        numBox4 = (EditText) view.findViewById(R.id.pNum4);
        numBox5 = (EditText) view.findViewById(R.id.pNum5);
        numBox6 = (EditText) view.findViewById(R.id.pNum6);
        dash2 = (TextView) view.findViewById(R.id.dash2);
        numBox7 = (EditText) view.findViewById(R.id.pNum7);
        numBox8 = (EditText) view.findViewById(R.id.pNum8);
        numBox9 = (EditText) view.findViewById(R.id.pNum9);
        numBox10 = (EditText) view.findViewById(R.id.pNum10);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Context context = getApplicationContext();
        numBox1.addTextChangedListener(tw1);
        numBox2.addTextChangedListener(tw2);
        numBox3.addTextChangedListener(tw3);
        numBox4.addTextChangedListener(tw4);
        numBox5.addTextChangedListener(tw5);
        numBox6.addTextChangedListener(tw6);
        numBox7.addTextChangedListener(tw7);
        numBox8.addTextChangedListener(tw8);
        numBox9.addTextChangedListener(tw9);
        numBox10.addTextChangedListener(tw10);
        /*try {
            NexmoClient nexmoClient = new NexmoClient.NexmoClientBuilder()
                    .context(context)
                    .applicationId(NexmoAppId) //your App key
                    .sharedSecretKey(NexmoSharedSecretKey) //your App secret
                    .build();
            VerifyClient verifyClient = new VerifyClient(nexmoClient);
            //verifyListener(verifyClient);
        } catch (ClientBuilderException e) {
            e.printStackTrace();
        }*/
    }

    TextWatcher tw1 = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (numBox1.getText().toString().length() == 1)
            {
                numBox2.requestFocus();
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

    TextWatcher tw2 = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (numBox2.getText().toString().length() == 1)
            {
                numBox3.requestFocus();
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

    TextWatcher tw3 = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (numBox3.getText().toString().length() == 1)
            {
                numBox4.requestFocus();
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

    TextWatcher tw4 = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (numBox4.getText().toString().length() == 1)
            {
                numBox5.requestFocus();
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

    TextWatcher tw5 = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (numBox5.getText().toString().length() == 1)
            {
                numBox6.requestFocus();
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

    TextWatcher tw6 = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (numBox6.getText().toString().length() == 1)
            {
                numBox7.requestFocus();
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

    TextWatcher tw7 = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (numBox7.getText().toString().length() == 1)
            {
                numBox8.requestFocus();
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

    TextWatcher tw8 = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (numBox8.getText().toString().length() == 1)
            {
                numBox9.requestFocus();
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

    TextWatcher tw9 = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (numBox9.getText().toString().length() == 1)
            {
                numBox10.requestFocus();
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

    TextWatcher tw10 = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (numBox10.getText().toString().length() == 1)
            {
                nextFragment();
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

    private void initialization() {
        numB1 = numBox1.getText().toString().trim();
        numB2 = numBox2.getText().toString().trim();
        numB3 = numBox3.getText().toString().trim();
        numB4 = numBox4.getText().toString().trim();
        numB5 = numBox5.getText().toString().trim();
        numB6 = numBox6.getText().toString().trim();
        numB7 = numBox7.getText().toString().trim();
        numB8 = numBox8.getText().toString().trim();
        numB9 = numBox9.getText().toString().trim();
        numB10 = numBox10.getText().toString().trim();
    }

    private boolean emptyTest() {
        if (numB1.isEmpty() || numB2.isEmpty() || numB3.isEmpty() || numB4.isEmpty() || numB5.isEmpty() || numB6.isEmpty() ||
                numB7.isEmpty() || numB8.isEmpty() || numB9.isEmpty() || numB10.isEmpty())
            return true;
        else
            return false;
    }

    private String formatUserDataAsJSON() {
        String phoneNumber = "1" + numB1 + numB2 + numB3 + numB4 + numB5 + numB6 + numB7 + numB8 + numB9 + numB10;
        JSONObject data = new JSONObject();
        Log.d("JWP", "Attempting to write JSON file.");
        try {
            data.put("number", phoneNumber);
            Log.d("JWP", "JSON file written.");
            return data.toString();
        } catch (JSONException e) {
            Log.d("JWP", "Can't format JSON");
            return null;
        } catch (Exception e) {
            Log.d("JWP", e.toString());
            return null;
        }
    }

    private void sendDataToServer(final String userInfo) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return getServerResponse(userInfo);
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
            }
        }.execute();
    }

    private String getServerResponse(final String data) {
        try {
            Log.d("JWP", "Attempting to send data to server.");
            URL url = new URL("http://milkywayapp.me:8080/api/auth/verify/phone");
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestProperty("Accept", "application/json");
            urlConn.setRequestMethod("POST");
            Log.d("sendDataToServer", "Prereqs Completed.");
            //writes JSON
            OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
            wr.write(data);
            wr.flush();
            //reads and prints JSON
            String line;
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(urlConn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                requestID = line;
                System.out.println(line);
            }
            wr.close();
            reader.close();
            Log.d("sendDataToServer", "Successfully sent data to server.");
        } catch (UnsupportedEncodingException e) {
            Log.d("sendDataToServer", e.toString());
        } catch (MalformedURLException e) {
            Log.d("sendDataToServer", e.toString());
        } catch (IOException e) {
            Log.d("sendDataToServer", e.toString());
        } catch (Exception e) {
            Log.d("sendDataToServer", e.toString());
        }
        return "Unable to contact server.";
    }

    private void nextFragment() {
        data = new Bundle();
        initialization();
        if (emptyTest())
            Toast.makeText(getActivity().getApplicationContext(), "Invalid Verfication Code Entry", Toast.LENGTH_SHORT).show();
        else {
            sendDataToServer(formatUserDataAsJSON());
            data.putString("requestID:", requestID);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            EnterVerificationCodeFragment enterVerCode = new EnterVerificationCodeFragment();
            enterVerCode.setArguments(data);
            fragmentTransaction.replace(R.id.mainActivity, new EnterVerificationCodeFragment());
            fragmentTransaction.commit();
            Log.d("status:", "SENT DATA TO SERVER.");
        }
    }
    /*private void verifyListener(VerifyClient verifyClient){
        verifyClient.addVerifyListener(new VerifyClientListener() {
            @Override
            public void onVerifyInProgress(final VerifyClient verifyClient, UserObject user) {
                verifyClient.getVerifiedUser("GB", user.getPhoneNumber());
                verifyClient.checkPinCode("1234");
                Log.d(TAG, "onVerifyInProgress for number: " + user.getPhoneNumber());
            }

            @Override
            public void onUserVerified(final VerifyClient verifyClient, UserObject user) {
                Log.d(TAG, "onUserVerified for number: " + user.getPhoneNumber());
            }

            @Override
            public void onError(final VerifyClient verifyClient, final com.nexmo.sdk.verify.event.VerifyError errorCode, UserObject user) {
                Log.d(TAG, "onError: " + errorCode + " for number: " + user.getPhoneNumber());
            }

            @Override
            public void onException(final IOException exception) {
            }
        });
    }*/

}
