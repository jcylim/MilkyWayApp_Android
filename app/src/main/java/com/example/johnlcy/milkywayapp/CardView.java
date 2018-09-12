package com.example.johnlcy.milkywayapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnlcy on 5/28/2017.
 */

public class CardView extends Fragment {

    private ImageView businessImage = null;
    private TextView businessName = null;
    private TextView businessDesc = null;
    private List<CardList> cardList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cardview, container, false);
        businessImage = (ImageView) view.findViewById(R.id.businessImage);
        businessName = (TextView) view.findViewById(R.id.businessName);
        businessDesc = (TextView) view.findViewById(R.id.businessInfo);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cardList = new ArrayList<>();
        receiveDataFromServer();
    }

    private void receiveDataFromServer() {

    }

}
