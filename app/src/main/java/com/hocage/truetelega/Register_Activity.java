package com.hocage.truetelega;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Register_Activity extends AppCompatActivity {

    EditText userET, passET, emailET;
    Button registerBtn;

    FirebaseAuth auth;
    DatabaseReference myRef;

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        userET = findViewById(R.id.editUsername);
        passET = findViewById(R.id.editTextPassword);
        emailET = findViewById(R.id.editMail);
        registerBtn = findViewById(R.id.buttonRegister);

        auth = FirebaseAuth.getInstance();
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_text = userET.getText().toString();
                String email_text = emailET.getText().toString();
                String pass_text = passET.getText().toString();

                


                if(TextUtils.isEmpty(username_text) || TextUtils.isEmpty(email_text) || TextUtils.isEmpty(pass_text)){
                    Toast.makeText(Register_Activity.this, "Please FILL ALL Fields", Toast.LENGTH_SHORT).show();

                }else{
                    RegisterNow(username_text,email_text,pass_text);
                }
            }
        });

    }

    private void RegisterNow(final String username, String email, String password)
    {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();

                    myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);


                    Map<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username);
                    hashMap.put("imageURL", "default");
                    //sssssssssssssssssssss

                    //ssssssssssssssssssss
                    //Openign MAIN ACTIVITY AFTER SUCCESS LOGIN

                    myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Intent i = new Intent(Register_Activity.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
                }else {
                    Toast.makeText(Register_Activity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
