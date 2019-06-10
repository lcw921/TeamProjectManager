package edu.skku.MAP.teamprojectmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatroomActivity extends AppCompatActivity {

    private DatabaseReference mPostReference;
    private MyApplication MyApp;
    ListView listView;
    ArrayList<ChatItem> chats;
    ChatItemAdapter chatAdapter;
    Context context;
    Button btn, member, table, place, role;
    String userNAME;
    String roomID;
    String from;
    String contents;
    String time;
    EditText message;
    Integer n;
    String[] members = new String[100];
    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_chatroom);
        Intent intent = getIntent();
        mPostReference = FirebaseDatabase.getInstance().getReference();
        context = getApplicationContext();
        MyApp = (MyApplication)context;

        roomID = intent.getStringExtra("roomID"); // chat list에서 방 번호 전달 받기 --> 이걸 기반으로 채팅방을 로딩해야할텐데
        userNAME = MyApp.getUserNAME();

        listView = ChatroomActivity.this.findViewById(R.id.Chattings);
        chats = new ArrayList<ChatItem>();
        chatAdapter = new ChatItemAdapter(this, chats);
        listView.setAdapter(chatAdapter);
        getFirebaseDatabase();

        message = ChatroomActivity.this.findViewById(R.id.MESSAGE);
        member = (Button) findViewById(R.id.MEMBER);
        table = (Button) findViewById(R.id.TIMETABLE);
        place = (Button) findViewById(R.id.PLACE);
        role = (Button) findViewById(R.id.ROLE) ;

        btn = ChatroomActivity.this.findViewById(R.id.SEND);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contents = message.getText().toString();
                if(contents.isEmpty()) {
                    Toast.makeText( ChatroomActivity.this, "메세지를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    long Now;
                    Now = System.currentTimeMillis();
                    Date nowDate = new Date(Now);
                    time = timeFormat.format(nowDate);
                    postFirebaseDatabase(true);
                    from = userNAME;
                }
            }
        });

        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pinent = new Intent(ChatroomActivity.this, LocationActivity.class);
                startActivity(pinent);
            }
        });

        table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent time = new Intent(ChatroomActivity.this, Overlap.class);
                time.putExtra("NUMBER_OF_MEMBER", n);
                time.putExtra("MEMBER_NAME", members);
                startActivity(time);
            }
        });


    }
    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                chats.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    ChatItemPost get = postSnapshot.getValue(ChatItemPost.class);
                    String[] info = {get.FROM, get.CONTENTS, get.TIME};
                    ChatItem newChat = new ChatItem(info[0],info[1],info[2]);
                    chats.add(newChat);
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2]);
                }
                chatAdapter = new ChatItemAdapter(ChatroomActivity.this, chats);
                listView.setAdapter(chatAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("/ChatRoomList/" + roomID + "/Messages/").addValueEventListener(postListener);
    }

    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            ChatItemPost post = new ChatItemPost(userNAME, contents, time);
            postValues = post.toMap();
        }
        childUpdates.put("/ChatRoomList/" + roomID + "/Messages/"+time, postValues);
        mPostReference.updateChildren(childUpdates);
        clearET();
    }

    public void clearET () {
        message.setText("");
        from="";
        contents = "";
        time="";
    }

}
