package ke.co.ximmoz.fleet.views;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ke.co.ximmoz.fleet.models.Consignment;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.viewmodels.ConsignmentViewmodel;
import ke.co.ximmoz.fleet.views.Utils.ConsignmentStatus;
import ke.co.ximmoz.fleet.views.Utils.DatePickerFrag;

import static ke.co.ximmoz.fleet.views.FleetRequestsActivity.hasPermissions;

public class DestinationChooserActivity extends FragmentActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener, RoutingListener {

    private static final int AUTOCOMPLETE_LOCATION_DESTINATION = 20;
    private static final int AUTOCOMPLETE_LOCATION_PICKUP = 21;
    private static final String TAG = DestinationChooserActivity.class.getSimpleName();
    private GoogleMap mMap;
    private MarkerOptions markerOptions = new MarkerOptions();
    private Consignment consignment;
    private ConsignmentViewmodel consignmentViewmodel;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Button confirmConsignment;
    int PERMISSION_ALL = 1;
    Unbinder unbinder;
    private ArrayList<Polyline> polylines = new ArrayList<>();


    @BindView(R.id.main_pop_up)
    ConstraintLayout main_pop_up;
    @BindView(R.id.delivery_location)
    TextView delivery_location;
    @BindView(R.id.pick_location)
    TextView pick_location;
    @BindView(R.id.chooseCon)
    TextView chooseCon;
    @BindView(R.id.getMeATruck)
    Button getMeATruck;
    @BindView(R.id.containerDimensions)
    Spinner containerDimensions;


    LatLng destination_location = null;
    LatLng consignment_pickup_location = null;
    String container_size = null;

    private void finishConsignment(View v) {


        if (destination_location == null || consignment_pickup_location == null || container_size == null) {
            Toast.makeText(this, "All fields required to proceed", Toast.LENGTH_SHORT).show();
            return;
        }
//        consignment.setPickup_lat(consignment_pickup_location.latitude);
//        consignment.setPickup_lng(consignment_pickup_location.longitude);
//        consignment.setDestination_lat(destination_location.latitude);
//        consignment.setDestination_lng(destination_location.longitude);
        consignment.setContainer_size(container_size);
        SaveConsignment(consignment);
    }

    private void GetContainer() {
        consignment.setContainer_size(containerDimensions.getSelectedItem().toString());
        SaveConsignment(consignment);
    }

    private void GetPickupDate() {

        DialogFragment dialogFragment = new DatePickerFrag();
        dialogFragment.show(getSupportFragmentManager(), "date picker");


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_chooser);
        unbinder = ButterKnife.bind(this);
        ViewCompat.animate(main_pop_up)
                .scaleY(1)
                .setStartDelay(50)
                .setDuration(500).setInterpolator(
                new DecelerateInterpolator()).start();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        containerDimensions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                container_size = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_api_key));
        }
        ArrayAdapter<CharSequence> containerSize = ArrayAdapter.createFromResource(this, R.array.container_size, android.R.layout.simple_spinner_dropdown_item);
        containerDimensions.setAdapter(containerSize);
        confirmConsignment = findViewById(R.id.getMeATruck);
        String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        }


        consignmentViewmodel = ViewModelProviders.of(DestinationChooserActivity.this).get(ConsignmentViewmodel.class);
        consignment = new Consignment();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(DestinationChooserActivity.this,CargoOwnerActitivy.class));
//        finish();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_LOCATION_DESTINATION) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                delivery_location.setVisibility(View.GONE);


                chooseCon.setVisibility(View.VISIBLE);
                containerDimensions.setVisibility(View.VISIBLE);
                destination_location = place.getLatLng();
                if (destination_location != null && consignment_pickup_location != null) {
                    String api = getResources().getString(R.string.google_api_key);
                    Routing routing = new Routing.Builder()
                            .key(api)
                            .travelMode(AbstractRouting.TravelMode.DRIVING)
                            .waypoints(consignment_pickup_location, destination_location)
                            .withListener(this)
                            .alternativeRoutes(false)
                            .build();
                    routing.execute();
                }
                delivery_location.setVisibility(View.GONE);
                chooseCon.setVisibility(View.VISIBLE);
                confirmConsignment.setVisibility(View.VISIBLE);
                containerDimensions.setVisibility(View.VISIBLE);
                mMap.clear();
                markerOptions.position(destination_location)
                        .title("Destination")
                        .snippet("This will be the destination")
                        .draggable(true);
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination_location, 9));
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this, "Error " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
                Toast.makeText(this, "Destination Required", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == AUTOCOMPLETE_LOCATION_PICKUP) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                delivery_location.setVisibility(View.VISIBLE);
                pick_location.setVisibility(View.GONE);
                consignment_pickup_location = place.getLatLng();
                pick_location.setVisibility(View.GONE);
                delivery_location.setVisibility(View.VISIBLE);

                mMap.clear();

                markerOptions.position(consignment_pickup_location)
                        .title("Pickup")
                        .snippet("This will be the pickup")
                        .draggable(true);
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(consignment_pickup_location, mMap.getCameraPosition().zoom + 2.0f));
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this, "Error " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
                Toast.makeText(this, "Pickup Required", Toast.LENGTH_SHORT).show();
            }
        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {


        pick_location.setOnClickListener(v -> {

            List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
            Intent locationSearchAutoComplete = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(this);
            startActivityForResult(locationSearchAutoComplete, AUTOCOMPLETE_LOCATION_PICKUP);
        });
        delivery_location.setOnClickListener(v -> {

            List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
            Intent locationSearchAutoComplete = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(this);
            startActivityForResult(locationSearchAutoComplete, AUTOCOMPLETE_LOCATION_DESTINATION);
        });


        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mary_map));


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 14));
            }

        });

//        mMap.setOnMapClickListener(lattlng-> {
//                mMap.clear();
//                markerOptions.position(lattlng);
//                markerOptions.draggable(true);
//                destination_location=lattlng;
//                mMap.addMarker(markerOptions);
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lattlng,mMap.getCameraPosition().zoom+1f));
//        });



//        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//            @Override
//            public void onMarkerDragStart(Marker marker) {
//
//            }
//
//            @Override
//            public void onMarkerDrag(Marker marker) {
//
//            }
//
//            @Override
//            public void onMarkerDragEnd(Marker marker) {
//                if(consignment_pickup_location==null)
//                {
//                    consignment_pickup_location=marker.getPosition();
//                    Toast.makeText(DestinationChooserActivity.this, "Added to pickupt", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                destination_location=marker.getPosition();
//                Toast.makeText(DestinationChooserActivity.this, "Added to delivery", Toast.LENGTH_SHORT).show();
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),mMap.getCameraPosition().zoom+1.5f));
//
////                GetGeoDesicLocation(marker.getPosition());
//
//            }
//        });


        confirmConsignment.setOnClickListener(v->finishConsignment(v));

    }

    private void GetGeoDesicLocation(LatLng position) {

        String url="https:///maps.googleapis.com/maps/api/geocode/json?latlng="+position.latitude+","+position.longitude+"&key="+getResources().getString(R.string.google_api_key);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(DestinationChooserActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "onResponse: date"+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    public void SaveConsignment(Consignment consignmentFinal)
    {


                AlertDialog.Builder builder=new AlertDialog.Builder(DestinationChooserActivity.this)
                        .setIcon(R.mipmap.ic_launcher_round)
                        .setTitle("Payment")
                        .setCancelable(false)
                        .setMessage("Proceeding to payment")
                        .setPositiveButton("Okay", (dialog,which)-> {
                            consignmentFinal.setStatus(ConsignmentStatus.PENDING_PAYMENT);
                            consignmentFinal.setOwner(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            consignmentViewmodel.SaveConsignment().observe(DestinationChooserActivity.this, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    dialog.dismiss();
                                    Intent intent=new Intent(DestinationChooserActivity.this,OwnerActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        })
                        .setNegativeButton("Not Now", (dialog,which)-> {
                                consignmentFinal.setStatus(ConsignmentStatus.PENDING_PAYMENT);
                            consignmentViewmodel.SaveConsignment().observe(DestinationChooserActivity.this, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    dialog.dismiss();
                                    Intent intent=new Intent(DestinationChooserActivity.this,OwnerActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        });

                builder.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String date= DateFormat.getDateInstance().format(calendar.getTime());
        consignment.setDate_of_pickup(date);


        AlertDialog.Builder builder=new AlertDialog.Builder(DestinationChooserActivity.this)
                .setIcon(R.mipmap.ic_launcher_round)
                .setTitle("Choose Container")
                .setCancelable(false)
                .setMessage("what knd of container looking for?")
                .setPositiveButton("Choose Container", (dialog,which)-> {

                        dialog.dismiss();
                });

        builder.show();
    }


    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        if(polylines.size()>0)
        {
            for(Polyline polyline:polylines)
            {
                polyline.remove();
            }
        }
        String distance = null;
        String destinationText = null;

        for(int i=0;i<route.size();i++)
        {
            distance=route.get(i).getDistanceText();
            destinationText=route.get(i).getEndAddressText();

        }
        consignment.setDistance(distance);
        consignment.setDestinationName(destinationText);


    }

    @Override
    public void onRoutingCancelled() {

    }
}
