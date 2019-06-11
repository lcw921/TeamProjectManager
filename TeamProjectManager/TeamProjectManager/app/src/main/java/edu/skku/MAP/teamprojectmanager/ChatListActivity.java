package edu.skku.MAP.teamprojectmanager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatListActivity extends AppCompatActivity {

    ChatListItemAdapter chatlistAdapter;
    ArrayList<ChatListItem> chatlists;
    ListView listView;
    String userSEARCHID;

    private DatabaseReference mPostReference;
    private MyApplication MyApp;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        context = getApplicationContext();
        MyApp = (MyApplication)context;
        userSEARCHID = MyApp.getTmpID();

        listView = ChatListActivity.this.findViewById(R.id.Chatlists);
        chatlists = new ArrayList<ChatListItem>();
        chatlistAdapter = new ChatListItemAdapter(this, chatlists);
        listView.setAdapter(chatlistAdapter);

        getFirebaseDatabase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getApplicationContext(), ChatroomActivity.class);
                intent.putExtra("roomID", chatlists.get(position).getPROJECTNAME()); // 클릭한 리스트의 번호를 전달
                intent.putExtra("from", chatlists.get(position).getFROM()); // 클릭한 리스트의 번호를 전달
                intent.putExtra("to", chatlists.get(position).getTO()); // 클릭한 리스트의 번호를 전달
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration configuration){
        super.onConfigurationChanged(configuration);
        setContentView(R.layout.activity_chatlist);
        listView = ChatListActivity.this.findViewById(R.id.Chatlists);
        listView.setAdapter(chatlistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getApplicationContext(), ChatroomActivity.class);
                intent.putExtra("roomID", chatlists.get(position).getPROJECTNAME()); // 클릭한 리스트의 번호를 전달
                intent.putExtra("from", chatlists.get(position).getFROM()); // 클릭한 리스트의 번호를 전달
                intent.putExtra("to", chatlists.get(position).getTO()); // 클릭한 리스트의 번호를 전달
                startActivity(intent);
            }
        });
    }


    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                chatlists.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    ChatListItemPost get = postSnapshot.getValue(ChatListItemPost.class);
                    String[] info = {get.PRJECTNAME, get.FROM, get.TO};
                    ChatListItem newChat = new ChatListItem(info[0],info[1],info[2]);
                    chatlists.add(newChat);
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2]);
                }
                chatlistAdapter = new ChatListItemAdapter(ChatListActivity.this, chatlists);
                listView.setAdapter(chatlistAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("/UserList/" + userSEARCHID + "/PROJECTS/").addValueEventListener(postListener);
    }

}
