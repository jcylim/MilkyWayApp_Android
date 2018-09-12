package com.example.johnlcy.milkywayapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.MalformedJsonException;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.CallbackManager;
import com.facebook.ProfileTracker;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * Created by johnlcy on 8/28/2016.
 * This fragment enables users to use Facebook API
 */
public class FacebookLoginFragment extends Fragment /*implements LoaderCallbacks<Cursor>*/ {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mPasswordView;

    public static final String PARCEL_KEY = "parcel_key";
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton fbloginButton;
    private Button loginButton;
    private Button signUpButton;
    private String fName;
    private String lName;
    private String email;
    private String id;
    private String userName;
    private String pW;
    private int httpResponse;

    // Callback registration
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    // Get facebook data from login
                    //object = response.getJSONObject();
                    Log.v("Graph Response: ", response.toString());
                    Log.v("Object Response: ", object.toString());
                    response.getError();
                    try {
                        //user_id = json.getString("id");
                        fName = object.getString("first_name");
                        lName = object.getString("last_name");
                        email = object.getString("email");
                        id = object.getString("id");
                        //userName = object.getString("username");
                        //pW = json.getString("password");
                        sendDataToServer(object);
                    } catch (JSONException e) {
                        Log.d("JWP", "JSON Not Formatted.");
                    }
                }
            });
            Bundle params = new Bundle();
            params.putString("fields", "first_name, last_name, email, id");
            request.setParameters(params);
            request.executeAsync();

            exploreFragment(profile);
            //getServerResponse();
            Toast.makeText(getActivity().getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancel() {
            // App code
        }
        @Override
        public void onError(FacebookException e) {
            // App code
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                exploreFragment(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    // FACEBOOK LOGIN
    private void exploreFragment(Profile profile) {
        if (profile != null) {
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(PARCEL_KEY, profile);
            ExploreFragment ef = new ExploreFragment();
            ef.setArguments(mBundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.replace(R.id.mainActivity, new ExploreFragment());
            fragmentTransaction.commit();
        }
    }

    private void registerFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.mainActivity, new NewUserRegistrationFragment());
        fragmentTransaction.commit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_facebooklogin, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*View decorView = getActivity().getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.hide();*/
        fbloginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton = (Button) view.findViewById(R.id.loginButton);
        signUpButton = (Button) view.findViewById(R.id.signUpButton);
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.emailOruName);
        mPasswordView = (AutoCompleteTextView) view.findViewById(R.id.password);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               registerFragment();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextFragment();
            }
        });
        // If using in a fragment
        fbloginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        fbloginButton.setFragment(this);
        fbloginButton.registerCallback(callbackManager, callback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLoggedIn()) {
            loginButton.setVisibility(View.INVISIBLE);
            Profile profile = Profile.getCurrentProfile();
            exploreFragment(profile);
        }
    }private String formatFacebookDataAsJSON(JSONObject json) {
        final JSONObject data = new JSONObject();
        //final JSONObject pastUserInfo = new JSONObject();
        Log.d("JWP", "Attempting to write JSON file.");
        Profile profile = Profile.getCurrentProfile();
        try {
            //data.put("", );
            data.put("firstName", json.getString("first_name"));
            data.put("lastName", json.getString("last_name"));
            data.put("username", json.getString("id"));
            data.put("password", "MilkyWay08/05");
            data.put("email", json.getString("email"));
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

    private String formatUserDataAsJSON() {
        final JSONObject uInfo = new JSONObject();
        Log.d("JWP", "Attempting to write JSON file.");
        Profile profile = Profile.getCurrentProfile();
        try {
            //data.put("", );
            uInfo.put("username", userName);
            uInfo.put("password", pW);
            Log.d("JWP", "JSON file written.");
            return uInfo.toString();
        } catch (JSONException e) {
            Log.d("JWP", "Can't format JSON");
            return null;
        } catch (Exception e) {
            Log.d("JWP", e.toString());
            return null;
        }
    }

    private void sendDataToServer(JSONObject json) {
        final String data = formatFacebookDataAsJSON(json);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return getServerResponse(data);
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
            }
        }.execute();
    }

    private void sendUserInfoToServer(final String userInfo) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return getUserServerResponse(userInfo);
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
            URL url = new URL("http:milkywayapp.me:8080/api/auth/signin");
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

    private String getUserServerResponse(String userInfo) {
        try {
            Log.d("JWP", "Attempting to send data to server.");
            URL url = new URL("http://milkywayapp.me:8080/api/auth/signin");
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestProperty("Accept", "application/json");
            urlConn.setRequestMethod("POST");
            Log.d("sendDataToServer", "Prereqs Completed.");
            //writes JSON
            OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
            wr.write(userInfo);
            wr.flush();
            //reads and prints JSON
            String line;
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(urlConn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            wr.close();
            reader.close();
            /*if (urlConn.getResponseCode() == 200) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                fragmentTransaction.replace(R.id.mainActivity, new ExploreFragment());
                fragmentTransaction.commit();
                Toast.makeText(getContext(), "Logged into Explore Fragment", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();*/
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

    private void initialization() {
        userName = mEmailView.getText().toString().trim();
        pW = mPasswordView.getText().toString().trim();
    }

    private boolean validTest() {
        boolean empty = false;
        if (userName.isEmpty()) {
            mEmailView.setError("Please Enter Your Username");
            empty = true;
        }
        if (pW.isEmpty()) {
            mPasswordView.setError("Please Enter Your Password");
            empty = true;
        }
        return empty;
    }

    private void nextFragment() {
        initialization();
        if (validTest())
            Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
        else {
            sendUserInfoToServer(formatUserDataAsJSON());
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.replace(R.id.mainActivity, new ExploreFragment());
            fragmentTransaction.commit();
        }
    }

    /*
    // GOOGLE LOGIN
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            // Use ContactsContract.Profile (API 14+)
            getActivity().getLoaderManager().initLoader(0, null, this);
        } else if (Build.VERSION.SDK_INT >= 8) {
            // Use AccountManager (API 8+)
            new SetupEmailAutoCompleteTask().execute(null, null);
        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (getActivity().checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
/*    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
/*    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Use an AsyncTask to fetch the user's email addresses on a background thread, and update
     * the email text field with results on the main UI thread.
     */
/*    class SetupEmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            ArrayList<String> emailAddressCollection = new ArrayList<>();

            // Get all emails from the user's contacts and copy them to a list.
            ContentResolver cr = getActivity().getContentResolver();
            Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    null, null, null);
            while (emailCur.moveToNext()) {
                String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract
                        .CommonDataKinds.Email.DATA));
                emailAddressCollection.add(email);
            }
            emailCur.close();

            return emailAddressCollection;
        }

        @Override
        protected void onPostExecute(List<String> emailAddressCollection) {
            addEmailsToAutoComplete(emailAddressCollection);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
/*    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                getActivity().finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }*/
}

