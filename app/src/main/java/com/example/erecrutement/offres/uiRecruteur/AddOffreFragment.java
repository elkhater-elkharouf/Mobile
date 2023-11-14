package com.example.erecrutement.offres.uiRecruteur;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.erecrutement.R;
import com.example.erecrutement.offres.database.Appdatabase;
import com.example.erecrutement.offres.entity.Offre;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

public class AddOffreFragment extends Fragment {

    double latitude ;
    double longitude ;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String locationName;
    // UI components
    private EditText editTitleJob, editTypeJob, editNameCompagny, editLocationCompagny , jobDescription , companyUrl;
    private ImageView imageView ;  // Assuming you have an ImageView in your layout
    ImageView imgGallery;
    private Appdatabase appDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_offre, container, false);
        imgGallery = view.findViewById(R.id.imgGallery);

        // Initialize UI components
        editTitleJob = view.findViewById(R.id.editTitleJob);
        editTypeJob = view.findViewById(R.id.editTypeJob);
        editNameCompagny = view.findViewById(R.id.editNameCompagny);
        editLocationCompagny = view.findViewById(R.id.editLocationCompagny);
        jobDescription = view.findViewById(R.id.jobDescription);
        companyUrl = view.findViewById(R.id.companyUrl);
        //imageView = view.findViewById(R.id.);

        // Initialize the database
        appDatabase = Appdatabase.getInstance(requireContext());


        Button btnOpen = view.findViewById(R.id.btnChooseImage);

        // Button click listeners
        Button btnChooseImage = view.findViewById(R.id.btnChooseImage);
        btnChooseImage.setOnClickListener(this::onChooseImage);

        // Add this to your onCreateView method after initializing views
        ImageView iconLocation = view.findViewById(R.id.iconLocation);
        iconLocation.setOnClickListener(this::onLocationIconClick);

        Button btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this::onSaveButtonClick);



        return view;
    }

private final int GALLERY_REQ_CODE =1000;

    public void onChooseImage(View view) {
        // Your implementation for handling the button click goes here
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

        Intent iGallery = new Intent(Intent.ACTION_PICK);
        iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(iGallery,GALLERY_REQ_CODE);
    }

    // Ajoutez cette méthode pour gérer le résultat de la sélection d'image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
             selectedImageUri = data.getData();
            Toast.makeText(requireContext(), "selectedImageUri selected: " + selectedImageUri, Toast.LENGTH_SHORT).show();

            // Utilisez également PICK_IMAGE_REQUEST ici
            if (requestCode == PICK_IMAGE_REQUEST) {
                Toast.makeText(requireContext(), "d5al fel if ", Toast.LENGTH_SHORT).show();
                Picasso.get().load(selectedImageUri).into(imgGallery);
                imgGallery.setVisibility(View.VISIBLE); // Make the ImageView visible
            }
        }
    }
    private Uri selectedImageUri;

    public void onSaveButtonClick(View view) {

        // Retrieve data from UI components
        String titleJob = editTitleJob.getText().toString().trim();
        String typeJob = editTypeJob.getText().toString().trim();
        String nameCompagny = editNameCompagny.getText().toString().trim();
        String locationCompagny = editLocationCompagny.getText().toString().trim();
        String jobDecription = jobDescription.getText().toString().trim();
        String companyUrll = companyUrl.getText().toString().trim();

        // Get the resource ID of the selected image (assuming you have stored it in a variable)
        //int imgJobResource = R.drawable.your_selected_image;  // Replace with the actual resource ID

        // Generate a unique code using UUID
        String uniqueCode = UUID.randomUUID().toString();

        // Modify the image name
        String imageName = "logo" + nameCompagny + uniqueCode + ".png";

        // Copy the selected image to external storage
        copyImageToExternalStorage(selectedImageUri, imageName);

        // Create an Offre object
        Offre offre = new Offre(titleJob, typeJob, nameCompagny, locationCompagny, "ImagesOffres/" + imageName, R.drawable.baseline_work_24,imageName , jobDecription , companyUrll , latitude , longitude , false);
        offre.setImageName(imageName);
        offre.setLatitude(latitude);
        offre.setLongitude(longitude);
        // Add the Offre to the database

        try {
            appDatabase.offreDAO.addOffre(offre);

            // Display a toast message indicating success
            showToast("Offer added successfully");

            // Optionally, you can clear the input fields or perform other actions after saving.
          //  clearInputFields();
        } catch (Exception e) {
            // Display a toast message indicating failure
            showToast("Failed to add offer: " + e.getMessage());
            System.out.println("error : "+e.getMessage());
        }

        File externalFilesDir = requireContext().getExternalFilesDir("ImagesOffres");
        if (externalFilesDir != null) {
            String path = externalFilesDir.getAbsolutePath();
            Log.d("ExternalFilesDir", "Path: " + path);
            Toast.makeText(requireContext(), "Path: " + path, Toast.LENGTH_LONG).show();
        } else {
            Log.e("ExternalFilesDir", "External files directory is null");
        }

        // Optionally, you can clear the input fields or perform other actions after saving.
        //clearInputFields();

        // Navigate back to the list of offers
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ListOffresFragment())
                .commit();

    }
    private void clearInputFields() {
        editTitleJob.setText("");
        editTypeJob.setText("");
        editNameCompagny.setText("");
        editLocationCompagny.setText("");
      //  imageView.setImageURI(null);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void copyImageToExternalStorage(Uri sourceUri, String destinationFileName) {
        // Copy the image to external storage
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(sourceUri);
            FileOutputStream outputStream = new FileOutputStream(new File(requireContext().getExternalFilesDir("ImagesOffres"), destinationFileName));

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Add this method to your AddOffreFragment class
    public void onLocationIconClick(View view) {
        // Handle the click event here, for example, navigate to another fragment
        // You can replace NextFragment.class with the fragment you want to navigate to
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MapFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Mettre à jour le champ editLocationCompagny avec le nom de l'emplacement
        Bundle args = getArguments();
        if (args != null) {
            String locationName = args.getString("locationName");
             latitude = args.getDouble("latitude", 0.0);
             longitude = args.getDouble("longitude", 0.0);
            if (locationName != null) {
                editLocationCompagny.setText(locationName);
            }
        }
    }






}