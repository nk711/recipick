package com.example.softeng.recipick.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.softeng.recipick.AsyncTasks.NearbySupermarkets;
import com.example.softeng.recipick.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NearbyTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NearbyTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearbyTab extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Holds the reference to the map.
     */
    private GoogleMap gMap;

    /**
     *  Holds the reference to view
     */
    private View view;

    /**
     * Holds the reference to the mapView
     */
    private SupportMapFragment mapFragment;

    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    /**
     * Holds the reference to the Google Api Client
     */
    private GoogleApiClient client;

    /**
     * Holds the reference for requesting current location
     */
    private LocationRequest locationRequest;

    /**
     *  Holds the marker for the current location
     */
    private Marker currentMarker;

    /**
     *  Holds the LatLng of current location
     */
    private LatLng currentLocation;

    /**
     *  Holds the reference to the button to find the nearest supermarkets.
     */
    private Button searchSupermarkets;

    public NearbyTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NearbyTab.
     */
    // TODO: Rename and change types and number of parameters
    public static NearbyTab newInstance(String param1, String param2) {
        NearbyTab fragment = new NearbyTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment; inflating the map view
        view = inflater.inflate(R.layout.fragment_nearby_tab, container, false);

        // Inflating mapFragment
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapView, mapFragment).commit();
        }

        // Call onMapReady when mapFragment is loaded.
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.searchSupermarkets = (Button) view.findViewById(R.id.find);
        this.searchSupermarkets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // findSupermarkets method is invoked
                findSupermarkets();
                // Notify user that it may take a while to find nearby supermarkets
                Toast.makeText(getContext(), "This may take a while. Press again if it takes too long.",Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     *  A method that sends the reference of the URL and the map to the background,
     *  where the method to find nearest supermarkets are done.
     *
     *  Reference: https://developers.google.com/places/web-service/search
     */
    public void findSupermarkets() {
        if(this.currentLocation != null) {
            // Starting URL to find nearest supermarket
            StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            // Based nearby supermarkets using current location
            stringBuilder.append("location=" + this.currentLocation.latitude + "," + this.currentLocation.longitude);
            // Find supermarkets within a 2km radius from current location
            stringBuilder.append("&radius=" + 2000);
            // Keyword set to "supermarket"
            stringBuilder.append("&keyword=" + "supermarket");
            // API KEY
            stringBuilder.append("&key=AIzaSyDaLq6zKdTw4niS4AZu52K2itC-zqrRUSc");

            // Convert stringbuilder to a string
            String url = stringBuilder.toString();

            // Creating an array of type object that holds references for the AsyncTask
            Object transferData[] = new Object[2];
            transferData[0] = gMap;
            transferData[1] = url;

            // Execute the AsyncTask to find the nearest supermarkets.
            NearbySupermarkets getNearbySupermarkets = new NearbySupermarkets();
            getNearbySupermarkets.execute(transferData);

            // Camera zooming option when button is pressed.
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLocation, 13);
            this.gMap.animateCamera(update);
        } else {
            Toast.makeText(getContext(), "Current location is not found.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Initialising gMap field.
        this.gMap = googleMap;
        this.gMap.setOnMapLoadedCallback(this);
        this.gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Building the Google API Client
        this.client = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        // Connect the client
        this.client.connect();
    }

    @Override
    public void onMapLoaded() {
        // Setting map type
        this.gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Allow map to be zoomable
        gMap.getUiSettings().setZoomControlsEnabled(true);
        // Listen for marker clicks
        gMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Moves camera to the marker clicked.
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
        return false;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // If location is null, location is not detected
        if (location == null) {
            Toast.makeText(getContext(), "Location not found", Toast.LENGTH_SHORT).show();
        }
        // Remove previous current location marker if found previously - replace with the new location
        // Called every 1 second
        if (this.currentMarker != null) {
            this.currentMarker.remove();
        }
        // Get the LatLng of current location.
        this.currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        // Creating marker options for the current location marker
        this.currentMarker = gMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_person)));
    }

    /**
     * Called once client is connected
     *
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Creating a location request.
        this.locationRequest = new LocationRequest().create();
        // Request location every 1 second.
        this.locationRequest.setInterval(1000);
        // Priority of accuracy set to high.
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        /**
         * Setting up permissions needed for the application
         */
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // We can request permissions
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

        } else {
            //Permission is granted
        }
        // Request for current location
        LocationServices.FusedLocationApi.requestLocationUpdates(this.client, this.locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
