package com.example.cityassistant;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class recviewfeedb extends RecyclerView.Adapter<recviewfeedb.ViewHolder> {

    Context ctx;
    ArrayList<feedbackconst> fb;

    public recviewfeedb() {

    }

    public recviewfeedb(Context ctx, ArrayList<feedbackconst> fb) {
        this.ctx = ctx;
        this.fb = fb;
    }


    @NonNull
    @Override
    public recviewfeedb.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(ctx)
                .inflate(R.layout.activity_recviewfeedb, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull recviewfeedb.ViewHolder holder, int position) {

        holder.feedb.setText(fb.get(position).getF());
        holder.time.setText(fb.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        if (fb == null) {
            return 0;
        } else {
            return fb.size();
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView time, feedb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.timefeedbReceived);
            feedb = itemView.findViewById(R.id.txtfeedbReceived);

        }
    }

}



