package com.flourmillco.flourmill_1.UI.Fragments;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.flourmillco.flourmill_1.CallBacks.FirestoreCallBack;
import com.flourmillco.flourmill_1.CallBacks.FirestoreCallBackBakery;
import com.flourmillco.flourmill_1.CallBacks.FirestoreCallBackFlourMill;
import com.flourmillco.flourmill_1.Location.FlourMillsLocation;
import com.flourmillco.flourmill_1.Location.PolylineData;
import com.flourmillco.flourmill_1.Location.User;
import com.flourmillco.flourmill_1.Location.UserLocation;
import com.flourmillco.flourmill_1.Location.UserOrder;
import com.flourmillco.flourmill_1.MapClusterModel.ClusterMarker;
import com.flourmillco.flourmill_1.MapClusterModel.ClusterMarkerFlourMill;
import com.flourmillco.flourmill_1.MapClusterModel.MyClusterManagerRenderer;
import com.flourmillco.flourmill_1.MapClusterModel.MyClusterManagerRendererFlourMill;
import com.flourmillco.flourmill_1.R;
import com.flourmillco.flourmill_1.RecycleViews.RecycleViewAdapterTruckDrivers;
import com.flourmillco.flourmill_1.util.ViewWeightAnimationWrapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;
import static com.flourmillco.flourmill_1.UI.Constants.MAPVIEW_BUNDLE_KEY;


public class HomeFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnInfoWindowLongClickListener, GoogleMap.OnPolylineClickListener {
    private static final String TAG = "HomeFragment";
    private static final int LOCATION_UPDATE_INTERVAL = 3000;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int MAP_LAYOUT_STATE_CONTRACTED = 0;
    private static final int MAP_LAYOUT_STATE_EXPANDED = 1;
    List<UserLocation> temp2 = new ArrayList<>();
    UserLocation userLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private GeofencingClient mGeofencingClient;
    private ArrayList<Geofence> mGeofenceList;
    private ArrayList<UserOrder> mUserLocations = new ArrayList<>();
    private ArrayList<UserLocation> LocationsList = new ArrayList<>();
    private ArrayList<UserLocation> LocationsListBakery = new ArrayList<>();
    private ArrayList<FlourMillsLocation> FlourMills = new ArrayList<>();
    private ArrayList<ClusterMarker> markerClusters = new ArrayList<>();
    private ArrayList<ClusterMarker> markersBakerys = new ArrayList<>();
    private ArrayList<ClusterMarkerFlourMill> markerClustersFlourMills = new ArrayList<>();
    private Handler handler = new Handler();
    private int MapLayoutState = 0;
    private MapView mMapView;
    private RecyclerView recyclerView;
    private RecycleViewAdapterTruckDrivers adapterTruckDrivers;
    private LinearLayoutManager layoutManager;
    private FirebaseFirestore mDB;
    private FirebaseFirestore mDB2;
    private SharedPreferences pref;
    private GoogleMap googleMap;
    private LatLngBounds Boundery;
    private UserLocation userPosition;
    private List<UserLocation> temp1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ClusterManager TruckDriverCluster;
    private ClusterManager FlourMillCluster;
    private ClusterManager BakeryCluster;
    private MyClusterManagerRenderer clusterManagerRenderer;
    private MyClusterManagerRenderer BakeryRenderer;
    private MyClusterManagerRendererFlourMill RendererCluster;
    private Runnable runnable;
    private RelativeLayout mapContianer;
    private GeoApiContext geoApiContext = null;
    private ArrayList<PolylineData> mPolyLinesData = new ArrayList<>();
    private Marker SelectedMaker = null;


    private ArrayList<Marker> TripMakers = new ArrayList<>();
    private SharedPreferences pref3;

    public HomeFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.recyclerview190);
        pref = getActivity().getSharedPreferences("secretcode", MODE_PRIVATE);
        temp1 = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences("location", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mGeofenceList = new ArrayList<>();
        mapContianer = getActivity().findViewById(R.id.map_container);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        pref3 = getContext().getSharedPreferences("secretcode", MODE_PRIVATE);
        mDB = FirebaseFirestore.getInstance();
        mDB2 = FirebaseFirestore.getInstance();


        try {
            CollectionReference c2 = mDB.collection("full_order");
            c2.whereEqualTo(FieldPath.of("order", "bakeryID"), Integer.parseInt(pref3.getString("nameid", "")))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    UserOrder userLocation = documentSnapshot.toObject((UserOrder.class));
                                    if (userLocation.getOrder().getOrderStatues() != 3) {
                                        mUserLocations.add(userLocation);
                                     //   Toasty.success(getContext(), mUserLocations.get(0).getOrder().getDestination());
                                        //   Log.d("size2",mUserLocations.size()+" " );
                                        Log.d("newman", documentSnapshot.getId() + "  " + documentSnapshot.getData());
                                    }

                                }

                                adapterTruckDrivers = new RecycleViewAdapterTruckDrivers(getContext(), mUserLocations);

                                recyclerView.setAdapter(adapterTruckDrivers);
                            } else {
                                Log.d("newman", "Error  ", task.getException());
                            }
                        }
                    });
        } catch (Exception e) {
            Log.d(TAG, "error");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v3 = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = v3.findViewById(R.id.recyclerview190);
        v3.findViewById(R.id.fullscreen).setOnClickListener(this);
        v3.findViewById(R.id.resetmap).setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mMapView = v3.findViewById(R.id.mymap);
        mapContianer = v3.findViewById(R.id.map_container);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterTruckDrivers);
        getUserDetails();
        initGoogleMap(savedInstanceState);

        RecycleClick.addTo(recyclerView).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                Log.e("clicklistt", "clicked");

                String selectedID = mUserLocations.get(position).getUser().getUser_id();
                Log.e("clicklistt", selectedID);
                for (ClusterMarker clusterMarker : markerClusters) {
                    if (selectedID.equals(clusterMarker.getUser().getUser_id())) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(clusterMarker.getPosition().latitude, clusterMarker.getPosition().longitude)), 600, null);
                        Log.e("clicklistt", "clicked inside ");

                        break;
                    }
                }


            }
        });


        return v3;

    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

        if (geoApiContext == null) {
            geoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_map_api_key)).build();

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

        startUserLocationsRunnable();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMyLocationEnabled(true);
        googleMap = map;
        //Log.d("cameraview", "called  " + FirebaseAuth.getInstance().getUid());


        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {

                addMapMakerers();
                addMapMakerersFlourMill();
                addMapMakerersBakerys();

            }
        }, 1500);

        googleMap.setOnInfoWindowLongClickListener(this);
        googleMap.setOnPolylineClickListener(this);
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        stopLocationUpdates();
        super.onPause();

    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void removeTripMakers() {
        for (Marker marker : TripMakers) {
            marker.remove();
        }
    }

    private void resetSelectedMarker() {
        if (SelectedMaker != null) {
            SelectedMaker.setVisible(true);
            SelectedMaker = null;
            removeTripMakers();
        }
    }

    private void stopLocationUpdates() {
        handler.removeCallbacks(runnable);
    }


    private void readData(FirestoreCallBack firestoreCallBack) {
        try {


            CollectionReference c = mDB.collection("user_locations");
            c.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot doc : task.getResult()) {
                        UserLocation updaed = doc.toObject(UserLocation.class);
                        LocationsList.add(updaed);
                    }

                    firestoreCallBack.onCallBack(LocationsList);
                    //   firestoreCallBack
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "error");
        }

    }

    void readDataFlourMills(FirestoreCallBackFlourMill firestoreCallBackFlourMill) {
        try {
            CollectionReference c = mDB.collection("flourmill_location");
            c.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot doc : task.getResult()) {
                        FlourMillsLocation updaed = doc.toObject(FlourMillsLocation.class);
                        Log.d("clusterfsf", updaed.getLocation() + "");
                        Log.d("clusterfsf", updaed.getTimestamp() + "");
                        Log.d("clusterfsf", updaed.getUser().getEmail() + "");
                        Log.d("clusterfsf", updaed.getUser().getUsername() + "");
                        FlourMills.add(updaed);


                    }


                    firestoreCallBackFlourMill.onCallBack2(FlourMills);
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }


    private void readDataBakerys(FirestoreCallBackBakery firestoreCallBack2) {
        try {
            CollectionReference c = mDB.collection("Bakerys_Location");
            c.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot doc : task.getResult()) {
                        UserLocation updaed = doc.toObject(UserLocation.class);
                        LocationsListBakery.add(updaed);
                        Log.d("checkbakery", updaed.getGeo_point().getLatitude() + " ");

                        if (updaed.getUser() != null) {
                            Log.d("checkbakery", updaed.getUser().getUser_id() + " ");
                        }
                    }

                    firestoreCallBack2.onCallBack(LocationsListBakery);
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }


    private void addMapMakerersBakerys() {
        try {


            if (googleMap != null) {
                if (BakeryCluster == null) {
                    BakeryCluster = new ClusterManager<ClusterMarker>(getActivity().getApplicationContext(), googleMap);
                }
                if (BakeryRenderer == null) {
                    BakeryRenderer = new MyClusterManagerRenderer(
                            getActivity(),
                            googleMap,
                            BakeryCluster
                    );
                }
                BakeryCluster.setRenderer(BakeryRenderer);
            }

            readDataBakerys(new FirestoreCallBackBakery() {
                @Override
                public void onCallBack(List<UserLocation> userLocations) {
                    Log.d("checkbakery", userLocations.size() + " ");
                    for (UserLocation u : userLocations) {
                        String snippet = "";
                        //  Log.d("cluster", u.getUser().getUser_id());

                        try {


                            snippet = "For more information " + u.getUser().getUsername() + "?";

                        } catch (NullPointerException e) {
                            Log.d("addmapmarker:", "nullnew " + e.getMessage());
                        }
                        try {
                            int avatar = R.drawable.bakeryi;
                            ClusterMarker newClusterMarker = new ClusterMarker(
                                    new LatLng(u.getGeo_point().getLatitude(), u.getGeo_point().getLongitude()), u.getUser().getUsername(), snippet, avatar, u.getUser());
                            BakeryCluster.addItem(newClusterMarker);
                            markerClusters.add(newClusterMarker);
                        } catch (NullPointerException e) {
                            Log.d("addmapmarker:", "null pointer " + e.getMessage());

                        }
                    }
                    BakeryCluster.cluster();


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        public void run() {

                            createCap();

                        }
                    }, 2000);


                }
            });
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }

    private void addMapMakerers() {

        try {
            if (googleMap != null) {

                resetMap();

                if (TruckDriverCluster == null) {
                    TruckDriverCluster = new ClusterManager<ClusterMarker>(getActivity().getApplicationContext(), googleMap);
                }
                if (clusterManagerRenderer == null) {
                    clusterManagerRenderer = new MyClusterManagerRenderer(
                            getActivity(),
                            googleMap,
                            TruckDriverCluster
                    );
                }
                TruckDriverCluster.setRenderer(clusterManagerRenderer);
            }

            readData(new FirestoreCallBack() {
                @Override
                public void onCallBack(List<UserLocation> userLocations) {
                    Log.d("clustersize", userLocations.size() + " ");
                    for (UserLocation u : userLocations) {
                        String snippet = "";
                        //  Log.d("cluster", u.getUser().getUser_id());

                        try {


                            snippet = "Determine route to " + u.getUser().getUsername() + "?";

                        } catch (NullPointerException e) {
                            Log.d("addmapmarker:", "nullnew " + e.getMessage());
                        }
                        try {
                            int avatar = R.drawable.truckic;
                            ClusterMarker newClusterMarker = new ClusterMarker(
                                    new LatLng(u.getGeo_point().getLatitude(), u.getGeo_point().getLongitude()), u.getUser().getUsername(), snippet, avatar, u.getUser());
                            TruckDriverCluster.addItem(newClusterMarker);
                            markerClusters.add(newClusterMarker);
                        } catch (NullPointerException e) {
                            Log.d("addmapmarker:", "null pointer " + e.getMessage());

                        }
                    }
                    TruckDriverCluster.cluster();

                }
            });
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }


    private void addMapMakerersFlourMill() {
        try {
            if (googleMap != null) {
                if (FlourMillCluster == null) {
                    FlourMillCluster = new ClusterManager<ClusterMarker>(getActivity().getApplicationContext(), googleMap);
                }

                if (RendererCluster == null) {
                    RendererCluster = new MyClusterManagerRendererFlourMill(
                            getActivity(),
                            googleMap,
                            FlourMillCluster
                    );
                }
                FlourMillCluster.setRenderer(RendererCluster);
            }

            readDataFlourMills(new FirestoreCallBackFlourMill() {
                @Override
                public void onCallBack2(List<FlourMillsLocation> userLocations) {
                    Log.d("newclustersize", FlourMills.size() + " ");
                    for (FlourMillsLocation u2 : userLocations) {
                        String snippet = "";

                        Log.d("idnsds", u2.getUser().getId() + " ");
                        try {


                            snippet = "Flour Mill " + u2.getUser().getUsername() + "";

                        } catch (NullPointerException e) {
                            Log.d("newaddmapmarker:", "nullnew " + e.getMessage());
                        }
                        try {
                            int avatar = R.drawable.wiconw;
                            ClusterMarkerFlourMill newClusterMarker = new ClusterMarkerFlourMill(
                                    new LatLng(u2.getLocation().getLatitude(), u2.getLocation().getLongitude()), u2.getUser().getUsername(), snippet, avatar, u2.getUser());
                            FlourMillCluster.addItem(newClusterMarker);
                            markerClustersFlourMills.add(newClusterMarker);
                        } catch (NullPointerException e) {
                            Log.d("newaddmapmarker:", "null pointer " + e.getMessage());

                        }
                    }
                    FlourMillCluster.cluster();

                }
            });
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }


    private void createCap() {

        try {
            String uid = FirebaseAuth.getInstance().getUid();
            readDataBakerys(new FirestoreCallBackBakery() {
                @Override
                public void onCallBack(List<UserLocation> userLocations) {
                    Log.d("checkbakery", userLocations.size() + " ");


                    for (UserLocation u : userLocations) {
                        if (u.getUser() != null) {
                            if (u.getUser().getUser_id().trim().equals(uid.trim())) {

                                Log.d("checkbakery", u.getUser().getUser_id() + "/ " + uid);
                                userPosition = u;
                            }
                        }
                    }
                    if (userPosition != null) {
                        double bottomBoundary = userPosition.getGeo_point().getLatitude() - .1;
                        double leftBoundary = userPosition.getGeo_point().getLongitude() - .1;
                        double topBoundary = userPosition.getGeo_point().getLatitude() + .1;
                        double rightBoundary = userPosition.getGeo_point().getLongitude() + .1;

                        Boundery = new LatLngBounds(
                                new LatLng(bottomBoundary, leftBoundary),
                                new LatLng(topBoundary, rightBoundary)
                        );

                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(Boundery, 0));
                    }
                }

            });
        } catch (Exception e) {
            Log.d(TAG, "error");
        }

    }


    private void startUserLocationsRunnable() {
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                retrieveUserLocations2();
                retrieveUserLocations4();
                retrieveUserLocations5();
                handler.postDelayed(runnable, LOCATION_UPDATE_INTERVAL);
                Log.d("runnable", "called");
            }

        }, LOCATION_UPDATE_INTERVAL);
    }


    private void retrieveUserLocations2() {
        try {

            try {
                for (ClusterMarker clusterMarker : markerClusters) {

                    DocumentReference userLocationRef = FirebaseFirestore.getInstance()
                            .collection("user_locations")
                            .document(clusterMarker.getUser().getUser_id());
                    Log.e("uil", "retrieveUserLocations: " + clusterMarker.getUser().getUser_id());

                    userLocationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                UserLocation updatedUserLocation = task.getResult().toObject(UserLocation.class);
                                //    Log.e("userid", "retrieveUserLocations: " + updatedUserLocation.getUser().getUser_id());

                                for (int i = 0; i < markerClusters.size(); i++) {
                                    try {
                                        if (markerClusters.get(i).getUser().getUser_id().equals(updatedUserLocation.getUser().getUser_id())) {
                                            LatLng updatedLatLng = new LatLng(
                                                    updatedUserLocation.getGeo_point().getLatitude(),
                                                    updatedUserLocation.getGeo_point().getLongitude()
                                            );
                                            markerClusters.get(i).setPosition(updatedLatLng);
                                            clusterManagerRenderer.setUpdateMarker(markerClusters.get(i));
                                        }
                                    } catch (NullPointerException e) {
                                        Log.e("HomeFragmnet", "retrieveUserLocations: NullPointerException: " + e.getMessage());
                                    }
                                }
                            }
                        }
                    });
                }

            } catch (IllegalStateException e) {

            }

        } catch (Exception e) {
            Log.d(TAG, "error");
        }

    }

    private void retrieveUserLocations4() {

        try {
            Log.e("calleddd", "retrieveUserLocations: ");


            for (final ClusterMarkerFlourMill clusterMarker : markerClustersFlourMills) {


                DocumentReference userLocationRef = FirebaseFirestore.getInstance()
                        .collection("flourmill_location")
                        .document(String.valueOf(clusterMarker.getUser().getId()));
                Log.e("newuil", "retrieveUserLocations: " + clusterMarker.getUser().getId());

                userLocationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            try {

                                FlourMillsLocation updatedUserLocation = task.getResult().toObject(FlourMillsLocation.class);
                                Log.e("userid", "retrieveUserLocations: " + updatedUserLocation.getUser().getId());

                                for (int i = 0; i < markerClustersFlourMills.size(); i++) {
                                    try {
                                        if (String.valueOf(markerClustersFlourMills.get(i).getUser().getId()).equals(String.valueOf(updatedUserLocation.getUser().getId()))) {
                                            LatLng updatedLatLng = new LatLng(
                                                    updatedUserLocation.getLocation().getLatitude(),
                                                    updatedUserLocation.getLocation().getLongitude()
                                            );
                                            markerClustersFlourMills.get(i).setPosition(updatedLatLng);
                                            RendererCluster.setUpdateMarker(markerClustersFlourMills.get(i));
                                        }
                                    } catch (NullPointerException e) {
                                        Log.e("newHomeFragmnet", "retrieveUserLocations: NullPointerException: " + e.getMessage());
                                    }
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                });

            }

        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }


    private void retrieveUserLocations5() {
        try {
            try {
                for (final ClusterMarker clusterMarker : markersBakerys) {

                    DocumentReference userLocationRef = FirebaseFirestore.getInstance()
                            .collection("Bakerys_Location")
                            .document(clusterMarker.getUser().getUser_id());
                    Log.e("checkbakery", "retrieveUserLocations: " + clusterMarker.getUser().getUser_id());

                    userLocationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                UserLocation updatedUserLocation = task.getResult().toObject(UserLocation.class);
                                Log.e("checkbakery", "retrieveUserLocations: " + updatedUserLocation.getUser().getUser_id());

                                for (int i = 0; i < markersBakerys.size(); i++) {
                                    try {
                                        if (markersBakerys.get(i).getUser().getUser_id().equals(updatedUserLocation.getUser().getUser_id())) {
                                            LatLng updatedLatLng = new LatLng(
                                                    updatedUserLocation.getGeo_point().getLatitude(),
                                                    updatedUserLocation.getGeo_point().getLongitude()
                                            );
                                            markersBakerys.get(i).setPosition(updatedLatLng);
                                            BakeryRenderer.setUpdateMarker(markersBakerys.get(i));
                                        }
                                    } catch (NullPointerException e) {
                                        Log.e("HomeFragmnet", "retrieveUserLocations: NullPointerException: " + e.getMessage());
                                    }
                                }
                            }
                        }
                    });
                }
            } catch (IllegalStateException ignored) {
            }
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }


    private void expandMapAnimation() {

        try {
            ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(mapContianer);
            ObjectAnimator mapAnimantion = ObjectAnimator.ofFloat(mapAnimationWrapper, "weight", 50, 100);
            mapAnimantion.setDuration(800);

            ViewWeightAnimationWrapper recycleViewAnimationWrraper = new ViewWeightAnimationWrapper(recyclerView);
            ObjectAnimator recycleViewAnimation = ObjectAnimator.ofFloat(recycleViewAnimationWrraper, "weight", 50, 0);
            recycleViewAnimation.setDuration(800);

            recycleViewAnimation.start();
            mapAnimantion.start();
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }

    private void reverseMapAnimation() {
        try {
            ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(mapContianer);
            ObjectAnimator mapAnimantion = ObjectAnimator.ofFloat(mapAnimationWrapper, "weight", 100, 50);
            mapAnimantion.setDuration(800);

            ViewWeightAnimationWrapper recycleViewAnimationWrraper = new ViewWeightAnimationWrapper(recyclerView);
            ObjectAnimator recycleViewAnimation = ObjectAnimator.ofFloat(recycleViewAnimationWrraper, "weight", 0, 50);
            recycleViewAnimation.setDuration(800);

            recycleViewAnimation.start();
            mapAnimantion.start();
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }

    private void resetMap() {
        try {


            if (googleMap != null) {

                googleMap.clear();
                if (TruckDriverCluster != null) {
                    TruckDriverCluster.clearItems();
                }
                if (FlourMillCluster != null) {
                    FlourMillCluster.clearItems();
                }
                if (BakeryCluster != null) {
                    BakeryCluster.clearItems();
                }
                if (markerClusters.size() > 0) {
                    markerClusters.clear();
                    markerClusters = new ArrayList<>();
                }
                if (markersBakerys.size() > 0) {
                    markersBakerys.clear();
                    markersBakerys = new ArrayList<>();
                }
                if (markerClustersFlourMills.size() > 0) {
                    markerClustersFlourMills.clear();
                    markerClustersFlourMills = new ArrayList<>();
                }
                if (mPolyLinesData.size() > 0) {
                    mPolyLinesData.clear();
                    mPolyLinesData = new ArrayList<>();
                }

            }
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fullscreen: {

                if (MapLayoutState == MAP_LAYOUT_STATE_CONTRACTED) {
                    MapLayoutState = MAP_LAYOUT_STATE_EXPANDED;
                    expandMapAnimation();
                } else if (MapLayoutState == MAP_LAYOUT_STATE_EXPANDED) {
                    MapLayoutState = MAP_LAYOUT_STATE_CONTRACTED;
                    reverseMapAnimation();
                }
                break;
            }
            case R.id.resetmap: {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    public void run() {

                        addMapMakerers();
                        addMapMakerersFlourMill();
                        addMapMakerersBakerys();

                    }
                }, 1500);


                break;
            }
        }
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {

        if (marker.getTitle().contains("Delivery #")) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Open Google Maps ?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            String latitude = String.valueOf(marker.getPosition().latitude);
                            String longitude = String.valueOf(marker.getPosition().longitude);
                            Uri googlemapURL = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, googlemapURL);
                            mapIntent.setPackage("com.google.android.apps.maps");

                            try {
                                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                    startActivity(mapIntent);
                                }
                            } catch (NullPointerException e) {
                                Log.e(TAG, "error" + e.getMessage());
                                Toast.makeText(getActivity(), "Couldn't open map", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();


        } else {


            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Do you want to determine the route ?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            resetSelectedMarker();
                            SelectedMaker = marker;
                            CalculateDirections(marker);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

    }

    private void CalculateDirections(Marker marker) {
        try {
            Log.d(TAG, "Calculatedireaction");
            com.google.maps.model.LatLng mydestination = new com.google.maps.model.LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
            DirectionsApiRequest directionsApiRequest = new DirectionsApiRequest(geoApiContext);
            directionsApiRequest.alternatives(true);
            directionsApiRequest.origin(
                    new com.google.maps.model.LatLng(
                            userPosition.getGeo_point().getLatitude(),
                            userPosition.getGeo_point().getLongitude()
                    )
            );

            Log.d(TAG, "Calcualtedirection destination" + mydestination.toString());
            directionsApiRequest.destination(mydestination).setCallback(new PendingResult.Callback<DirectionsResult>() {
                @Override
                public void onResult(DirectionsResult result) {

                    Log.d("mki", "calculateDirections: route " + result.routes[0].toString());
                    Log.d("mki", "calculateDirections: duration " + result.routes[0].legs[0].duration);
                    Log.d("mki", "calculateDirections: distance " + result.routes[0].legs[0].distance);
                    Log.d("mki", "calculateDirections: geocodedwaypoints " + result.geocodedWaypoints.toString());
/*
                Log.d("mki", "2: route " + result.routes[1].toString());
                Log.d("mki", "2: duration " + result.routes[1].legs[1].duration);
                Log.d("mki", "2: distance " + result.routes[1].legs[1].distance);
                Log.d("mki", "2: geocodedwaypoints " + result.geocodedWaypoints.toString());


                Log.d("mki", "3: route " + result.routes[0].toString());
                Log.d("mki", "3: duration " + result.routes[0].legs[1].duration);
                Log.d("mki", "3: distance " + result.routes[0].legs[1].distance);
                Log.d("mki", "3: geocodedwaypoints " + result.geocodedWaypoints.toString());
                */

                    AddPolyLinesToMap(result);

                }

                @Override
                public void onFailure(Throwable e) {
                    Log.d(TAG, "calculateDirections: failyer " + e.getMessage());


                }
            });
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }


    private void getUserDetails() {
        try {
            if (userLocation == null) {
                userLocation = new UserLocation();
                DocumentReference userRef = mDB.collection("Users")
                        .document(FirebaseAuth.getInstance().getUid());
                Log.d("useriol", FirebaseAuth.getInstance().getUid());
                //     Log.d("useriol",userLocation.getUser().getUser_id());


                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: successfully set the user client.");
                            User user = task.getResult().toObject(User.class);
                            userLocation.setUser(user);

                            getLastKnownLocation();
                        }
                    }
                });
            } else {
                getLastKnownLocation();
            }
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }

    private void getLastKnownLocation() {
        try {
            Log.d(TAG, "getLastKnownLocation: called.");

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
                @Override
                public void onComplete(@NonNull Task<android.location.Location> task) {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        Log.d(TAG, "onComplete: latitude: " + geoPoint.getLatitude());
                        Log.d(TAG, "onComplete: longitude: " + geoPoint.getLongitude());
                        userLocation.setGeo_point(geoPoint);
                        userLocation.setTimestamp(null);
                        saveUserLocation();
                    }
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }

    private void saveUserLocation() {
        try {
            if (userLocation != null) {
                DocumentReference locationRef = mDB.collection("Bakerys_Location").document(FirebaseAuth.getInstance().getUid());
                Log.d("userloca", userLocation.getUser().getUser_id());
                locationRef.set(userLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "latitude: " + userLocation.getGeo_point().getLatitude() +
                                    " longitude: " + userLocation.getGeo_point().getLongitude());
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }

    void AddPolyLinesToMap(final DirectionsResult result) {
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Log.d("poly", "runing " + result.routes.length);
                    if (mPolyLinesData.size() > 0) {
                        for (PolylineData polylineData : mPolyLinesData) {
                            polylineData.getPolyline().remove();
                        }
                        mPolyLinesData.clear();
                        mPolyLinesData = new ArrayList<>();

                    }
                    double duration = 99999999;
                    for (DirectionsRoute route : result.routes) {
                        List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
                        List<LatLng> newDecodedPath = new ArrayList<>();
                        for (com.google.maps.model.LatLng latLng : decodedPath) {
                            newDecodedPath.add(new LatLng(
                                    latLng.lat,
                                    latLng.lng
                            ));
                        }
                        Polyline polyline = googleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                        polyline.setColor(ContextCompat.getColor(getActivity(), R.color.darkGrey));
                        polyline.setClickable(true);


                        mPolyLinesData.add(new PolylineData(polyline, route.legs[0]));


                        double tempDuration = route.legs[0].duration.inSeconds;
                        if (tempDuration < duration) {
                            duration = tempDuration;
                            onPolylineClick(polyline);
                            zoomRoute(polyline.getPoints());
                        }
                        SelectedMaker.setVisible(false);
                    }
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        try {


            for (PolylineData polylineData : mPolyLinesData) {
                int index = 0;
                Log.d("polykj", polylineData.toString());
                if (polyline.getId().equals(polylineData.getPolyline().getId())) {
                    index++;
                    polylineData.getPolyline().setColor(ContextCompat.getColor(getActivity(), R.color.blue1));
                    polylineData.getPolyline().setZIndex(1);

                    LatLng endLocaction = new LatLng(polylineData.getLeg().endLocation.lat, polylineData.getLeg().endLocation.lng);
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(endLocaction).title("Delivery #" + index).snippet("Duration: " + polylineData.getLeg().duration + " Distance: " + polylineData.getLeg().distance));
                    marker.showInfoWindow();
                    TripMakers.add(marker);

                } else {
                    polylineData.getPolyline().setColor(ContextCompat.getColor(getActivity(), R.color.darkGrey));
                    polylineData.getPolyline().setZIndex(0);
                }
            }

        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }

    public void zoomRoute(List<LatLng> latLngList) {
        try {


            if (googleMap == null || latLngList == null || latLngList.isEmpty()) {
                return;
            }
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (LatLng latLngPoint : latLngList) {
                boundsBuilder.include(latLngPoint);
            }
            int routePadding = 120;
            LatLngBounds latLngBounds = boundsBuilder.build();
            googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                    600,
                    null
            );
        } catch (Exception e) {
            Log.d(TAG, "error");
        }
    }


}