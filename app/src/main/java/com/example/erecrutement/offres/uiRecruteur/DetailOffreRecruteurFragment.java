package com.example.erecrutement.offres.uiRecruteur;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erecrutement.R;
import com.example.erecrutement.offres.database.Appdatabase;
import com.example.erecrutement.offres.entity.Offre;
import com.squareup.picasso.Picasso;

import java.io.File;


public class DetailOffreRecruteurFragment extends Fragment {
    private static final String ARG_OFFRE = "offre";
    private EditText titleTextView, companyTextView, JobTypeTextView, JobDescriptionTextView ,
            JobCompagnyTextView , JobCompagny2TextView ,  JobLocationTextView , JobCompagnyUrlTextView;
    public static DetailOffreRecruteurFragment newInstance(Offre offre) {
        DetailOffreRecruteurFragment fragment = new DetailOffreRecruteurFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_OFFRE, offre);
        fragment.setArguments(args);
        return fragment;
    }

    public  Offre selectedOffre;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Utilisez getArguments() pour récupérer les arguments
         selectedOffre = (Offre) getArguments().getSerializable(ARG_OFFRE);
        Toast.makeText(requireContext(), "welcome offre selected: " + selectedOffre.getTitleJob(), Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_offre_recruteur, container, false);

        // Ajoutez cette partie pour charger l'image
        ImageView imgJob = view.findViewById(R.id.imageJobDetail);


        // Obtenez le répertoire des fichiers externes pour "ImagesOffres" en utilisant le contexte du fragment
        File externalFilesDir = requireContext().getExternalFilesDir("");

        // Vérifiez si externalFilesDir n'est pas null (pour des raisons de sécurité)
        if (externalFilesDir != null) {
            // Construisez le chemin complet vers le fichier image
            File imageFile = new File(externalFilesDir, selectedOffre.getImagePath());

            // Vérifiez si le fichier existe et chargez l'image avec Glide ou Picasso
            if (imageFile.exists()) {
                // Utilisez Glide pour charger l'image
                //Glide.with(this).load(Uri.fromFile(imageFile)).into(imgJob);
                Picasso.get().load(Uri.fromFile(imageFile)).into(imgJob);

                // OU utilisez Picasso pour charger l'image
                // Picasso.get().load(Uri.fromFile(imageFile)).into(imgJob);
            } else {
                Log.e("ImageLoadError", "Le fichier n'existe pas : " + imageFile.getAbsolutePath());
                // Vous pouvez définir une image de substitution ou gérer l'erreur d'une autre manière
                imgJob.setImageResource(R.drawable.baseline_work_24);
            }
        } else {
            Log.e("ExternalFilesDir", "Le répertoire des fichiers externes est null");
        }





        titleTextView = view.findViewById(R.id.idJobTitle);
        titleTextView.setText(selectedOffre.getTitleJob());

         companyTextView = view.findViewById(R.id.idJobCompagny);
        companyTextView.setText(selectedOffre.getNameCompagny());

         JobTypeTextView = view.findViewById(R.id.idJobType);
        JobTypeTextView.setText(selectedOffre.getTypeJob());

         JobDescriptionTextView = view.findViewById(R.id.idJobDescription);
        JobDescriptionTextView.setText(selectedOffre.getJobDescription());

         JobCompagnyTextView = view.findViewById(R.id.idJobCompagny);
        JobCompagnyTextView.setText(selectedOffre.getNameCompagny());

         JobCompagny2TextView = view.findViewById(R.id.idJobCompagny2);
        JobCompagny2TextView.setText(selectedOffre.getNameCompagny());

         JobLocationTextView = view.findViewById(R.id.idJobLocation);
        JobLocationTextView.setText(selectedOffre.getLocationCompagny());

         JobCompagnyUrlTextView = view.findViewById(R.id.idJobCompagnyUrl);
        JobCompagnyUrlTextView.setText(selectedOffre.getCompanyUrl());

        Button btnDelete = view.findViewById(R.id.btnSupprimer);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to delete this offer?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked Yes, delete the offer
                        Appdatabase.getInstance(requireContext()).offreDAO.deleteOffre(selectedOffre);

                        // Show a toast indicating successful deletion
                        Toast.makeText(requireContext(), "Offer deleted", Toast.LENGTH_SHORT).show();

                        // Navigate back to the list fragment
                        requireActivity().onBackPressed();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked No, do nothing
                        dialog.dismiss(); // Dismiss the dialog
                    }
                });
                // Show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        Button btnUpdate = view.findViewById(R.id.btnModifier);
        btnUpdate.setOnClickListener(this::onUpdateButtonClick);

        return  view;
    }


    public void onUpdateButtonClick(View view) {
        // Retrieve data from UI components
        String titleJob = titleTextView.getText().toString().trim();
        String typeJob = JobTypeTextView.getText().toString().trim();
        String nameCompagny = companyTextView.getText().toString().trim();
        String locationCompagny = JobLocationTextView.getText().toString().trim();
        String jobDescription = JobDescriptionTextView.getText().toString().trim();
        String companyUrl = JobCompagnyUrlTextView.getText().toString().trim();
        // Update the fields of the selected offer directly
        selectedOffre.setTitleJob(titleJob);
        selectedOffre.setTypeJob(typeJob);
        selectedOffre.setNameCompagny(nameCompagny);
        selectedOffre.setLocationCompagny(locationCompagny);
        selectedOffre.setJobDescription(jobDescription);
        selectedOffre.setCompanyUrl(companyUrl);

        // Update the offer in the database
        Appdatabase.getInstance(requireContext()).offreDAO.updateOffre(selectedOffre);

        // Show a toast indicating successful update
        Toast.makeText(requireContext(), "Offer updated", Toast.LENGTH_SHORT).show();

        // Update the UI with the new data
        titleTextView.setText(selectedOffre.getTitleJob());
        companyTextView.setText(selectedOffre.getNameCompagny());
        JobTypeTextView.setText(selectedOffre.getTypeJob());
        JobLocationTextView.setText(selectedOffre.getLocationCompagny());
        JobDescriptionTextView.setText(selectedOffre.getJobDescription());
        JobCompagnyUrlTextView.setText(selectedOffre.getCompanyUrl());

    }
}