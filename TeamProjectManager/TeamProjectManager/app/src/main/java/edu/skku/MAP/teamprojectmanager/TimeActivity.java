package edu.skku.MAP.teamprojectmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TimeActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;
    private MyApplication MyApp;
    Context context;
    String userFindID;

    Button Add;

    EditText editScheduleNAME;
    String ScheduleNAME;

    ArrayList<String> days;
    ArrayAdapter<String> daysAdapter;
    Spinner selectDays;

    Calendar calendar;
    Button select_start, select_end;
    TextView time_from, time_to;

    String day;
    Intent intent;
    AlertDialog.Builder ad;
    LinearLayout layout;
    String start_time = "", end_time = "";
    String tableName;
    TextView title;
    public ToDo[] todos = new ToDo[100];
    TextView[] textViews = new TextView[100];
    Integer i = 0;
    Integer check = 0;
    RelativeLayout container;
    public Integer height;
    public Integer tempheight;
    public Integer gap;
    public Integer tempgap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        intent = new Intent(TimeActivity.this, TimeActivity.class);
        context = getApplicationContext();
        MyApp = (MyApplication)context;
        userFindID = MyApp.getTmpID();

        mPostReference = FirebaseDatabase.getInstance().getReference();

        Add = TimeActivity.this.findViewById (R.id.ADD);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(TimeActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.add_schedule, null);
                ad.setView(view);

                editScheduleNAME = view.findViewById(R.id.editSCHEDULENAME);
                ScheduleNAME = editScheduleNAME.getText().toString();

                days = new ArrayList<String>();
                days.add("월요일");
                days.add("화요일");
                days.add("수요일");
                days.add("목요일");
                days.add("금요일");
                days.add("토요일");
                days.add("일요일");
                daysAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, days);
                selectDays = view.findViewById(R.id.SELECT_DAY);
                selectDays.setAdapter(daysAdapter);
                selectDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        day = days.get(i);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                time_from = view.findViewById(R.id.TIME_FROM);
                select_start = view.findViewById(R.id.SELECT_FROM);
                select_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        TimePickerDialog dialog = new TimePickerDialog(TimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                                String msg = String.format("%02d 시 %02d 분", hour, min);
                                start_time = String.format("%02d%02d", hour, min);
                                time_from.setText(msg);
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                        dialog.show();
                    }
                });

                time_to = view.findViewById(R.id.TIME_TO);
                select_end = view.findViewById(R.id.SELECT_TO);
                select_end.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        TimePickerDialog dialog = new TimePickerDialog(TimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                                String msg = String.format("%02d 시 %02d 분", hour, min);
                                end_time = String.format("%02d%02d", hour, min);
                                time_to.setText(msg);
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                        dialog.show();
                    }
                });



                ad.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ScheduleNAME = editScheduleNAME.getText().toString();
                        if(Integer.parseInt(start_time)>Integer.parseInt(end_time)){
                            Toast.makeText(TimeActivity.this, "정확한 시간을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else if ((ScheduleNAME.length() * day.length() *start_time.length() *end_time.length() ) == 0) {
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
                    if(get.day.toString().equals("월요일")){
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
                    else if(get.day.toString().equals("화요일")){
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
                    else if(get.day.toString().equals("수요일")){
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
                    else if(get.day.toString().equals("목요일")){
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
                    else if(get.day.toString().equals("금요일")){
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
                    else if(get.day.toString().equals("토요일")){
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
                    else if(get.day.toString().equals("일요일")){
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
        mPostReference.child("/table_list/"+userFindID).addValueEventListener(postListener);
    }

    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            ToDo post = new ToDo(ScheduleNAME, day, start_time , end_time);
            postValues = post.toMap();
        }
        childUpdates.put("/table_list/" + userFindID + "/" + ScheduleNAME, postValues);
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
        ScheduleNAME = "";
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