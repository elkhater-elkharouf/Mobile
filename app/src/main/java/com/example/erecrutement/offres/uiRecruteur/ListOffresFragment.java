package com.example.erecrutement.offres.uiRecruteur;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.erecrutement.R;
import com.example.erecrutement.offres.database.Appdatabase;
import com.example.erecrutement.offres.entity.Offre;
import com.example.erecrutement.offres.uiCandidat.JobDetailFragment;
import com.example.erecrutement.offres.uiCandidat.OffresAdapter;

import java.util.ArrayList;
import java.util.List;


public class ListOffresFragment extends Fragment implements OffresAdapter.OnItemClickListener {
    private List<Offre> offresList;
    private RecyclerView recyclerView;
    private OffresAdapter offresAdapter;
    private Appdatabase appDatabase;
    private OffresAdapter.OnItemClickListener onItemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_list_offres, container, false);

        appDatabase = Appdatabase.getInstance(requireContext());
        recyclerView = view.findViewById(R.id.recyclerViewOffres);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        offresList  = new ArrayList<>();
        //offresList.add(new Offre("FullStack Developer", "CDI", "Vermeg", "Lac 1", R.drawable.baseline_work_24));
        //offresList.add(new Offre("Frontend Developer", "CDI", "TechCorp", "City Center", R.drawable.baseline_work_24));
        //from db
        offresList = appDatabase.offreDAO.getAllOffre();
        offresAdapter = new OffresAdapter(offresList,appDatabase);
        offresAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(offresAdapter);
        ImageView btnAdd2 = view.findViewById(R.id.btnAdd2);
        btnAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClick(v);
            }
        });
        return view;

    }


    public void setOnItemClickListener(OffresAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public void onAddButtonClick(View view) {
        // Utilisez requireActivity() pour obtenir l'activité associée au fragment,
        // puis appelez getSupportFragmentManager() pour obtenir le gestionnaire de fragments.
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddOffreFragment())
                .addToBackStack(null)  // Optionnel : ajoutez cette transaction à la pile de retour
                .commit();
    }


    @Override
    public void onItemClick(int position) {
        // Récupérez l'offre sélectionnée
        Offre selectedOffre = offresList.get(position);
        Toast.makeText(requireContext(), "Offer selected: " + selectedOffre.getTitleJob(), Toast.LENGTH_SHORT).show();

        // Utilisez requireActivity() pour obtenir l'activité associée au fragment,
        // puis appelez getSupportFragmentManager() pour obtenir le gestionnaire de fragments.
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, DetailOffreRecruteurFragment.newInstance(selectedOffre))
                .addToBackStack(null)
                .commit();
    }
}