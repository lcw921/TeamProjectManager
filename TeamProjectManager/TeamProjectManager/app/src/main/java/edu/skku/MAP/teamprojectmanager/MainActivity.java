package edu.skku.MAP.teamprojectmanager;

import android.content.Context;
import android.content.Intent;
import android.icu.lang.UCharacterEnums;
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
import java.util.Date;
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
                findUserID = findUserIDET.getText().toString();
                if (findUserID.length() == 0) {
                    Toast.makeText(MainActivity.this, "친구의 ID를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    postFirebaseDatabase(true);
                }
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
                    Log.d("getFirebaseDatabase", "info: " + info[0]);
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
            FriendsItem post = new FriendsItem(findUserID);
            result.put("NAME", findUserID);
            postValues = result;
        }
        childUpdates.put("/UserList/" + userFindID + "/FRIENDS/"+findUserID, postValues);
        mPostReference.updateChildren(childUpdates);
        clearET();
    }

    public void clearET () {
        findUserIDET.setText("");
        findUserID = "";
    }
}
