package edu.skku.MAP.teamprojectmanager;

import android.app.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AddProjectActivity extends Activity {
    private DatabaseReference mPostReference;
    private MyApplication MyApp;
    Context context;
    Intent intent;

    Button Close;
    Button AddProject;
    Button From, To;
    Button AddFriend;
    TextView selectedFrom, selectedTo;
    String period_from, period_to;
    int from_year, from_month, from_day;
    int to_year, to_month, to_day;
    EditText ProjectNameET;
    String ProjectName;
    String userSEARCHID;
    ArrayList<String> participants;
    ArrayList<String> participantsNAME;
    int total_participants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_project);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        context = getApplicationContext();
        MyApp = (MyApplication)context;
        userSEARCHID = MyApp.getTmpID();

        ProjectNameET = AddProjectActivity.this.findViewById(R.id.editPROJECTNAME);
        selectedFrom = AddProjectActivity.this.findViewById(R.id.PERIOD_FROM);
        selectedTo = AddProjectActivity.this.findViewById(R.id.PERIOD_TO);

        final Calendar cal = Calendar.getInstance();


        AddFriend = AddProjectActivity.this.findViewById(R.id.PARTICIPANTS_SELECT);
        AddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(AddProjectActivity.this, SelectFriendActivity.class);
                startActivityForResult(intent, 3000);
            }
        });

        From = AddProjectActivity.this.findViewById(R.id.SELECT_FROM);
        From.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(AddProjectActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        period_from = String.format("%d.%d.%d", year, month+1, date);
                        from_year = year;
                        from_month = month+1;
                        from_day = date;
                        Toast.makeText(AddProjectActivity.this, period_from, Toast.LENGTH_SHORT).show();
                        selectedFrom.setText(period_from);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                //dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        To = AddProjectActivity.this.findViewById(R.id.SELECT_TO);
        To.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(AddProjectActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        period_to = String.format("%d.%d.%d", year, month+1, date);
                        to_year = year;
                        to_month = month+1;
                        to_day = date;
                        selectedTo.setText(period_to);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.show();
            }
        });


        Close = AddProjectActivity.this.findViewById(R.id.CLOSE);
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AddProject = AddProjectActivity.this.findViewById(R.id.ADD);
        AddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectName = ProjectNameET.getText().toString();
                if(to_year>from_year){
                    addProject();
                }
                else if(to_year==from_year){
                    if(to_month>from_month){
                        addProject();
                    }
                    else if(to_month==from_month){
                        if(to_day>from_day){
                            addProject();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"프로젝트 시작 날짜와 종료 날짜를\n정확히 입력해주세요",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "프로젝트 시작 날짜와 종료 날짜를\n정확히 입력해주세요", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"프로젝트 시작 날짜와 종료 날짜를\n정확히 입력해주세요",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3000) {
            if(resultCode==RESULT_OK) {
                Bundle MBuddle = data.getExtras();
                participants = MBuddle.getStringArrayList("participants"); //추가한 친구 리스트 받아옴
                participantsNAME = MBuddle.getStringArrayList("participantsNAME"); //추가한 친구 리스트 받아옴
                if(participants==null){
                    Toast.makeText(AddProjectActivity.this, "추가할 친구 정보를 받아올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AddProjectActivity.this, "총 " + participants.size()+"명을 추가하셨습니다.", Toast.LENGTH_SHORT).show();
                    total_participants = participants.size();
                }
            }
            else{
                Toast.makeText(AddProjectActivity.this, "추가할 친구 정보를 받아올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(AddProjectActivity.this, "추가할 친구 정보를 받아올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addProject(){
        if(ProjectName.length()==0 || period_from.length()==0 || period_to.length()==0 || total_participants==0){
            Toast.makeText(getApplicationContext(),"모든 정보를 입력해주세요",Toast.LENGTH_LONG).show();
        }
        else{
            for(int i=0;i<total_participants;i++) {
                postProjectFirebaseDatabase(true, participants.get(i));
                for(int j=0;j<total_participants;j++){
                    postParticipantsFirebaseDatabase(true, participants.get(i), participants.get(j));
                }
            }
            postChatroomFirebaseDatabase(true);
        }

    }

    public void postProjectFirebaseDatabase(boolean add, String ID){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            ChatListItemPost post = new ChatListItemPost(ProjectName, period_from, period_to);
            postValues = post.toMap();
        }
        childUpdates.put("/UserList/" + ID + "/PROJECTS/" + ProjectName, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    public void postParticipantsFirebaseDatabase(boolean add, String Owner, String Party){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        HashMap<String, Object> result = new HashMap<>();
        if(add){
            result.put("TMPID", Party);
            postValues = result;
        }
        childUpdates.put("/UserList/" + Owner + "/PROJECTS/" + ProjectName+"/PARTICIPANTS/"+Party, postValues);
        mPostReference.updateChildren(childUpdates);
    }


    public void postChatroomFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        HashMap<String, Object> result = new HashMap<>();
        if(add){
            for(int i=0;i<total_participants;i++) {
                result.put(participantsNAME.get(i), participantsNAME.get(i));
            }
            postValues = result;
        }
        childUpdates.put("/ChatRoomList/" + ProjectName+"/Participants", postValues);
        mPostReference.updateChildren(childUpdates);
        clearET();
    }

    public void clearET () {
        ProjectNameET.setText("");
        selectedFrom.setText("");
        selectedTo.setText("");
        participants = new ArrayList<String>();
    }

}
