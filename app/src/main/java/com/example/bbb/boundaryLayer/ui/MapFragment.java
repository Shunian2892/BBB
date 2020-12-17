package com.example.bbb.boundaryLayer.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bbb.R;
import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.controlLayer.gps.OpenRouteService;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.Route;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements IMapChanged {
    private Context fragmentContext;
    private IMapController mapController;
    private MapView map;
    private OpenRouteService openRouteService;
    private Marker currentLocation;
    private ImageButton ibRouteInfo;
    private ImageButton ibHelpPopup;
    private ImageButton ibUserInfo;
    private ImageButton ibCenterPosition;
    private Fragment userInfoFragment;
    private Spinner routeSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private List<String> routeNameList;
    private DatabaseManager dm;

    private boolean centerOnStart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentContext = getActivity().getApplicationContext();
        Configuration.getInstance().load(fragmentContext, PreferenceManager.getDefaultSharedPreferences(fragmentContext));

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        dm = DatabaseManager.getInstance(getContext());
        map = (MapView) view.findViewById(R.id.map_view);
        map.setUseDataConnection(true);
        map.setTileSource(TileSourceFactory.MAPNIK);

        mapController = map.getController();
        mapController.setZoom(14);

        centerOnStart = false;

        //Check for Location permission
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        }

        //Zoom in with pinching
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(true);

        openRouteService = new OpenRouteService(map, fragmentContext);

        ibRouteInfo = view.findViewById(R.id.imageButtonRouteInfo);
        ibHelpPopup = view.findViewById(R.id.imageButtonHelp);
        ibUserInfo = view.findViewById(R.id.imageButtonUserInfo);
        routeSpinner = view.findViewById(R.id.spinner_route);
        ibCenterPosition = view.findViewById(R.id.centerPosition);

        routeNameList = new ArrayList<>();
        routeNameList.add("Select a Route");
        for (Route route : dm.getRoutes()) {
            routeNameList.add(route.RouteName);
        }

        this.spinnerAdapter = new ArrayAdapter<String>(
                getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                routeNameList);

        routeSpinner.setAdapter(spinnerAdapter);
        routeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String routeName = (String) parent.getItemAtPosition(position);

                if (position != 0) {
                    for (Route route : dm.getRoutes()) {
                        if (route.RouteName.equals(routeName)) {
                            Route selectedRoute = route;
                            List<POI> pois = dm.getPOIsFromRoute(route.ID);
                            double[][] coordinates = new double[pois.size()][2];

                            for (int i = 0; i < pois.size(); i++) {
                                coordinates[i][0] = pois.get(i).latitude;
                                coordinates[i][1] = pois.get(i).longitude;

//                                System.out.println(pois.get(i).POIName);
                            }
                            Toast.makeText(fragmentContext, "Loading route...", Toast.LENGTH_SHORT).show();
                            openRouteService.getRoute(coordinates, "foot-walking", Locale.getDefault().getLanguage());
                            mapController.setCenter(new GeoPoint(coordinates[0][1],coordinates[0][0]));
                            break;
                        }
                    }
                } else{
                    onMapChange();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonClickListeners();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentLocation = new Marker(map);
        getLocation();

        mapController.setCenter(currentLocation.getPosition());

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getLocation() {
        LocationManager locationManager = (LocationManager) fragmentContext.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = location -> {
            if (getView() == null) {
                return;
            }
            Log.d("Latitude", "onLocationChanged: " + location.getLatitude());
            GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());

            Marker startPoint = new Marker(map);
            startPoint.setPosition(point);
            startPoint.setIcon(fragmentContext.getDrawable(R.drawable.my_location));
            map.getOverlays().remove(currentLocation);
            currentLocation = startPoint;
            map.getOverlays().add(startPoint);

            if(!centerOnStart){
                mapController.setCenter(currentLocation.getPosition());
                centerOnStart = true;
            }

            map.invalidate();
        };

        if (ContextCompat.checkSelfPermission(fragmentContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
        }


    }

    public void buttonClickListeners() {
        ibRouteInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (routeSpinner.getSelectedItemPosition() != 0) {
                    DialogFragment dialogFragment = new RoutePopUp(MapFragment.this, dm.getRoutes().get(routeSpinner.getSelectedItemPosition()-1));
                    dialogFragment.show(getActivity().getSupportFragmentManager(), "route_popup");
                } else {
                    Toast.makeText(fragmentContext, "Please select a route!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ibHelpPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new HelpPopUp();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "help_popup");
            }
        });

        ibUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserInfoFragment(getActivity().getSupportFragmentManager());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, userInfoFragment).commit();
            }
        });

        ibCenterPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapController.setCenter(currentLocation.getPosition());
            }
        });
    }

    public void setUserInfoFragment(FragmentManager fm) {
        if (fm.findFragmentById(R.id.user_info_fragment) == null) {
            userInfoFragment = new UserInfoFragment();
        } else {
            userInfoFragment = (UserInfoFragment) fm.findFragmentById(R.id.user_info_fragment);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMapChange() {
        map.getOverlays().clear();
        routeSpinner.setSelection(0);
        getLocation();
        map.invalidate();

    }
}
