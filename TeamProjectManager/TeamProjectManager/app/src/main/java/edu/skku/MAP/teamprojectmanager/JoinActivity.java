package edu.skku.MAP.teamprojectmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    private FirebaseAuth Auth;

    private EditText typeNAME, typeID, typePW, typePW_CHECK, typeEMAIL;

    private String NAME, ID, PW, PW_CHECK, EMAIL;

    private DatabaseReference mPostReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        typeNAME = findViewById(R.id.typeNAME);
        typeID = findViewById(R.id.typeID);
        typePW = findViewById(R.id.typePW);
        typePW_CHECK = findViewById(R.id.typePW_CHECK);
        typeEMAIL = findViewById(R.id.typeEMAIL);

        Auth = FirebaseAuth.getInstance();

        mPostReference = FirebaseDatabase.getInstance().getReference();

        Button button = findViewById(R.id.JOIN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Join
                NAME = typeNAME.getText().toString();
                ID = typeNAME.getText().toString();
                EMAIL = typeEMAIL.getText().toString();
                PW = typePW.getText().toString();
                PW_CHECK = typePW_CHECK.getText().toString();

                if(NAME.length()==0 || ID.length()==0 || EMAIL.length()==0 || PW.length()==0 || PW_CHECK.length()==0){ // Empty String Exist
                    AlertDialog.Builder EmptyStringAlert = new AlertDialog.Builder(JoinActivity.this);
                    EmptyStringAlert.setTitle("입력하지 않은 내용이 있습니다");
                    EmptyStringAlert.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(),"정보를 모두 입력해주세요",Toast.LENGTH_LONG).show();
                                }
                            });
                    EmptyStringAlert.show();
                }

                else if (!PW.equals(PW_CHECK)) { // Different passward
                    AlertDialog.Builder pwAlert = new AlertDialog.Builder(JoinActivity.this);
                    pwAlert.setTitle("패스워드가 일치하지 않습니다");
                    pwAlert.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(),"패스워드를 다시 입력해주세요",Toast.LENGTH_LONG).show();
                                }
                            });
                    pwAlert.show();
                }
                else {
                    signUp();
                }
            }
        });
    }

    public void signUp() {
        if(isValidEmail() && isValidPasswd()) {
            createUser();
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (!Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()) {
            final AlertDialog.Builder emailAlert = new AlertDialog.Builder(JoinActivity.this);
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
            final AlertDialog.Builder pwAlert = new AlertDialog.Builder(JoinActivity.this);
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

    // 회원가입
    private void createUser() {
        Auth.createUserWithEmailAndPassword(EMAIL, PW)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(JoinActivity.this,"회원가입 성공", Toast.LENGTH_SHORT).show();
                            postFirebaseDatabase(true);
                            Intent intent= new Intent(JoinActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(JoinActivity.this, "이미 가입된 이메일 주소 입니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            UserPost post = new UserPost(EMAIL, ID, NAME);
            postValues = post.toMap();
        }
        String[] tmpID = EMAIL.split("@");
        childUpdates.put("/UserList/" + tmpID[0], postValues);
        mPostReference.updateChildren(childUpdates);
    }
}
