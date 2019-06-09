package edu.skku.MAP.teamprojectmanager;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mPostReference;
    private MyApplication MyApp;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    ArrayList<UserPost> users;

    private FirebaseAuth Auth;
    private EditText typeEMAIL, typePW;
    private String EMAIL, PW;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        context = getApplicationContext();
        MyApp = (MyApplication)context;

        Auth = FirebaseAuth.getInstance();
        context = getApplicationContext();
        typeEMAIL = findViewById(R.id.typeEMAIL);
        typePW = findViewById(R.id.typePW);

        Button login= findViewById(R.id.LOGIN);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMAIL = typeEMAIL.getText().toString();
                PW = typePW.getText().toString();
                signIn();
            }
        });

        Button join = findViewById(R.id.JOIN);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

    }

    public void signIn() {
        if(isValidEmail() && isValidPasswd()) {
            loginUser();
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (!Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()) {
            final AlertDialog.Builder emailAlert = new AlertDialog.Builder(LoginActivity.this);
            emailAlert.setTitle("유효한 이메일 주소가 아닙니다");
            emailAlert.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"이메일 주소를 다시 입력해주세요",Toast.LENGTH_LONG).show();
                        }
                    });
            emailAlert.show();
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (!PASSWORD_PATTERN.matcher(PW).matches()) {
            final AlertDialog.Builder pwAlert = new AlertDialog.Builder(LoginActivity.this);
            pwAlert.setTitle("유효한 비밀번호가 아닙니다");
            pwAlert.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"비밀번호를 다시 입력해주세요",Toast.LENGTH_LONG).show();
                        }
                    });
            pwAlert.show();
            return false;
        } else {
            return true;
        }
    }

    private void loginUser(){
        Auth.signInWithEmailAndPassword(EMAIL, PW)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                            MyApplication MyApp = (MyApplication)context;
                            String[] tmpID = EMAIL.split("@");
                            MyApp.setTmpID(tmpID[0]);
                            getUserInfo(tmpID[0]);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    public void getUserInfo(String tmpID) {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                MyApp.setUserNAME(dataSnapshot.child("NAME").getValue(String.class));
                MyApp.setUserID(dataSnapshot.child("ID").getValue(String.class));
                String key = dataSnapshot.getKey();
                Log.d("getFirebaseDatabase", "key: " + key);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        mPostReference.child("/UserList/" + tmpID + "/").addListenerForSingleValueEvent(postListener);
    }
}
