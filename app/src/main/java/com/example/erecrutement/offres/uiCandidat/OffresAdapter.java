package com.example.erecrutement.offres.uiCandidat;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erecrutement.R;
import com.example.erecrutement.offres.database.Appdatabase;
import com.example.erecrutement.offres.entity.Offre;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class OffresAdapter extends RecyclerView.Adapter<OffresAdapter.OffresViewHolder> {
    private List<Offre> offresList;
    private Appdatabase appDatabase;
    public OffresAdapter(List<Offre> offresList, Appdatabase appDatabase) {
        this.offresList = offresList;
        this.appDatabase = appDatabase;
    }
    @NonNull
    @Override
    public OffresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offre, parent, false);
        return new OffresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OffresViewHolder holder, int position) {
        Offre offre = offresList.get(position);

        // Définir les données dans le ViewHolder
        holder.jobTitleTextView.setText(offre.getTitleJob());
        holder.companyTextView.setText(offre.getNameCompagny());
        holder.locationTextView.setText(offre.getLocationCompagny());
        holder.jobTypeTextView.setText(offre.getTypeJob());

        int color = offre.isClicked() ? R.color.colorBleu : R.color.colorRouge;
        holder.mark.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), color));

        // Ajouter un OnClickListener pour l'ImageView "mark"
        holder.mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inverser l'état isClicked et mettre à jour l'élément
                boolean newIsClicked = !offre.isClicked();
                // Mettre à jour l'élément dans la base de données
                offre.setClicked(newIsClicked);
                appDatabase.offreDAO.updateOffre(offre);

                // Mettre à jour l'UI avec le nouvel état
                updateItemState(holder.getAdapterPosition(), newIsClicked);
            }
        });


       // holder.imgJob.setImageResource(offre.getImgJob());
       // holder.jobDescriptionTextView.setText(offre.getJobDescription());
        // holder.companyTextView.setText(offre.getCompanyUrl());

        //Picasso.get().load(new File(offre.getImagePath())).into(holder.imgJob);
        // Get the externalFilesDir for "ImagesOffres" using the fragment's context
       // File externalFilesDir = requireContext().getExternalFilesDir("ImagesOffres");
        File externalFilesDir = holder.itemView.getContext().getExternalFilesDir("");
        // Check if externalFilesDir is not null (for safety)
        if (externalFilesDir != null) {
            // Construct the full path to the image file
            File imageFile = new File(externalFilesDir, offre.getImagePath());

            // Check if the file exists and load the image with Picasso
            if (imageFile.exists()) {
                Picasso.get().load(Uri.fromFile(imageFile)).into(holder.imgJob);
            } else {
                Log.e("ImageLoadError", "File does not exist: " + imageFile.getAbsolutePath());
                // You can set a placeholder image or handle the error in another way
                holder.imgJob.setImageResource(R.drawable.baseline_work_24);
            }
        } else {
            Log.e("ExternalFilesDir", "External files directory is null");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                if (onItemClickListener != null && clickedPosition != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(clickedPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return offresList.size();
    }
    private OnItemClickListener onItemClickListener; // Add this line
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public void updateItemState(int position, boolean isClicked) {
        Offre offre = offresList.get(position);
        offre.setClicked(isClicked);
        notifyItemChanged(position);
    }
    public static class OffresViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitleTextView;
        TextView companyTextView;
        TextView locationTextView;
        TextView jobTypeTextView;
        TextView jobDescriptionTextView;
        TextView companyUrlTextView;
        ImageView imgJob , mark;
        public OffresViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.idJobTitle);
            companyTextView = itemView.findViewById(R.id.idJobCompagny);
            locationTextView = itemView.findViewById(R.id.idJobLocation);
            jobTypeTextView = itemView.findViewById(R.id.idJobType);
           // jobDescriptionTextView = itemView.findViewById(R.id.jobDescription);
           // companyUrlTextView = itemView.findViewById(R.id.companyUrl); // Fix the name here
            imgJob = itemView.findViewById(R.id.offreImageView);
            mark = itemView.findViewById(R.id.mark);
        }


    }
}