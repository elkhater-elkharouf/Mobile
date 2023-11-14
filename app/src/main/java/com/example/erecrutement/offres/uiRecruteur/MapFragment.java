package com.example.erecrutement.offres.uiRecruteur;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.erecrutement.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private Marker selectedMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize the MapView
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        Button btnSave = view.findViewById(R.id.btnValiderPosition);
        btnSave.setOnClickListener(this::onValiderPositionClick);
        // Initialize the Button
      // Button btnValiderPosition = view.findViewById(R.id.btnValiderPosition);

        // Set up the onClickListener for the button
      //  btnValiderPosition.setOnClickListener(v -> onValiderPositionClick());

        return view;
    }

    private void onValiderPositionClick(View view) {
        // Check if a location is selected
        if (selectedMarker != null) {
            // Get the selected location's information
            LatLng selectedLocation = selectedMarker.getPosition();
            String locationName = selectedMarker.getTitle(); // Récupérer le titre du marqueur

            // Bundle pour passer les données à AddOffreFragment
            Bundle bundle = new Bundle();
            bundle.putString("locationName", locationName);
            bundle.putDouble("latitude", selectedLocation.latitude);
            bundle.putDouble("longitude", selectedLocation.longitude);

            // Ajouter les données au fragment
            AddOffreFragment addOffreFragment = new AddOffreFragment();
            addOffreFragment.setArguments(bundle);

            // Remplacer le fragment actuel par le nouveau avec les données
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, addOffreFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(requireContext(), "No location selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Initialize the map when it's ready
        this.googleMap = googleMap;

        // Default location (San Francisco)
        LatLng defaultLocation = new LatLng(36.7952629462835, 10.181627954342435);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));

        // Set up the map click listener
        googleMap.setOnMapClickListener(latLng -> onMapClick(latLng));
    }

    private void onMapClick(LatLng latLng) {
        // Remove the previous marker, if any
        if (selectedMarker != null) {
            selectedMarker.remove();
        }
        // Obtenez le nom de l'emplacement (à implémenter)
        String locationName = getLocationNameFromCoordinates(latLng);
        // Add a new marker at the clicked location
        selectedMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title(locationName));

       // selectedMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));

        // Move the camera to the clicked location
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    private String getLocationNameFromCoordinates(LatLng latLng) {
        Geocoder geocoder = new Geocoder(requireContext());
        List<Address> addresses;

        try {
            // Obtenez la liste des adresses à partir des coordonnées
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                // Obtenez le nom de l'emplacement à partir de la première adresse dans la liste
                Address address = addresses.get(0);
                StringBuilder locationName = new StringBuilder();

                // Ajoutez les composants d'adresse pertinents (par exemple, ville, pays)
                if (address.getLocality() != null) {
                    locationName.append(address.getLocality());
                }

                if (address.getCountryName() != null) {
                    if (locationName.length() > 0) {
                        locationName.append(", ");
                    }
                    locationName.append(address.getCountryName());
                }

                return locationName.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // En cas d'échec, retournez une chaîne vide
        return "";
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
}
