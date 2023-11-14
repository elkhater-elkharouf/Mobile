package com.example.erecrutement.offres.uiCandidat;

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
import com.example.erecrutement.offres.uiRecruteur.AddOffreFragment;

import java.util.ArrayList;
import java.util.List;


public class OffresFragment extends Fragment implements OffresAdapter.OnItemClickListener  {
    private List<Offre> offresList;
    private RecyclerView recyclerView;
    private OffresAdapter offresAdapter;
    private Appdatabase appDatabase;
    private OffresAdapter.OnItemClickListener onItemClickListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offres, container, false);
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


        return view;
    }
    public void setOnItemClickListener(OffresAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    @Override
    public void onItemClick(int position) {
        // Récupérez l'offre sélectionnée
        Offre selectedOffre = offresList.get(position);
        Toast.makeText(requireContext(), "Offer selected: " + selectedOffre.getTitleJob(), Toast.LENGTH_SHORT).show();

        // Utilisez requireActivity() pour obtenir l'activité associée au fragment,
        // puis appelez getSupportFragmentManager() pour obtenir le gestionnaire de fragments.
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, JobDetailFragment.newInstance(selectedOffre))
                .addToBackStack(null)
                .commit();
    }
}