package com.example.cityassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

import timber.log.Timber;

public class recviewplaces extends RecyclerView.Adapter<recviewplaces.ViewHolder> {

    Context c;
    ArrayList<checkTheme> ct;

    public static final String tag = "locations viewing ";

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference("Places");


    public recviewplaces() {

    }

    public recviewplaces(Context c, ArrayList<checkTheme> ct) {
        this.c = c;
        this.ct = ct;
    }

    @NonNull
    @Override
    public recviewplaces.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(c)
                .inflate(R.layout.activity_recviewplaces, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(ct.get(position).getNameOfPlace());
        holder.date.setText(ct.get(position).getDate());

        Double la = ct.get(position).getLat();
        Double lt = ct.get(position).getLng();

        holder.longitude.setText(la.toString());
        holder.latitude.setText(lt.toString());
        holder.descrip.setText(ct.get(position).getTypeOfInstitution());


        Glide.with(c).load(ct.get(position).getImageUri())
                .error(" image was not found ").into(holder.image);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // build an alert dialog to it  to ask the  user
                AlertDialog.Builder bd = new AlertDialog.Builder(holder.delete.getContext());

                AlertDialog alertDialog = bd.create();

                bd.setMessage(" Are you sure you want to delete this location?");
                bd.setTitle("Delete A Location");

                bd.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ref.child(ct.get(position).getPlaceId()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Timber.tag(tag).d(" location deleted successfully");

                                                    Toast.makeText(holder.delete.getContext(), "deleted successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(holder.delete.getContext(), "ooops.." + task.getException(),
                                                            Toast.LENGTH_SHORT).show();

                                                    Timber.tag(tag).d(task.getException());

                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(holder.delete.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                                Timber.tag(tag).d(e);
                                            }
                                        });

                            }
                        });
                bd.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();


                    }
                });


                alertDialog.show();


            }
        });


    }

    @Override
    public int getItemCount() {
        if (ct == null) {
            return 0;
        } else {
            return ct.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, date, descrip, latitude, longitude;
        ImageButton delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            delete = itemView.findViewById(R.id.deletePlaces);
            image = itemView.findViewById(R.id.imagesPlaces);
            name = itemView.findViewById(R.id.namePlaces);
            date = itemView.findViewById(R.id.timePlaces);
            descrip = itemView.findViewById(R.id.descriptionPlaces);
            latitude = itemView.findViewById(R.id.latPlaces);
            longitude = itemView.findViewById(R.id.lngPlaces);

        }
    }
}