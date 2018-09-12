package com.example.johnlcy.milkywayapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.FacebookSdk;


/**
 * A blank activity for putting different fragments/tabs on
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //FacebookSdk.sdkInitialize(getApplicationContext());

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                /* Create an Intent that will start the Menu-Activity. */
//                Intent mainIntent = new Intent(LoginActivity.this, Menu.class);
//                startActivity(mainIntent);
//                finish();
//            }
//        }, 1500);
    }
}