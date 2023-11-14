package com.example.erecrutement.candidature.ui.admin;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.erecrutement.R;
import com.example.erecrutement.candidature.database.CandidatureDBHelper;
import com.example.erecrutement.candidature.model.Candidature;

public class UpdateCandidatureAdminActivity extends AppCompatActivity {

    private Spinner spinnerEtat;
    private long candidatureId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_candidature_admin);

        initializeViews();

        TextView topTextView = findViewById(R.id.topTextView);
        Button buttonAddOrUpdateCandidature = findViewById(R.id.buttonManageCandidature);

        candidatureId = getIntent().getLongExtra("candidature_id", -1);

        Candidature candidature;

        if (candidatureId != -1) {
            CandidatureDBHelper candidatureDBHelper = new CandidatureDBHelper(UpdateCandidatureAdminActivity.this);
            candidatureDBHelper.open();

            candidature = candidatureDBHelper.getById(candidatureId);

            candidatureDBHelper.close();

            loadCandidatureDetails(candidature);
            buttonAddOrUpdateCandidature.setText("Update");
            topTextView.setText("Update candidature");
        } else {
            candidature = null;
            showToast("Invalid candidature ID");
            finish();
        }

        buttonAddOrUpdateCandidature.setOnClickListener(view -> {
            if (candidatureId != -1) {
                updateCandidature(candidature);
            }
        });
    }

    private void initializeViews() {
        spinnerEtat = findViewById(R.id.spinnerEtat);
    }

    private void loadCandidatureDetails(Candidature candidature) {
        long etat = candidature.getEtat();
        String etatText;

        switch ((int) etat) {
            case 0:
                etatText = "En attente";
                break;
            case 1:
                etatText = "Accepté";
                break;
            case -1:
                etatText = "Refusé";
                break;
            default:
                etatText = "Unknown";
                break;
        }

        // Assuming spinnerEtat is an instance of Spinner
        ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) spinnerEtat.getAdapter();
        if (spinnerAdapter != null) {
            int position = spinnerAdapter.getPosition(etatText);
            if (position != -1) {
                spinnerEtat.setSelection(position);
            }
        }
    }


    private void updateCandidature(Candidature candidature) {
        Candidature updatedCandidature = createCandidatureFromInput(candidature);
        updatedCandidature.setId(candidatureId);

        int updated = updateCandidatureInDatabase(updatedCandidature);

        handleUpdateResult(updated);
    }

    private Candidature createCandidatureFromInput(Candidature oldCandidature) {
        String etatText = spinnerEtat.getSelectedItem().toString();
        int etat;

        // Convert etatText to corresponding integer value
        switch (etatText) {
            case "En attente":
                etat = 0;
                break;
            case "Accepté":
                etat = 1;
                break;
            case "Refusé":
                etat = -1;
                break;
            default:
                etat = 0;

                break;
        }

        oldCandidature.setEtat(etat);

        return oldCandidature;
    }

    private void handleUpdateResult(int updated) {
        if (updated != -1) {
            showToast("Candidature updated successfully");
            finish();
        } else {
            showToast("Failed to update candidature");
        }
    }

    private int updateCandidatureInDatabase(Candidature updatedCandidature) {
        CandidatureDBHelper candidatureDBHelper = new CandidatureDBHelper(UpdateCandidatureAdminActivity.this);
        candidatureDBHelper.open();

        int updated = candidatureDBHelper.update(updatedCandidature);

        candidatureDBHelper.close();
        return updated;
    }

    private void showToast(String message) {
        Toast.makeText(UpdateCandidatureAdminActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
