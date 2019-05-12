package com.example.softeng.recipick.AsyncTasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.softeng.recipick.Fragments.NearbyTab;
import com.example.softeng.recipick.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Shows supermarkets nearby the users position
 */
public class NearbySupermarkets extends AsyncTask<Object, String, String> {

    /**
     * Holds the reference to the map
     */
    private GoogleMap gMap;

    /**
     * Holds the url for getting the nearby supermarkets
     */
    private String url;

    /**
     * Holds the input stream of the url
     */
    private InputStream inputStream;

    /**
     * Used to read the url from inputStream
     */
    private BufferedReader bufferedReader;

    /**
     * Holds the url in string format
     */
    private StringBuilder stringBuilder;

    /**
     * Holds the url
     */
    private String data;

    @Override
    protected String doInBackground(Object... objects) {
        // Initialising gMap and url
        this.gMap = (GoogleMap) objects[0];
        this.url = (String) objects[1];

        try {
            // Creating a URL object
            URL myURL = new URL(url);
            // Creating a url connection
            HttpURLConnection urlConnection = (HttpURLConnection) myURL.openConnection();
            // Connect the url connection
            urlConnection.connect();
            // Initialising inputSteam to hold the characters of the url.
            this.inputStream = urlConnection.getInputStream();
            // Initialising bufferedReader to read the url
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // Declaring a variable of type String to hold the lines present in bufferedReader
            String line;
            // Initialising stringBuilder
            this.stringBuilder = new StringBuilder();

            // If bufferedReader is not empty, add it to the stringBuilder
            while ((line = this.bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // Convert the characters in stringBuilder to one string
            this.data = this.stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }

        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            // Creating a JSON object to access the results of the url
            JSONObject parentObject = new JSONObject(s);
            // We want the results to get the information of the nearest supermarkets
            JSONArray results = parentObject.getJSONArray("results");

            // Loop through the results "block"
            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonObject = results.getJSONObject(i);
                // Reference to the information inside geometry; which is accessed via results
                JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");

                // Access the location within geometry
                String lat = locationObj.getString("lat");
                String lng = locationObj.getString("lng");

                // Reference to the name of the supermarket
                JSONObject nameObj = results.getJSONObject(i);

                // Holds the name of the supermarket
                String supermarketName = nameObj.getString("name");
                // Holds the vicinity of the address
                String vicinity = nameObj.getString("vicinity");

                // Holds the latitude and longitude of the supermarket.
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                // Creating the marker for the supermarket
                MarkerOptions markerOptions = new MarkerOptions();
                // Title set to supermarket name
                markerOptions.title(supermarketName);
                // Snippet set to be the vicinity
                markerOptions.snippet(vicinity);
                // Position set to be the LatLng of the supermarket
                markerOptions.position(latLng);

                // Add marker to the map.
                gMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_store)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
