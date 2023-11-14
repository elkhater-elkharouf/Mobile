package com.example.erecrutement.candidature.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erecrutement.R;
import com.example.erecrutement.candidature.adapters.CandidatureAdapterUser;
import com.example.erecrutement.candidature.database.CandidatureDBHelper;

import java.util.ArrayList;

public class CandidatureListUserFragment extends Fragment {

    private RecyclerView recyclerView;
    private CandidatureAdapterUser candidatureAdapterUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candidature_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCandidatureList);

        // Initialize the adapter here
        candidatureAdapterUser = new CandidatureAdapterUser(new ArrayList<>(), this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData() {
        CandidatureDBHelper candidatureDBHelper = new CandidatureDBHelper(requireContext());
        candidatureDBHelper.open();

        // Set the data to the adapter and notify the changes
        candidatureAdapterUser.setData(candidatureDBHelper.getAll());
        recyclerView.setAdapter(candidatureAdapterUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}
