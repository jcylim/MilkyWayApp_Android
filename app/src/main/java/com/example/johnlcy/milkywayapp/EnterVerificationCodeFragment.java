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
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.nexmo.sdk.NexmoClient;
import com.nexmo.sdk.core.client.ClientBuilderException;
import com.nexmo.sdk.verify.client.VerifyClient;

import org.json.JSONArray;
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

/**
 * Created by johnlcy on 11/25/2016.
 */

public class EnterVerificationCodeFragment extends Fragment {

    private EditText pinCode1 = null;
    private EditText pinCode2 = null;
    private EditText pinCode3 = null;
    private EditText pinCode4 = null;
    private Bundle data;
    private String requestID;
    private String id;
    private String pin1;
    private String pin2;
    private String pin3;
    private String pin4;
    private String pinCode;
    private String successString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enterverificationcode, container, false);
        pinCode1 = (EditText) view.findViewById(R.id.editText1);
        pinCode2 = (EditText) view.findViewById(R.id.editText2);
        pinCode3 = (EditText) view.findViewById(R.id.editText3);
        pinCode4 = (EditText) view.findViewById(R.id.editText4);
        data = getArguments();
        requestID = data.getString("requestID:");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Context context = getApplicationContext();
        pinCode2.addTextChangedListener(tw1);
        pinCode1.addTextChangedListener(tw2);
        pinCode3.addTextChangedListener(tw3);
        pinCode4.addTextChangedListener(tw4);
    }

    TextWatcher tw1 = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (pinCode2.getText().toString().length() == 1)
            {
                pinCode1.requestFocus();
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
            if (pinCode1.getText().toString().length() == 1)
            {
                pinCode3.requestFocus();
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
            if (pinCode3.getText().toString().length() == 1)
            {
                pinCode4.requestFocus();
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
            if (pinCode4.getText().toString().length() == 1)
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
        pin1 = pinCode1.getText().toString().trim();
        pin2 = pinCode2.getText().toString().trim();
        pin3 = pinCode3.getText().toString().trim();
        pin4 = pinCode4.getText().toString().trim();
    }

    private void sendDataToServer(final String pinInfo) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return getServerResponse(pinInfo);
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
            }
        }.execute();
    }

    private String getRequestID() {
        //get requestID from previous fragment
        char x;
        for (int i = 0; i < requestID.length(); i++) {
            x = requestID.charAt(i);
            if (x == ':')
                id = requestID.substring(i+1, requestID.length() - 1);
        }
        return id;
    }

    private String formatUserDataAsJSON() {
        pinCode = pin2 + pin1 + pin3 + pin4;
        JSONObject data = new JSONObject();
        Log.d("JWP", "Attempting to write JSON file.");
        try {
            data.put("pin", pinCode);
            data.put("request_id", getRequestID());
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

    private boolean successOrNah() {
        char x;
        String s = null;
        for (int i = 0; i < successString.length(); i++) {
            x = successString.charAt(i);
            if (x == ':')
                s = successString.substring(i+1, successString.length() - 1);
        }
        if (s == "true")
            return false;
        return true;
    }

    private String getServerResponse(final String data) {
        try {
            Log.d("getServerResponse", "Attempting to receive data from server.");
            URL url = new URL("http://milkywayapp.me:8080/api/auth/verify/pin");
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoOutput(true);
            //urlConn.setDoInput(true);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestProperty("Accept", "application/json");
            urlConn.setRequestMethod("GET");
            Log.d("getServerResponse", "Prereqs Completed.");
            //writes JSON
            OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
            wr.write(data);
            wr.flush();
            //reads and prints JSON
            String line;
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(urlConn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                successString = line;
                System.out.println(line);
            }
            reader.close();
            wr.close();
            Log.d("getServerResponse", "Successfully sent data to server.");
        } catch (UnsupportedEncodingException e) {
            Log.d("getServerResponse", e.toString());
        } catch (MalformedURLException e) {
            Log.d("getServerResponse", e.toString());
        } catch (IOException e) {
            Log.d("getServerResponse", e.toString());
        } catch (Exception e) {
            Log.d("getServerResponse", e.toString());
        }
        return "Unable to Communicate with Server.";
    }

    private void nextFragment() {
        initialization();
        sendDataToServer(formatUserDataAsJSON());
        if (successOrNah())
            Toast.makeText(getActivity().getApplicationContext(), "Invalid PIN Code", Toast.LENGTH_SHORT).show();
        else {
            /*Bundle bundle = new Bundle();
            bundle.putString("user", "new");
            ExploreFragment ef = new ExploreFragment();
            ef.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.replace(R.id.mainActivity, new ExploreFragment());
            fragmentTransaction.commit();*/
            Log.d("status:", "SENT DATA TO SERVER.");
        }
    }


}
