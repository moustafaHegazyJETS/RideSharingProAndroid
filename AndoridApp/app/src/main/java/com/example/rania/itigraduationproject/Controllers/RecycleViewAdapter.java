package com.example.rania.itigraduationproject.Controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rania.itigraduationproject.Interfaces.ClickListener;
import com.example.rania.itigraduationproject.R;
import com.example.rania.itigraduationproject.TripDetailsActivity;
import com.example.rania.itigraduationproject.TripShowActivity;
import com.example.rania.itigraduationproject.model.Trip;

import java.io.Serializable;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.TripViewHolder> {

   private Context context;
   private List<Trip>tripList;
   private static ClickListener clickListener;

    public RecycleViewAdapter(Context context, List<Trip> tripList) {
        this.context = context;
        this.tripList = tripList;
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView tripName;
        ImageView tripPhoto;


        TripViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            tripName = (TextView)itemView.findViewById(R.id.trip_name);
            tripPhoto = (ImageView) itemView.findViewById(R.id.Trip_photo);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }
    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.list_layout,null);
        return  new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, int position) {
        Trip trip=tripList.get(position);
        holder.tripName.setText(trip.getTripName());


    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}
