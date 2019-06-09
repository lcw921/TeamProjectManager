package edu.skku.MAP.teamprojectmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Overlap extends AppCompatActivity {

    public ToDo[] todos = new ToDo[100];
    TextView[] textViews = new TextView[100];
    Integer i = 0;
    RelativeLayout container;
    //
    String t_name[] = new String[5];
    Integer t_number = 2;
    Integer t = 0;
    //
    private DatabaseReference mPostReference;
    public Integer height;
    public Integer tempheight;
    public Integer gap;
    public Integer tempgap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        //
        t_name[0] = "table1";
        t_name[1] = "table2";
        //
        getFirebaseDatabase();
    }

    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                i = 0;
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
                        textViews[i] = new TextView(Overlap.this);
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
                        textViews[i].setBackground(getResources().getDrawable((R.drawable.edge2)));
                        i++;
                    }
                    else if(get.day.toString().equals("화")){
                        container = (RelativeLayout) findViewById(R.id.tue);
                        textViews[i] = new TextView(Overlap.this);
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
                        textViews[i].setBackground(getResources().getDrawable((R.drawable.edge2)));
                        i++;
                    }
                    else if(get.day.toString().equals("수")){
                        container = (RelativeLayout) findViewById(R.id.wen);
                        textViews[i] = new TextView(Overlap.this);
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
                        textViews[i].setBackground(getResources().getDrawable((R.drawable.edge2)));
                        i++;
                    }
                    else if(get.day.toString().equals("목")){
                        container = (RelativeLayout) findViewById(R.id.thur);
                        textViews[i] = new TextView(Overlap.this);
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
                        textViews[i].setBackground(getResources().getDrawable((R.drawable.edge2)));
                        i++;
                    }
                    else if(get.day.toString().equals("금")){
                        container = (RelativeLayout) findViewById(R.id.fri);
                        textViews[i] = new TextView(Overlap.this);
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
                        textViews[i].setBackground(getResources().getDrawable((R.drawable.edge2)));
                        i++;
                    }
                    else if(get.day.toString().equals("토")){
                        container = (RelativeLayout) findViewById(R.id.sat);
                        textViews[i] = new TextView(Overlap.this);
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
                        textViews[i].setBackground(getResources().getDrawable((R.drawable.edge2)));
                        i++;
                    }
                    else if(get.day.toString().equals("일")){
                        container = (RelativeLayout) findViewById(R.id.sun);
                        textViews[i] = new TextView(Overlap.this);
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
                        textViews[i].setBackground(getResources().getDrawable((R.drawable.edge2)));
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
        for(t = 0; t < t_number; t++){
            mPostReference.child("table_list/" + t_name[t]).addValueEventListener(postListener);
        }
    }
}
