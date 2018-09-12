package com.example.johnlcy.milkywayapp;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by johnlcy on 7/4/2017.
 */

public class ExploreRecyclerAdapter extends RecyclerView.Adapter<ExploreRecyclerAdapter.BusinessViewHolder> {

    private List<CardList> cardList;
    private Context context;

    public ExploreRecyclerAdapter(List<CardList> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
    }

    @Override
    public BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        return new BusinessViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BusinessViewHolder holder, int position) {
        final int i = position;
        CardList card = cardList.get(position);
        holder.name.setText(card.getName());
        Picasso.with(context)
                .load(card.getImageURL())
                .into(holder.image);
        holder.relLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You just clicked on business #" + i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class BusinessViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView image;
        public RelativeLayout relLayout;

        public BusinessViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.businessName);
            image = (ImageView) itemView.findViewById(R.id.businessImage);
            relLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
        }
    }
}
