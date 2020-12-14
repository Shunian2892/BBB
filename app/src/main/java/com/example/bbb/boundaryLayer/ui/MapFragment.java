package com.example.bbb.boundaryLayer.ui;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bbb.R;
import com.example.bbb.controlLayer.gps.OpenStreetMaps;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapFragment extends Fragment {

    private static final String PREFS_NAME = "MapFragment";
    private static final String PREFS_TILE_SOURCE = "tilesource";
    private static final String PREFS_LATITUDE_STRING = "latitudeString";
    private static final String PREFS_LONGITUDE_STRING = "longitudeString";
    private static final String PREFS_ORIENTATION = "orientation";
    private static final String PREFS_ZOOM_LEVEL_DOUBLE = "zoomLevelDouble";

    private SharedPreferences mPrefs;
    private Context fragmentContext;
    private IMapController mapController;
    private MapView map;
    private OpenStreetMaps osm;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentContext = getActivity().getApplicationContext();
        Configuration.getInstance().load(fragmentContext, PreferenceManager.getDefaultSharedPreferences(fragmentContext));

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        map = view.findViewById(R.id.map_view);

        map.setUseDataConnection(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setDestroyMode(false);
        mapController = map.getController();
        mapController.setZoom(20);

        //Check for Location permission
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        }

        //Zoom in with pinching
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(true);

        osm = new OpenStreetMaps();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //trying to load in last location with sharedPrefs.
/*        final Context context = this.getActivity();

        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        final float zoomLevel = mPrefs.getFloat(PREFS_ZOOM_LEVEL_DOUBLE, 1);
        map.getController().setZoom(zoomLevel);
        final float orientation = mPrefs.getFloat(PREFS_ORIENTATION, 0);
        map.setMapOrientation(orientation, false);
        final String latitudeString = mPrefs.getString(PREFS_LATITUDE_STRING, "1.0");
        final String longitudeString = mPrefs.getString(PREFS_LONGITUDE_STRING, "1.0");
        final double latitude = Double.valueOf(latitudeString);
        final double longitude = Double.valueOf(longitudeString);

        GeoPoint startPosition = new GeoPoint(latitude, longitude);
        map.setExpectedCenter(startPosition);
        getLocation(startPosition);*/
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        getLocation(null);
        getLocation(new GeoPoint(51.9225, 4.47917));

        osm.drawRoute(map);
    }


    public void getLocation(GeoPoint geoPoint) {

        LocationManager locationManager = (LocationManager) fragmentContext.getSystemService(Context.LOCATION_SERVICE);

        //Not needed. Only if use savedPrefs
        if (geoPoint != null) {
            mapController.setCenter(geoPoint);
            Marker startMarker = new Marker(map);
            startMarker.setPosition(geoPoint);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            startMarker.setVisible(true);
            map.getOverlays().add(startMarker);
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("Latitude", "onLocationChanged: " + location.getLatitude());
                GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());
                mapController.setCenter(point);
                Marker startPoint = new Marker(map);
                startPoint.setPosition(point);
                map.getOverlays().add(startPoint);
            }
        };

        if (ContextCompat.checkSelfPermission(fragmentContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }

    @Override
    public void onPause() {
        //save the current location with sharedPrefs
/*        final SharedPreferences.Editor edit = mPrefs.edit();
        edit.putString(PREFS_TILE_SOURCE, map.getTileProvider().getTileSource().name());
        edit.putFloat(PREFS_ORIENTATION, map.getMapOrientation());
        edit.putString(PREFS_LATITUDE_STRING, String.valueOf(map.getMapCenter().getLatitude()));
        edit.putString(PREFS_LONGITUDE_STRING, String.valueOf(map.getMapCenter().getLongitude()));
        edit.putFloat(PREFS_ZOOM_LEVEL_DOUBLE, (float) map.getZoomLevelDouble());
        edit.apply();*/

        map.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(fragmentContext, PreferenceManager.getDefaultSharedPreferences(fragmentContext));
    }

    public void invalidateMapView() {
        if (map != null) {
            map.invalidate();
        }
    }


}
