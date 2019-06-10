package edu.skku.MAP.teamprojectmanager;

import android.content.Context;
import android.content.Intent;
import android.icu.lang.UCharacterEnums;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<FriendsItem> friends;
    FriendsAdapter adapter;
    private DatabaseReference mPostReference;
    private MyApplication MyApp;
    Context context;
    String userFindID;
    Button AddProject;
    Button ToChatroom;
    Button ManageTime;
    Button Search;
    EditText findUserIDET;
    String findUserID;
    String findUserNAME;
    String findUserTMPID;

    Integer check = 0;
    Integer myID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        listView = (ListView)findViewById(R.id.friendlist);
        friends = new ArrayList<FriendsItem>();
        findUserIDET = findViewById(R.id.SEARCHFRIEND);

        context = getApplicationContext();
        MyApp = (MyApplication)context;
        userFindID = MyApp.getTmpID();

        ManageTime = MainActivity.this.findViewById(R.id.MANAGETIME);
        ManageTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TimeActivity.class);
                startActivity(intent);
            }
        });

        ToChatroom = MainActivity.this.findViewById(R.id.CHATROOM);
        ToChatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
                startActivity(intent);
            }
        });

        AddProject = MainActivity.this.findViewById(R.id.ADDPROJECT);
        AddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddProjectActivity.class);
                startActivity(intent);
            }
        });

        Search = MainActivity.this.findViewById(R.id.SEARCH);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myID=0;
                findUserID = findUserIDET.getText().toString();
                readData(new FirebaseCallback() {
                    @Override
                    public void onCallback(Integer c) {
                        Log.d("check5", "key: " + c.toString());
                        if(myID==1){
                            Toast.makeText(MainActivity.this, "본인의 아이디입니다.", Toast.LENGTH_SHORT).show();
                        } else if (findUserID.length() == 0) {
                        } else if ((findUserID.length() != 0) && (c ==0)){
                            Toast.makeText(MainActivity.this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show();
                        } else if ((findUserID.length() != 0) && (c ==1)){
                            postFirebaseDatabase(true);
                        }
                    }
                });
            }
        });

        adapter = new FriendsAdapter(this, friends);
        listView.setAdapter(adapter);
        getFirebaseDatabase();
    }

    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                friends.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    FriendsItem get = postSnapshot.getValue(FriendsItem.class);
                    String[] info = {get.getNAME()};
                    String result = info[0] ;
                    friends.add(get);
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "itermid");
                    Log.d("getFirebaseDatabase", "info: " + info[0]);
                    Log.d("getFirebaseDatabase", "iterend");
                }
                adapter = new FriendsAdapter(MainActivity.this, friends);
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("/UserList/" + userFindID + "/FRIENDS/").addValueEventListener(postListener);
    }

    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        HashMap<String, Object> result = new HashMap<>();
        if(add){
            result.put("NAME", findUserNAME);
            result.put("ID", findUserID);
            result.put("TMPID", findUserTMPID);
            postValues = result;
        }
        childUpdates.put("/UserList/" + userFindID + "/FRIENDS/"+findUserNAME, postValues);
        mPostReference.updateChildren(childUpdates);
        clearET();
    }

    public void clearET () {
        findUserIDET.setText("");
        findUserID = "";
    }

    private void readData(final FirebaseCallback firebaseCallback){
        check = 0;
        Log.d("check1", "key: " + check.toString());
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    UserPost get = postSnapshot.getValue(UserPost.class);
                    if(findUserID.equals(MyApp.getUserID())){
                        myID=1;
                    }
                    else if(findUserID.equals(get.ID)){
                        findUserTMPID = postSnapshot.getKey();
                        findUserNAME = get.NAME;
                        check++;
                    }
                }
                firebaseCallback.onCallback(check);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("UserList").addValueEventListener(postListener);
    }

    private interface FirebaseCallback{
        void onCallback(Integer c);
    }
}
