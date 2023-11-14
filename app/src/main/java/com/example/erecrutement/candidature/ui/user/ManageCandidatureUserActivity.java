package com.example.erecrutement.candidature.ui.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.erecrutement.MainActivity;
import com.example.erecrutement.R;
import com.example.erecrutement.candidature.database.CandidatureDBHelper;
import com.example.erecrutement.candidature.model.Candidature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class ManageCandidatureUserActivity extends AppCompatActivity {

    private EditText editTextNom;
    private EditText editTextPrenom;
    private EditText editTextNumero;
    private EditText editTextCvFilePath;
    private long candidatureId;
    private String nomOffre;
    private Uri selectedFileUri;

    private static final int PICK_FILE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_candidature);

        initializeViews();

        Button buttonAddOrUpdateCandidature = findViewById(R.id.buttonManageCandidature);
        Button buttonPickFile = findViewById(R.id.buttonPickFile);

        TextView topTextView = findViewById(R.id.topTextView);
        candidatureId = getIntent().getLongExtra("candidature_id", -1);
        nomOffre = getIntent().getStringExtra("nomOffre");

        if (nomOffre == null) nomOffre = "Null";

        Candidature candidature;

        if (candidatureId != -1) {
            CandidatureDBHelper candidatureDBHelper = new CandidatureDBHelper(ManageCandidatureUserActivity.this);
            candidatureDBHelper.open();

            candidature = candidatureDBHelper.getById(candidatureId);

            candidatureDBHelper.close();

            loadCandidatureDetails(candidature);
            topTextView.setText("Update candidature");
            buttonAddOrUpdateCandidature.setText("Update");
        } else {
            candidature = null;
        }

        buttonAddOrUpdateCandidature.setOnClickListener(view -> {
            if (validateInput()) {
                if (candidatureId != -1) {
                    updateCandidature(candidature);
                } else {
                    addCandidature();
                }
            }

        });

        buttonPickFile.setOnClickListener(view -> pickFile());
    }

    private boolean validateInput() {
        String nom = editTextNom.getText().toString().trim();
        String prenom = editTextPrenom.getText().toString().trim();
        String numero = editTextNumero.getText().toString().trim();

        if (nom.isEmpty()) {
            showAlert("Veuillez saisir un nom");
            return false;
        }

        if (prenom.isEmpty()) {
            showAlert("Veuillez saisir un prénom");
            return false;
        }

        if (numero.length() != 8) {
            showAlert("Veuillez saisir un numéro de téléphone valide de 8 chiffres");
            return false;
        }

        if (selectedFileUri == null) {
            showAlert("Veuillez sélectionner un fichier");
            return false;
        }

        return true;
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Input Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    // You can handle the OK button click if needed
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void initializeViews() {
        editTextNom = findViewById(R.id.editTextNom);
        editTextPrenom = findViewById(R.id.editTextPrenom);
        editTextNumero = findViewById(R.id.editTextNumero);
        editTextCvFilePath = findViewById(R.id.editTextCvFilePath);
    }

    private void loadCandidatureDetails(Candidature candidature) {
        editTextNom.setText(candidature.getNom());
        editTextPrenom.setText(candidature.getPrenom());
        editTextNumero.setText(candidature.getNumero());
        editTextCvFilePath.setText(candidature.getCvFilePath());
    }

    private void updateCandidature(Candidature candidature) {
        Candidature updatedCandidature = createCandidatureFromInput(candidature);
        updatedCandidature.setId(candidatureId);

        int updated = updateCandidatureInDatabase(updatedCandidature);

        handleUpdateResult(updated);
    }

    private void addCandidature() {
        Candidature newCandidature = createCandidatureFromInput(null);

        long insertedId = insertCandidatureIntoDatabase(newCandidature);

        handleInsertResult(insertedId);
    }

    private Candidature createCandidatureFromInput(Candidature oldCandidature) {
        String nom = editTextNom.getText().toString();
        String prenom = editTextPrenom.getText().toString();
        String numero = editTextNumero.getText().toString();

        Candidature candidature = new Candidature();
        candidature.setNom(nom);
        candidature.setPrenom(prenom);
        candidature.setNumero(numero);
        candidature.setNomOffre(nomOffre);

        if (selectedFileUri != null) {
            candidature.setCvFilePath(selectedFileUri.getPath());
        } else {
            if (oldCandidature != null) {
                candidature.setCvFilePath(oldCandidature.getCvFilePath());
            }
        }

        return candidature;
    }

    private void handleUpdateResult(int updated) {
        if (updated != -1) {
            showToast("Candidature updated successfully");
            finish();
        } else {
            showToast("Failed to update candidature");
        }
    }

    private void handleInsertResult(long insertedId) {
        if (insertedId != -1) {
            showToast("Candidature added successfully");
            finish();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                MainActivity.mainActivity.navigateToCandidatureUser();
            }, 500);

        } else {
            showToast("Failed to add candidature");
        }
    }

    private int updateCandidatureInDatabase(Candidature updatedCandidature) {
        CandidatureDBHelper candidatureDBHelper = new CandidatureDBHelper(ManageCandidatureUserActivity.this);
        candidatureDBHelper.open();

        int updated = candidatureDBHelper.update(updatedCandidature);

        candidatureDBHelper.close();
        return updated;
    }

    private long insertCandidatureIntoDatabase(Candidature newCandidature) {
        CandidatureDBHelper candidatureDBHelper = new CandidatureDBHelper(ManageCandidatureUserActivity.this);
        candidatureDBHelper.open();

        long insertedId = candidatureDBHelper.add(newCandidature);

        candidatureDBHelper.close();
        return insertedId;
    }

    private void showToast(String message) {
        Toast.makeText(ManageCandidatureUserActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void pickFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf"); // Set the MIME type to PDF
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            String fileName = getFileName(data.getData());
            editTextCvFilePath.setText(fileName);

            selectedFileUri = saveFileToInternalStorage(data.getData());
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private Uri saveFileToInternalStorage(Uri selectedFileUri) {
        try {
            // Open an input stream from the selected image URI
            InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);

            // Create a file in the app's internal storage directory
            File internalStorageDir = getFilesDir();
            String fileName = "cv_" + System.currentTimeMillis() + ".pdf";
            File file = new File(internalStorageDir, fileName);

            // Open an output stream to the file
            OutputStream outputStream = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                outputStream = Files.newOutputStream(file.toPath());
            }

            // Copy the image data from the input stream to the output stream
            byte[] buffer = new byte[1024];
            int length;
            while (true) {
                assert inputStream != null;
                if (!((length = inputStream.read(buffer)) > 0)) break;
                outputStream.write(buffer, 0, length);
            }

            // Close the streams
            inputStream.close();
            outputStream.close();

            // Now, the selected image is saved to the app's internal storage
            // You can use file.getAbsolutePath() to access the saved image path

            // Return the new Uri for the saved image
            return Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}