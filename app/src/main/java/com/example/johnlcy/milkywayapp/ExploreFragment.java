package com.example.johnlcy.milkywayapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginFragment;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView profilePic = null;
    private TextView tv = null;
    private Profile profile = null;
    private Toolbar toolbar = null;
    private DrawerLayout drawer = null;
    private FloatingActionButton fab = null;
    private ActionBarDrawerToggle toggle = null;
    private NavigationView navigationView = null;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private View navV;
    private List<CardList> cardList;
    private ProgressBar spiral;
    public String result = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        navV = navigationView.getHeaderView(0);
        tv = (TextView) navV.findViewById(R.id.userName);
        profilePic = (ImageView) navV.findViewById(R.id.profile_pic);
        recyclerView =
                (RecyclerView) view.findViewById(R.id.recycler_view);
        spiral = (ProgressBar) view.findViewById(R.id.spiral);
        cardList = new ArrayList<>();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        toggle = new ActionBarDrawerToggle(
                this.getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        Bundle bundle = getArguments();
        //Bundle info = new Bundle();
        if (bundle != null) {
            profile = (Profile) bundle.getParcelable(FacebookLoginFragment.PARCEL_KEY);
            Picasso.with(getActivity())
                    .load(profile.getProfilePictureUri(75, 75).toString())
                    .into(profilePic);
            tv.setText(profile.getName());
        } else {
            profile = Profile.getCurrentProfile();
        }
        //tv.setText(profile.getName());
        receiveDataFromServer();
        formatBusinessDataAsJSON(result);
    }

    private void receiveDataFromServer() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return getServerResponse();
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
            }
        }.execute();
    }

    private void formatBusinessDataAsJSON(String data) {
        Log.d("JWP", "Attempting to write JSON file.");
        try {
            JSONObject jsonO = new JSONObject(data);
            JSONArray array = jsonO.getJSONArray("businesses");
            for (int i = 0; i < array.length(); i++){
                //spiral.setVisibility(View.VISIBLE);
                JSONObject info = array.getJSONObject(i);
                CardList card = new CardList(
                        info.getString("name"),
                        info.getString("profileImage")
                );
                cardList.add(card);
                //spiral.setVisibility(View.GONE);
            }
            adapter = new ExploreRecyclerAdapter(cardList, getContext());
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            Log.d("formatting status", "Can't format JSON");
        } catch (Exception e) {
            Log.d("formatting Status", e.toString());
        }
    }

    private String getServerResponse() {
        try {
            Log.d("JWP", "Attempting to receive data to server.");
            URL url = new URL("http://localhost:8080/api/business");
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
           /* urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.addRequestProperty("Content-Type", "application/json");
            urlConn.addRequestProperty("Accept", "application/json");*/
            Log.d("receiveDataFromServer", "Prereqs Completed.");
            //reads and prints JSON
            String line;
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(urlConn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                result += line;
            }
            reader.close();
            Log.d("receiveDataFromServer", "Successfully received data from server.");
        } catch (UnsupportedEncodingException e) {
            Log.d("receiveDataFromServer", e.toString());
        } catch (MalformedURLException e) {
            Log.d("receiveDataFromServer", e.toString());
        } catch (IOException e) {
            Log.d("receiveDataFromServer", e.toString());
        } catch (Exception e) {
            Log.d("receiveDataFromServer", e.toString());
        }
        return "Unable to contact server.";
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.getActivity().onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.explore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        //Handle navigation view item clicks here.
        int id = item.getItemId();
         if (id == R.id.nav_camera) {
             // Handle the camera action
         } else if (id == R.id.nav_gallery) {

         } else if (id == R.id.nav_slideshow) {

         } else if (id == R.id.nav_manage) {

         } else if (id == R.id.nav_share) {

         } else if (id == R.id.nav_send) {

         } else if (id == R.id.sign_out) {
            logout();
         }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.mainActivity, new FacebookLoginFragment());
        fragmentTransaction.commit();
    }
}
