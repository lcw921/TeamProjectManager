package edu.skku.MAP.teamprojectmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TimeActivity extends AppCompatActivity {

    Intent intent;
    AlertDialog.Builder ad;
    LinearLayout layout;
    String todo_id="", day= "", start_time = "", end_time = "";
    String tableName;
    TextView title;
    public ToDo[] todos = new ToDo[100];
    TextView[] textViews = new TextView[100];
    Integer i = 0;
    Integer check = 0;
    RelativeLayout container;
    Button button1, button2;
    private DatabaseReference mPostReference;
    public Integer height;
    public Integer tempheight;
    public Integer gap;
    public Integer tempgap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        button1 = TimeActivity.this.findViewById (R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(TimeActivity.this);
                ad.setTitle("할일 추가");

                intent = new Intent(TimeActivity.this, TimeActivity.class);
                layout = new LinearLayout(TimeActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText todo = new EditText(TimeActivity.this);
                todo.setHint("수업명");
                layout.addView(todo);
                final EditText dof = new EditText(TimeActivity.this);
                dof.setHint("수업요일");
                layout.addView(dof);
                final EditText start = new EditText(TimeActivity.this);
                start.setHint("시작시간");
                layout.addView(start);
                final EditText end = new EditText(TimeActivity.this);
                end.setHint("종료시간");
                layout.addView(end);
                ad.setView(layout);

                ad.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        todo_id = todo.getText().toString();
                        day = dof.getText().toString();
                        start_time = start.getText().toString();
                        end_time = end.getText().toString();

                        if ((todo_id.length() * day.length() *start_time.length() *end_time.length() ) == 0) {
                            Toast.makeText(TimeActivity.this, "공란을 채워주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            checking();
                            if(check == 0){
                                postFirebaseDatabase(true);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(TimeActivity.this, "같은 시간에 할 일이 있습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        clearC();
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

        button2 = TimeActivity.this.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(TimeActivity.this, Overlap.class);
                startActivity(intent);
            }
        });

        timeclear();
        getFirebaseDatabase();
    }

    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                i = 0;
                timeclear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final float scale = getResources().getDisplayMetrics().density;
                    tempheight = 0;
                    height = 0;
                    gap = 0;
                    tempgap = 0;
                    String key = postSnapshot.getKey();
                    ToDo get = postSnapshot.getValue(ToDo.class);
                    String[] info = {get.todo_id, get.day, get.start_time, get.end_time};
                    todos[i] = new ToDo(get.todo_id, get.day, get.start_time, get.end_time);
                    Log.d("getFirebaseDatabase", "todos "  + todos[i].todo_id + todos[i].day +todos[i].start_time+ todos[i].end_time);
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3]);
                    if(get.day.toString().equals("월")){
                        container = (RelativeLayout) findViewById(R.id.mon);
                        textViews[i] = new TextView(TimeActivity.this);
                        textViews[i].setText(get.todo_id.toString());
                        tempheight =  Integer.parseInt(info[3])-Integer.parseInt(info[2]);
                        height = (int) ((tempheight/100 * 72 + tempheight%100 * 1.2) * scale + 0.5f);
                        tempgap = Integer.parseInt(info[2]) - 900;
                        gap = (int) ((tempgap/100 * 72 + tempgap%100 * 1.2 + 45) * scale + 0.5f);
                        //textViews[i].setBackground(MainActivity.this.getResources().getDrawable(R.drawable.edge));
                        container.addView(textViews[i]);
                        textViews[i].setHeight(height);
                        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) textViews[i].getLayoutParams();
                        RelativeLayout.LayoutParams s = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                        s.setMargins(0, gap, 0 ,0);
                        textViews[i].setLayoutParams(s);
                        textViews[i].bringToFront();
                        textViews[i].setGravity(Gravity.CENTER_HORIZONTAL);
                        //textViews[i].setBackgroundColor(getResources().getColor(R.color.skyblue));
                        textViews[i].setBackground(getResources().getDrawable((R.drawable.edge1)));
                        i++;
                    }
                    else if(get.day.toString().equals("화")){
                        container = (RelativeLayout) findViewById(R.id.tue);
                        textViews[i] = new TextView(TimeActivity.this);
                        textViews[i].setText(get.todo_id.toString());
                        tempheight =  Integer.parseInt(info[3])-Integer.parseInt(info[2]);
                        height = (int) ((tempheight/100 * 72 + tempheight%100 * 1.2) * scale + 0.5f);
                        tempgap = Integer.parseInt(info[2]) - 900;
                        gap = (int) ((tempgap/100 * 72 + tempgap%100 * 1.2 + 45) * scale + 0.5f);
                        container.addView(textViews[i]);
                        textViews[i].setHeight(height);
                        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) textViews[i].getLayoutParams();
                        RelativeLayout.LayoutParams s = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                        s.setMargins(0, gap, 0 ,0);
                        textViews[i].setLayoutParams(s);
                        textViews[i].bringToFront();
                        textViews[i].setGravity(Gravity.CENTER_HORIZONTAL);
                        textViews[i].setBackground(getResources().getDrawable((R.drawable.edge1)));
                        i++;
                    }
                    else if(get.day.toString().equals("수")){
                        container = (RelativeLayout) findViewById(R.id.wen);
                        textViews[i] = new TextView(TimeActivity.this);
                        textViews[i].setText(get.todo_id.toString());
                        tempheight =  Integer.parseInt(info[3])-Integer.parseInt(info[2]);
                        height = (int) ((tempheight/100 * 72 + tempheight%100 * 1.2) * scale + 0.5f);
                        tempgap = Integer.parseInt(info[2]) - 900;
                        gap = (int) ((tempgap/100 * 72 + tempgap%100 * 1.2 + 45) * scale + 0.5f);
                        container.addView(textViews[i]);
                        textViews[i].setHeight(height);
                        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) textViews[i].getLayoutParams();
                        RelativeLayout.LayoutParams s = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                        s.setMargins(0, gap, 0 ,0);
                        textViews[i].setLayoutParams(s);
                        textViews[i].bringToFront();
                        textViews[i].setGravity(Gravity.CENTER_HORIZONTAL);
                        textViews[i].setBackground(getResources().getDrawable((R.drawable.edge1)));
                        i++;
                    }
                    else if(get.day.toString().equals("목")){
                        container = (RelativeLayout) findViewById(R.id.thur);
                        textViews[i] = new TextView(TimeActivity.this);
                        textViews[i].setText(get.todo_id.toString());
                        tempheight =  Integer.parseInt(info[3])-Integer.parseInt(info[2]);
                        height = (int) ((tempheight/100 * 72 + tempheight%100 * 1.2) * scale + 0.5f);
                        tempgap = Integer.parseInt(info[2]) - 900;
                        gap = (int) ((tempgap/100 * 72 + tempgap%100 * 1.2 + 45) * scale + 0.5f);
                        container.addView(textViews[i]);
                        textViews[i].setHeight(height);
                        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) textViews[i].getLayoutParams();
                        RelativeLayout.LayoutParams s = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                        s.setMargins(0, gap, 0 ,0);
                        textViews[i].setLayoutParams(s);
                        textViews[i].bringToFront();
                        textViews[i].setGravity(Gravity.CENTER_HORIZONTAL);
                        textViews[i].setBackground(getResources().getDrawable((R.drawable.edge1)));
                        i++;
                    }
                    else if(get.day.toString().equals("금")){
                        container = (RelativeLayout) findViewById(R.id.fri);
                        textViews[i] = new TextView(TimeActivity.this);
                        textViews[i].setText(get.todo_id.toString());
                        tempheight =  Integer.parseInt(info[3])-Integer.parseInt(info[2]);
                        height = (int) ((tempheight/100 * 72 + tempheight%100 * 1.2) * scale + 0.5f);
                        tempgap = Integer.parseInt(info[2]) - 900;
                        gap = (int) ((tempgap/100 * 72 + tempgap%100 * 1.2 + 45) * scale + 0.5f);
                        container.addView(textViews[i]);
                        textViews[i].setHeight(height);
                        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) textViews[i].getLayoutParams();
                        RelativeLayout.LayoutParams s = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                        s.setMargins(0, gap, 0 ,0);
                        textViews[i].setLayoutParams(s);
                        textViews[i].bringToFront();
                        textViews[i].setGravity(Gravity.CENTER_HORIZONTAL);
                        textViews[i].setBackground(getResources().getDrawable((R.drawable.edge1)));
                        i++;
                    }
                    else if(get.day.toString().equals("토")){
                        container = (RelativeLayout) findViewById(R.id.sat);
                        textViews[i] = new TextView(TimeActivity.this);
                        textViews[i].setText(get.todo_id.toString());
                        tempheight =  Integer.parseInt(info[3])-Integer.parseInt(info[2]);
                        height = (int) ((tempheight/100 * 72 + tempheight%100 * 1.2) * scale + 0.5f);
                        tempgap = Integer.parseInt(info[2]) - 900;
                        gap = (int) ((tempgap/100 * 72 + tempgap%100 * 1.2 + 45) * scale + 0.5f);
                        container.addView(textViews[i]);
                        textViews[i].setHeight(height);
                        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) textViews[i].getLayoutParams();
                        RelativeLayout.LayoutParams s = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                        s.setMargins(0, gap, 0 ,0);
                        textViews[i].setLayoutParams(s);
                        textViews[i].bringToFront();
                        textViews[i].setGravity(Gravity.CENTER_HORIZONTAL);
                        textViews[i].setBackground(getResources().getDrawable((R.drawable.edge1)));
                        i++;
                    }
                    else if(get.day.toString().equals("일")){
                        container = (RelativeLayout) findViewById(R.id.sun);
                        textViews[i] = new TextView(TimeActivity.this);
                        textViews[i].setText(get.todo_id.toString());
                        tempheight =  Integer.parseInt(info[3])-Integer.parseInt(info[2]);
                        height = (int) ((tempheight/100 * 72 + tempheight%100 * 1.2) * scale + 0.5f);
                        tempgap = Integer.parseInt(info[2]) - 900;
                        gap = (int) ((tempgap/100 * 72 + tempgap%100 * 1.2 + 45) * scale + 0.5f);
                        container.addView(textViews[i]);
                        textViews[i].setHeight(height);
                        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) textViews[i].getLayoutParams();
                        RelativeLayout.LayoutParams s = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                        s.setMargins(0, gap, 0 ,0);
                        textViews[i].setLayoutParams(s);
                        textViews[i].bringToFront();
                        textViews[i].setGravity(Gravity.CENTER_HORIZONTAL);
                        textViews[i].setBackground(getResources().getDrawable((R.drawable.edge1)));
                        i++;
                    }
                    else{
                        i++;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("table_list/table1").addValueEventListener(postListener);
    }

    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            ToDo post = new ToDo(todo_id, day, start_time , end_time);
            postValues = post.toMap();
        }
        childUpdates.put("/table_list/table1/" + todo_id, postValues);
        mPostReference.updateChildren(childUpdates);
        clearC();
    }

    public void timeclear(){
        int c;
        for(c = 0; c < 100; c++){
            todos[i] = new ToDo("","","","");
        }
    }

    public void clearC () {
        todo_id = "";
        day = "";
        start_time = "";
        end_time = "";
    }

    public  void checking(){
        int c;
        int start_time1, start_time2, end_time1, end_time2;
        check = 0;
        for(c = 0; c < i; c++){
            Log.d("getFirebaseDatabase", "todos on checking "  + todos[c].todo_id + todos[c].day +todos[c].start_time+ todos[c].end_time);
            if(todos[c].day.toString().equals("월") && day.toString().equals("월")){
                start_time1 = Integer.parseInt(start_time);
                start_time2 = Integer.parseInt(todos[c].start_time);
                end_time1 = Integer.parseInt(end_time);
                end_time2 = Integer.parseInt(todos[c].end_time);
                if((start_time1 == start_time2) && (end_time1 == end_time2)){
                    check++;
                }
                else if((start_time1 < start_time2) && (end_time1 > end_time2)){
                    check++;
                }
                else if((start_time1 > start_time2) && (start_time1 < end_time2)){
                    check++;
                }
                else if((end_time1 > start_time2) && (end_time1 < end_time2)){
                    check++;
                }
                else if((start_time1 > start_time2) && (end_time1 < end_time2)){
                    check++;
                }
            }
            if(todos[c].day.toString().equals("화") && day.toString().equals("화")){
                start_time1 = Integer.parseInt(start_time);
                start_time2 = Integer.parseInt(todos[c].start_time);
                end_time1 = Integer.parseInt(end_time);
                end_time2 = Integer.parseInt(todos[c].end_time);
                if((start_time1 == start_time2) && (end_time1 == end_time2)){
                    check++;
                }
                else if((start_time1 < start_time2) && (end_time1 > end_time2)){
                    check++;
                }
                else if((start_time1 > start_time2) && (start_time1 < end_time2)){
                    check++;
                }
                else if((end_time1 > start_time2) && (end_time1 < end_time2)){
                    check++;
                }
                else if((start_time1 > start_time2) && (end_time1 < end_time2)){
                    check++;
                }
            }
            if(todos[c].day.toString().equals("수") && day.toString().equals("수")){
                start_time1 = Integer.parseInt(start_time);
                start_time2 = Integer.parseInt(todos[c].start_time);
                end_time1 = Integer.parseInt(end_time);
                end_time2 = Integer.parseInt(todos[c].end_time);
                if((start_time1 == start_time2) && (end_time1 == end_time2)){
                    check++;
                }
                else if((start_time1 < start_time2) && (end_time1 > end_time2)){
                    check++;
                }
                else if((start_time1 > start_time2) && (start_time1 < end_time2)){
                    check++;
                }
                else if((end_time1 > start_time2) && (end_time1 < end_time2)){
                    check++;
                }
                else if((start_time1 > start_time2) && (end_time1 < end_time2)){
                    check++;
                }
            }
            if(todos[c].day.toString().equals("목") && day.toString().equals("목")){
                start_time1 = Integer.parseInt(start_time);
                start_time2 = Integer.parseInt(todos[c].start_time);
                end_time1 = Integer.parseInt(end_time);
                end_time2 = Integer.parseInt(todos[c].end_time);
                if((start_time1 == start_time2) && (end_time1 == end_time2)){
                    check++;
                }
                else if((start_time1 < start_time2) && (end_time1 > end_time2)){
                    check++;
                }
                else if((start_time1 > start_time2) && (start_time1 < end_time2)){
                    check++;
                }
                else if((end_time1 > start_time2) && (end_time1 < end_time2)){
                    check++;
                }
                else if((start_time1 > start_time2) && (end_time1 < end_time2)){
                    check++;
                }
            }
            if(todos[c].day.toString().equals("금") && day.toString().equals("금")){
                start_time1 = Integer.parseInt(start_time);
                start_time2 = Integer.parseInt(todos[c].start_time);
                end_time1 = Integer.parseInt(end_time);
                end_time2 = Integer.parseInt(todos[c].end_time);
                if((start_time1 == start_time2) && (end_time1 == end_time2)){
                    check++;
                }
                else if((start_time1 < start_time2) && (end_time1 > end_time2)){
                    check++;
                }
                else if((start_time1 > start_time2) && (start_time1 < end_time2)){
                    check++;
                }
                else if((end_time1 > start_time2) && (end_time1 < end_time2)){
                    check++;
                }
                else if((start_time1 > start_time2) && (end_time1 < end_time2)){
                    check++;
                }
            }
            if(todos[c].day.toString().equals("토") && day.toString().equals("토")){
                start_time1 = Integer.parseInt(start_time);
                start_time2 = Integer.parseInt(todos[c].start_time);
                end_time1 = Integer.parseInt(end_time);
                end_time2 = Integer.parseInt(todos[c].end_time);
                if((start_time1 == start_time2) && (end_time1 == end_time2)){
                    check++;
                }
                else if((start_time1 < start_time2) && (end_time1 > end_time2)){
                    check++;
                }
                else if((start_time1 > start_time2) && (start_time1 < end_time2)){
                    check++;
                }
                else if((end_time1 > start_time2) && (end_time1 < end_time2)){
                    check++;
                }
                else if((start_time1 > start_time2) && (end_time1 < end_time2)){
                    check++;
                }
            }
            if(todos[c].day.toString().equals("일") && day.toString().equals("일")){
                start_time1 = Integer.parseInt(start_time);
                start_time2 = Integer.parseInt(todos[c].start_time);
                end_time1 = Integer.parseInt(end_time);
                end_time2 = Integer.parseInt(todos[c].end_time);
                if((start_time1 == start_time2) && (end_time1 == end_time2)){
                    check++;
                }
                else if((start_time1 < start_time2) && (end_time1 > end_time2)){
                    check++;
                }
                else if((start_time1 > start_time2) && (start_time1 < end_time2)){
                    check++;
                }
                else if((end_time1 > start_time2) && (end_time1 < end_time2)){
                    check++;
                }
                else if((start_time1 > start_time2) && (end_time1 < end_time2)){
                    check++;
                }
            }
        }
    }
}
