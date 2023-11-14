package com.example.erecrutement.offres.uiCandidat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.erecrutement.R;
import com.example.erecrutement.candidature.ui.user.ManageCandidatureUserActivity;
import com.example.erecrutement.offres.entity.Offre;
import com.example.erecrutement.offres.uiRecruteur.MapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.File;

public class JobDetailFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    Button postulerButton;
    private static final String ARG_OFFRE = "offre";

    // Ajoutez cette méthode pour créer une nouvelle instance du fragment avec les arguments
    public static JobDetailFragment newInstance(Offre offre) {
        JobDetailFragment fragment = new JobDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_OFFRE, offre);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Utilisez getArguments() pour récupérer les arguments
        Offre selectedOffre = (Offre) getArguments().getSerializable(ARG_OFFRE);
        View view = inflater.inflate(R.layout.fragment_job_detail, container, false);



        // Initialize the MapView
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
       // postulerButton = view.findViewById(R.id.postulerButton);

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


        TextView titleTextView = view.findViewById(R.id.idJobTitle);
        titleTextView.setText(selectedOffre.getTitleJob());

        TextView companyTextView = view.findViewById(R.id.idJobCompagny);
        companyTextView.setText(selectedOffre.getNameCompagny());

        TextView JobTypeTextView = view.findViewById(R.id.idJobType);
        JobTypeTextView.setText(selectedOffre.getTypeJob());

        TextView JobDescriptionTextView = view.findViewById(R.id.idJobDescription);
        JobDescriptionTextView.setText(selectedOffre.getJobDescription());

        TextView JobCompagnyTextView = view.findViewById(R.id.idJobCompagny);
        JobCompagnyTextView.setText(selectedOffre.getNameCompagny());

        TextView JobCompagny2TextView = view.findViewById(R.id.idJobCompagny2);
        JobCompagny2TextView.setText(selectedOffre.getNameCompagny());

        TextView JobLocationTextView = view.findViewById(R.id.idJobLocation);
        JobLocationTextView.setText(selectedOffre.getLocationCompagny());

        TextView JobCompagnyUrlTextView = view.findViewById(R.id.idJobCompagnyUrl);
        JobCompagnyUrlTextView.setText(selectedOffre.getCompanyUrl());
        // ... le reste de votre code

        // Add this to your onCreateView method after initializing views
        ImageView iconLocation = view.findViewById(R.id.iconLocation);
        iconLocation.setOnClickListener(this::onLocationIconClick);
        postulerButton = view.findViewById(R.id.postulerButton);
        postulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "salut malek here", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), ManageCandidatureUserActivity.class);
                intent.putExtra("nomOffre", selectedOffre.getTitleJob());
                view.getContext().startActivity(intent);
            }
        });


        return view;
    }

    private void onLocationIconClick(View view) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MapFragment())
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Get the selected offer
        Offre selectedOffre = (Offre) getArguments().getSerializable(ARG_OFFRE);

        // Extract latitude and longitude from the offer
        double latitude = selectedOffre.getLatitude();
        double longitude = selectedOffre.getLongitude();

        // Create a LatLng object for the marker
        LatLng location = new LatLng(latitude, longitude);

        // Add a marker at the specified location
        googleMap.addMarker(new MarkerOptions().position(location).title(selectedOffre.getLocationCompagny()));

        // Move the camera to the marker
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));
    }


}