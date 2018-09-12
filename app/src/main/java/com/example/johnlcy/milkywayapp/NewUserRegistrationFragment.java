package com.example.johnlcy.milkywayapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by johnlcy on 1/6/2017.
 */

public class NewUserRegistrationFragment extends Fragment{

    private EditText firstName = null;
    private EditText lastName = null;
    private EditText userName = null;
    private EditText email = null;
    private EditText password = null;
    private EditText confirmPassword = null;
    private Button createButton = null;
    private String firstN;
    private String lastN;
    private String userN;
    private String e;
    private String pW;
    private String confirmPW;
    private ImageButton backArrow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newuserregistration, container, false);
        firstName = (EditText) view.findViewById(R.id.firstName);
        lastName = (EditText) view.findViewById(R.id.lastName);
        userName = (EditText) view.findViewById(R.id.userName);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        createButton = (Button) view.findViewById(R.id.creat_account_button);
        backArrow = (ImageButton) view.findViewById(R.id.backArrow);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*if (!emptyTest(firstName, lastName, userName, email, password, confirmPassword)) {
            createButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isValidEmail(email) && isValidPassword(password, confirmPassword))
                            nextFragment();
                        else
                            Toast.makeText(getActivity().getApplicationContext(), "Invalid Entry of Email or Password", Toast.LENGTH_SHORT).show();
                    }
                });
        } else {
            createButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity().getApplicationContext(), "Invalid Entry of Information", Toast.LENGTH_SHORT).show();
                    }
            });
        }*/
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    nextFragment();
                }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backFunction();
            }
        });
    }

    private void initialization() {
        firstN = firstName.getText().toString().trim();
        lastN = lastName.getText().toString().trim();
        userN = userName.getText().toString().trim();
        e = email.getText().toString().trim();
        pW = password.getText().toString().trim();
        confirmPW = confirmPassword.getText().toString().trim();
    }
    /*private boolean isValidEmail(EditText email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email.getText().toString());
        return matcher.matches();
    }

    private boolean isValidPassword(EditText password, EditText confirmPassword) {
        String stringPW = password.getText().toString();
        String stringConfirmPW = confirmPassword.getText().toString();
        if (stringPW.equals(stringConfirmPW))
            return true;
        else
            return false;
    }*/

    private boolean emptyTest() {
        boolean empty = false;
        if (firstN.isEmpty()) {
            firstName.setError("Please Enter Your First Name");
            empty = true;
        }
        if (lastN.isEmpty()) {
            lastName.setError("Please Enter Your Last Name");
            empty = true;
        }
        if (userN.isEmpty()) {
            userName.setError("Please Enter Your User Name");
            empty = true;
        }
        if (e.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            email.setError("Please Enter Your Email or Enter a Proper Email");
            empty = true;
        }
        if (pW.isEmpty()) {
            password.setError("Please Enter Your Password");
            empty = true;
        }
        if (confirmPW.isEmpty() || !confirmPW.equals(pW)) {
            confirmPassword.setError("Please Confirm Your Password");
            empty = true;
        }
        return empty;
    }

    private String formatUserDataAsJSON(EditText firstName, EditText lastName, EditText userName, EditText email, EditText password) {
        JSONObject data = new JSONObject();
        Log.d("JWP", "Attempting to write JSON file.");
        try {
            data.put("firstName", firstName.getText().toString());
            data.put("lastName", lastName.getText().toString());
            data.put("username", userName.getText().toString());
            data.put("email", email.getText().toString());
            data.put("password", password.getText().toString());
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
            URL url = new URL("http://milkywayapp.me:8080/api/auth/signup");
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
                System.out.println(line);
            }
            if (urlConn.getResponseCode() == 200) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                fragmentTransaction.replace(R.id.mainActivity, new VerificationCodeFragment());
                fragmentTransaction.commit();
            }
            else if (urlConn.getResponseCode() == 202) {
                Toast.makeText(getContext(), "User Already Exists", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
            wr.close();
            reader.close();
            Log.d("sendDataToServer", "Successfully sent data to server.");
        } catch (UnsupportedEncodingException e) {
            Log.d("sendDataToServer", e.toString(), e);
        } catch (MalformedURLException e) {
            Log.d("sendDataToServer", e.toString(), e);
        } catch (IOException e) {
            Log.d("sendDataToServer", e.toString(), e);
        } catch (Exception e) {
            Log.d("sendDataToServer", e.toString(), e);
        }
        return "Unable to contact server.";
    }

    private void backFunction() {
        getFragmentManager().popBackStack();
    }

    private void nextFragment() {
        initialization();
        if (emptyTest())
            Log.d("emptyTest", "Info Incomplete");
        else {
            sendDataToServer(formatUserDataAsJSON(firstName, lastName, userName, email, password));
        }
    }
}
