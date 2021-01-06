package com.example.bbb.boundaryLayer.ui;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.bbb.R;
import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.controlLayer.geofencing.GeoFenceSetup;
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
    private GeoFenceSetup setupGF;
    private SharedPreferences prefs;
    private String currentLang;

    private UIViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentContext = getActivity().getApplicationContext();
        Configuration.getInstance().load(fragmentContext, PreferenceManager.getDefaultSharedPreferences(fragmentContext));

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        prefs = getActivity().getSharedPreferences("language", Context.MODE_PRIVATE);
        currentLang = prefs.getString("language", Locale.getDefault().getLanguage());

        dm = DatabaseManager.getInstance();
        map = view.findViewById(R.id.map_view);
        map.setUseDataConnection(true);
        map.setTileSource(TileSourceFactory.MAPNIK);

        mapController = map.getController();
        mapController.setZoom(14);

        setupGF = new GeoFenceSetup(getContext(), getActivity());

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

        viewModel = new ViewModelProvider(getActivity()).get(UIViewModel.class);
        viewModel.setIMapChanged(MapFragment.this);

        openRouteService = new OpenRouteService(map, fragmentContext, view, viewModel, getParentFragmentManager());

        ibRouteInfo = view.findViewById(R.id.imageButtonRouteInfo);
        ibHelpPopup = view.findViewById(R.id.imageButtonHelp);
        ibUserInfo = view.findViewById(R.id.imageButtonUserInfo);
        routeSpinner = view.findViewById(R.id.spinner_route);
        ibCenterPosition = view.findViewById(R.id.centerPosition);

        viewModel = new ViewModelProvider(getActivity()).get(UIViewModel.class);
        viewModel.setIMapChanged(MapFragment.this);

        buttonClickListeners();

        routeNameList = new ArrayList<>();
        routeNameList.add(getResources().getString(R.string.select_a_route));
        for (Route route : dm.getRoutes()) {
            switch (currentLang) {
                case "en":
                    routeNameList.add(route.RouteName_en);
                    break;
                case "fr":
                    routeNameList.add(route.RouteName_fr);
                    break;
                case "nl":
                    routeNameList.add(route.RouteName_nl);
                    break;
            }
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
                viewModel.setSelectedRoute(position);
                List<POI> pois = dm.getPOIsFromRoute(position);

                setupGF.setupGeoFencing(pois);

                if (position != 0) {
                    switch (currentLang) {
                        case "en":
                            createRoute(position, dm.getRoute(position).RouteName_en);
                            break;
                        case "fr":
                            createRoute(position, dm.getRoute(position).RouteName_fr);
                            break;
                        case "nl":
                            createRoute(position, dm.getRoute(position).RouteName_nl);
                            break;
                    }
                } else if (viewModel.getVisiblePOI().getValue() == null) {
                    onMapChange();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        routeSpinner.setSelection(viewModel.getSelectedRoute().getValue());

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createRoute(int position, String routeNameLang) {
        if (position != 0) {

            for (Route route : dm.getRoutes()) {
                if (routeNameLang.equals(route.RouteName_en) || routeNameLang.equals(route.RouteName_fr) || routeNameLang.equals(route.RouteName_nl)) {
                    List<POI> pois = dm.getPOIsFromRoute(route.ID);
                    double[][] coordinates = new double[pois.size()][2];

                    for (int i = 0; i < pois.size(); i++) {
                        coordinates[i][0] = pois.get(i).latitude;
                        coordinates[i][1] = pois.get(i).longitude;
                    }

                    Toast.makeText(fragmentContext, getResources().getString(R.string.loading_route), Toast.LENGTH_SHORT).show();
                    openRouteService.getRoute(coordinates, "foot-walking", Locale.getDefault().getLanguage(), route);
                    break;
                }
            }
        } else {
            onMapChange();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentLocation = new Marker(map);
        getLocation();
        mapController.setCenter(currentLocation.getPosition());

        if (viewModel.getVisiblePOI().getValue() != null) {
            POI poi = viewModel.getVisiblePOI().getValue();
            GeoPoint poiLocation = new GeoPoint(poi.longitude, poi.latitude);
            Marker poiMarker = new Marker(map);
            poiMarker.setPosition(poiLocation);
            poiMarker.setTitle(poi.POIName);
            poiMarker.setIcon(fragmentContext.getDrawable(R.drawable.ic_baseline_not_listed_location_24)); // change icon
            mapController.setCenter(poiMarker.getPosition());
            mapController.setZoom(18.0);
            map.getOverlays().add(poiMarker);
            map.invalidate();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getLocation() {
        LocationManager locationManager = (LocationManager) fragmentContext.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = location -> {
            if (getView() == null) {
                return;
            }
            GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());

            Marker startPoint = new Marker(map);
            startPoint.setPosition(point);
            startPoint.setIcon(fragmentContext.getDrawable(R.drawable.my_location));

            map.getOverlays().remove(currentLocation);
            currentLocation = startPoint;
            map.getOverlays().add(startPoint);

            if (viewModel.getCenterOnUser().getValue()) {
                mapController.setCenter(currentLocation.getPosition());
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
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (routeSpinner.getSelectedItemPosition() != 0) {
                    RoutePopUp dialogFragment = new RoutePopUp();
                    viewModel.setRoutePopUpSelectedRoute(dm.getRoutes().get(routeSpinner.getSelectedItemPosition() - 1));
                    dialogFragment.show(getActivity().getSupportFragmentManager(), "route_popup");
                } else {
                    onMapChange();
                    Toast.makeText(fragmentContext, getResources().getString(R.string.please_select_a_route), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ibHelpPopup.setOnClickListener(view -> {
            DialogFragment dialogFragment = new HelpPopUp();
            dialogFragment.show(getActivity().getSupportFragmentManager(), "help_popup");
        });

        ibUserInfo.setOnClickListener(view -> {
            setUserInfoFragment(getActivity().getSupportFragmentManager());
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, userInfoFragment).addToBackStack(null).commit();
        });

        ibCenterPosition.setOnClickListener(v -> {
            viewModel.setCenterOnUser(true);
            mapController.setCenter(currentLocation.getPosition());
        });
    }

    public void setUserInfoFragment(FragmentManager fm) {
        if (fm.findFragmentById(R.id.user_info_fragment) == null) {
            userInfoFragment = new UserInfoFragment();
        } else {
            userInfoFragment = fm.findFragmentById(R.id.user_info_fragment);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMapChange() {
        map.getOverlays().clear();
        routeSpinner.setSelection(0);
        viewModel.setSelectedRoute(0);
        getLocation();
        map.invalidate();

        setupGF.removeGeoFences(dm.getPOIs());
    }

}
