package edu.skku.MAP.teamprojectmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class RoleActivity extends AppCompatActivity {

    ListView listView;
    private DatabaseReference mPostReference;
    ArrayList<Act> acts;
    ActAdapter adapter;
    AlertDialog.Builder ad;
    ArrayList<String> members;
    Integer n, r;
    LinearLayout layout;
    Intent intent;
    String act_name="", act_des= "", act_worker = "";
    String roomID;
    Integer check = 0;
    EditText actname, actdes;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_role);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        Button button = (Button) (findViewById(R.id.button));
        listView = RoleActivity.this.findViewById(R.id.listView);
        acts = new ArrayList<Act>();

        Intent rintent = getIntent();
        members = rintent.getStringArrayListExtra("MEMBER_NAME");
        n = rintent.getExtras().getInt("NUMBER_OF_MEMBER");
        roomID = rintent.getStringExtra("roomID");

        adapter = new ActAdapter(this, acts);
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(RoleActivity.this);
                ad.setTitle("할일 추가");

                //intent = new Intent(RoleActivity.this, RoleActivity.class);
                layout = new LinearLayout(RoleActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                actname = new EditText(RoleActivity.this);
                actname .setHint("할 일");
                layout.addView(actname );
                actdes = new EditText(RoleActivity.this);
                actdes.setHint("설명");
                layout.addView(actdes);
                ad.setView(layout);

                ad.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        act_name = actname.getText().toString();
                        act_des = actdes.getText().toString();

                        if ((act_name.length() * act_des.length()) == 0) {
                            Toast.makeText(RoleActivity.this, "공란을 채워주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            checking();
                            if(check == 0){
                                postFirebaseDatabase(true);
                                //startActivity(intent);
                                //finish();
                            }
                            else{
                                Toast.makeText(RoleActivity.this, "동일한 이름의 할 일이 있습니다..", Toast.LENGTH_SHORT).show();
                            }
                        }
                        getFirebaseDatabase();

                        dialog.dismiss();
                    }
                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });

        getFirebaseDatabase();

    }



    @Override
    public void onConfigurationChanged(Configuration configuration){
        act_name = actname.getText().toString();
        act_des = actdes.getText().toString();
        listView = RoleActivity.this.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        super.onConfigurationChanged(configuration);
        setContentView(R.layout.activity_role);
        Button button = (Button) (findViewById(R.id.button));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(RoleActivity.this);
                ad.setTitle("할일 추가");

                //intent = new Intent(RoleActivity.this, RoleActivity.class);
                layout = new LinearLayout(RoleActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                actname = new EditText(RoleActivity.this);
                if(act_name.length()==0) {
                    actname.setHint("할 일");
                }else{
                    actname.setText(act_name);
                }
                layout.addView(actname);
                actdes = new EditText(RoleActivity.this);
                if(act_des.length()==0) {
                    actdes.setHint("설명");
                }else{
                    actdes.setText(act_des);
                }
                layout.addView(actdes);
                ad.setView(layout);

                ad.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        act_name = actname.getText().toString();
                        act_des = actdes.getText().toString();

                        if ((act_name.length() * act_des.length()) == 0) {
                            Toast.makeText(RoleActivity.this, "공란을 채워주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            checking();
                            if(check == 0){
                                postFirebaseDatabase(true);
                                //startActivity(intent);
                                //finish();
                            }
                            else{
                                Toast.makeText(RoleActivity.this, "동일한 이름의 할 일이 있습니다..", Toast.LENGTH_SHORT).show();
                            }
                        }
                        actname.setText("");
                        actdes.setText("");
                        getFirebaseDatabase();
                        dialog.dismiss();
                    }
                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });
    }


    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                acts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    Act get = postSnapshot.getValue(Act.class);
                    Act nadd = new Act(get.getNAME(), get.getDescription(), get.getWorker());
                    acts.add(nadd);
                    Log.d("fire", "content : " + get.getNAME());
                }
                adapter = new ActAdapter(RoleActivity.this, acts);
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("/ChatRoomList/" + roomID + "/acts_list/").addValueEventListener(postListener);
    }

    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        HashMap<String, Object> result = new HashMap<>();
        r = (int)(Math.random()*n);
        act_worker = members.get(r);
        Log.d("fire", "content : " + r.toString());
        Log.d("post", "content : " + act_worker);
        if(add){
            Act post = new Act(act_name, act_des, act_worker);
            result.put("NAME", act_name);
            result.put("Description", act_des);
            result.put("Worker", act_worker);
            postValues = result;
        }
        childUpdates.put("/ChatRoomList/" + roomID + "/acts_list/" + act_name, postValues);
        mPostReference.updateChildren(childUpdates);
        clearC();
    }

    public void clearC(){
        act_name = "";
        act_des = "";
        act_worker = "";
    }

    public void checking(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                acts.clear();
                check = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    Act get = postSnapshot.getValue(Act.class);
                    if(act_name.equals(get.getNAME())){
                        check++;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("/ChatRoomList/" + roomID + "/acts_list").addValueEventListener(postListener);
    }
}

