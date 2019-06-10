package edu.skku.MAP.teamprojectmanager;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private DatabaseReference mPostReference;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button1, button2;
    private EditText editText;
    String Address;
    String Latitude;
    String Longitude;
    String roomID;
    String address, latitude, longitude;
    Integer check = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        editText = (EditText) findViewById(R.id.editText);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);

        button1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str=editText.getText().toString();
                List<Address> addressList = null;
                if(str.length() == 0){
                    Toast.makeText(LocationActivity.this, "찾고자 하는 지역을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        addressList = geocoder.getFromLocationName(
                                str,
                                10);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    String []splitStr = addressList.get(0).toString().split(",");
                    address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2);
                    latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1);
                    longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1);
                    check++;

                    LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    MarkerOptions mOptions2 = new MarkerOptions();
                    mOptions2.title("search result");
                    mOptions2.snippet(address);
                    mOptions2.position(point);
                    mMap.addMarker(mOptions2);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check == 0){
                    Toast.makeText(LocationActivity.this, "장소를 먼저 검색해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    postFirebaseDatabase(true);
                }
            }
        });
        Intent intent = getIntent();
        roomID = intent.getStringExtra("roomID");

        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        getFirebaseDatabase();
    }

    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        HashMap<String, Object> result = new HashMap<>();
        if(add){
            LocationItem post = new LocationItem(address, latitude, longitude);
            result.put("ADDRESS", address);
            result.put("LATITUDE", latitude);
            result.put("LONGITUDE", longitude);
            postValues = result;
        }
        childUpdates.put("/ChatRoomList/" + roomID + "/Location/"+ "address", postValues);
        mPostReference.updateChildren(childUpdates);
    }

    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    LocationItem get = postSnapshot.getValue(LocationItem.class);
                    Log.d("getFirebaseDatabase", "key: " + key);
                    LatLng teamplace = new LatLng(Double.parseDouble(get.getLATITUDE()), Double.parseDouble(get.getLONGITUDE()));
                    mMap.addMarker(new MarkerOptions().position(teamplace).title("TEAM PLACE : "  + get.getADDRESS()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(teamplace,15));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("/ChatRoomList/" + roomID + "/Location/").addValueEventListener(postListener);
    }
}