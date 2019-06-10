package edu.skku.MAP.teamprojectmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class SelectFriendActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> friends;
    ArrayList<String> searchIDs;
    ArrayAdapter<String> adapter;
    Button Done;
    private DatabaseReference mPostReference;
    EditText findUserIDET;
    private MyApplication MyApp;
    Context context;
    String userFindID;
    ArrayList<String> selected;
    ArrayList<String> selectedNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        listView = (ListView)findViewById(R.id.friendlist);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        friends = new ArrayList<String>();
        searchIDs = new ArrayList<String>();
        selected = new ArrayList<String>();
        selectedNAME = new ArrayList<String>();
        findUserIDET = findViewById(R.id.SEARCHFRIEND);

        context = getApplicationContext();
        MyApp = (MyApplication)context;
        userFindID = MyApp.getTmpID();

        Done = SelectFriendActivity.this.findViewById(R.id.DONE);
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFriends();
                if(selected==null){
                    Toast.makeText(SelectFriendActivity.this, "선택된 친구가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(SelectFriendActivity.this, AddProjectActivity.class);
                    intent.putStringArrayListExtra("participants", selected);
                    intent.putStringArrayListExtra("participantsNAME", selectedNAME);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

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
                    friends.add(get.getNAME());
                    searchIDs.add(get.getTMPID());
                }
                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.multiple_choice, friends);
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("/UserList/" + userFindID + "/FRIENDS/").addValueEventListener(postListener);
    }

    public void selectedFriends(){
        int total_friends = listView.getCount();
        SparseBooleanArray test = listView.getCheckedItemPositions();
        for(int i=0;i<total_friends;i++){
            if(test.get(i)){ // 체크 되었다
                selected.add(searchIDs.get(i));
                selectedNAME.add(friends.get(i));
            }
        }
        selected.add(MyApp.getTmpID());
        selectedNAME.add(MyApp.getUserNAME());
    }
}
