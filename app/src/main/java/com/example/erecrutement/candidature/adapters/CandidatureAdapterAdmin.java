package com.example.erecrutement.candidature.adapters;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erecrutement.R;
import com.example.erecrutement.candidature.database.CandidatureDBHelper;
import com.example.erecrutement.candidature.model.Candidature;
import com.example.erecrutement.candidature.ui.admin.CandidatureListAdminFragment;
import com.example.erecrutement.candidature.ui.admin.UpdateCandidatureAdminActivity;

import java.io.File;
import java.util.List;

public class CandidatureAdapterAdmin extends RecyclerView.Adapter<CandidatureAdapterAdmin.ViewHolder> {

    private List<Candidature> candidatures;
    private CandidatureListAdminFragment candidatureListFragment;

    public CandidatureAdapterAdmin(List<Candidature> candidatures, CandidatureListAdminFragment candidatureListFragment) {
        this.candidatures = candidatures;
        this.candidatureListFragment = candidatureListFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_candidature, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Candidature candidature = candidatures.get(position);

        holder.textViewNomOffre.setText("Offre: " + candidature.getNomOffre());
        holder.textViewNom.setText("Nom: " + candidature.getNom());
        holder.textViewPrenom.setText("Prenom: " + candidature.getPrenom());
        holder.textViewNumero.setText("Numero: " + candidature.getNumero());

        holder.buttonDownloadPdf.setOnClickListener(view -> {
            String pdfFilePath = candidature.getCvFilePath();
            if (pdfFilePath != null && !pdfFilePath.isEmpty()) {
                openPdf(view.getContext(), pdfFilePath);
            } else {
                Toast.makeText(view.getContext(), "PDF file path is empty", Toast.LENGTH_SHORT).show();
            }
        });

        long etat = candidature.getEtat();
        String etatText;
        int textColor;

        switch ((int) etat) {
            case 0:
                etatText = "En attente";
                textColor = Color.BLUE;
                break;
            case -1:
                etatText = "Refusé";
                textColor = Color.RED;
                break;
            case 1:
                etatText = "Accepté";
                textColor = Color.GREEN;
                break;
            default:
                etatText = "Unknown";
                textColor = Color.BLACK;
                break;
        }

        holder.textViewEtat.setText("Etat: " + etatText);
        holder.textViewEtat.setTextColor(textColor);

        // Update Button
        holder.buttonUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), UpdateCandidatureAdminActivity.class);
            intent.putExtra("candidature_id", candidature.getId());
            view.getContext().startActivity(intent);
        });

        // Delete Button
        holder.buttonDelete.setOnClickListener(view -> {
            showDeleteConfirmationDialog(view.getContext(), candidature.getId());
        });
    }

    private void openPdf(Context context, String pdfFilePath) {
        File file = new File(pdfFilePath);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return candidatures.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView textViewNomOffre;
        public final TextView textViewEtat;
        public final TextView textViewNom;
        public final TextView textViewPrenom;
        public final TextView textViewNumero;
        public final Button buttonDownloadPdf;
        public final ImageButton buttonUpdate;
        public final ImageButton buttonDelete;

        public ViewHolder(View view) {
            super(view);
            this.view = view;

            textViewNomOffre = view.findViewById(R.id.textViewOffre);
            textViewEtat = view.findViewById(R.id.textViewEtat);
            textViewNom = view.findViewById(R.id.textViewNom);
            textViewPrenom = view.findViewById(R.id.textViewPrenom);
            textViewNumero = view.findViewById(R.id.textViewNumero);
            buttonDownloadPdf = view.findViewById(R.id.buttonDownloadPdf);
            buttonUpdate = view.findViewById(R.id.buttonUpdate);
            buttonDelete = view.findViewById(R.id.buttonDelete);
        }
    }

    private void showDeleteConfirmationDialog(Context context, long candidatureId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this candidature?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            deleteCandidature(context, candidatureId);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    private void deleteCandidature(Context context, long candidatureId) {
        CandidatureDBHelper candidatureDBHelper = new CandidatureDBHelper(context);
        candidatureDBHelper.open();
        int deleted = candidatureDBHelper.delete(candidatureId);
        candidatureDBHelper.close();

        if (deleted != -1) {
            Toast.makeText(context, "Candidature deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to delete candidature", Toast.LENGTH_SHORT).show();
        }

        candidatureListFragment.loadData();
    }
}
