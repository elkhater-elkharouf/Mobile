package com.example.erecrutement.candidature.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erecrutement.R;
import com.example.erecrutement.candidature.adapters.CandidatureAdapterAdmin;
import com.example.erecrutement.candidature.database.CandidatureDBHelper;

public class CandidatureListAdminFragment extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candidature_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCandidatureList);

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

        recyclerView.setAdapter(new CandidatureAdapterAdmin(candidatureDBHelper.getAll(), this));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}
