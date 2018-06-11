package com.log9materials.android.log9attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vrinda on 15-01-2018.
 */

public class ActivitySignUp extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName, inputMobNumber;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        inputEmail = (EditText) findViewById(R.id.emailId);
        inputPassword = (EditText) findViewById(R.id.password);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputName = (EditText) findViewById(R.id.employeeName);
        inputMobNumber = (EditText) findViewById(R.id.employeeMobileNumber);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(ActivitySignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Toast.makeText(ActivitySignUp.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                if (!task.isSuccessful()) {
                                    Toast.makeText(ActivitySignUp.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ActivitySignUp.this, "Authentication Successful", Toast.LENGTH_SHORT).show();

                                    String str = getIntent().getStringExtra("User_Code");
                                    mDatabaseReference = firebaseDatabase.getReference("Employee").child(str);
                                    mDatabaseReference.child("Name").setValue(inputName.getText().toString());
                                    mDatabaseReference.child("Email").setValue(inputEmail.getText().toString());
                                    mDatabaseReference.child("Contact").setValue(inputMobNumber.getText().toString());
                                    mDatabaseReference.child("Counter").setValue("1");

                                    Log.v("ABCD",editEmailforMailTable(inputEmail.getText().toString()));
                                    mDatabaseReference=firebaseDatabase.getReference("Mail");
                                    mDatabaseReference.child(editEmailforMailTable(inputEmail.getText().toString())).setValue(str);
                                    Intent intent = new Intent(ActivitySignUp.this, EmployeeMainActivity.class);
                                    intent.putExtra("EmailIdOfEmployee", inputEmail.getText().toString());
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });
    }

    public String editEmailforMailTable(String s) {

        if (s == null || s.trim().isEmpty()) {
            System.out.println("Incorrect format of string");
            return null;
        }
        int len = s.length();
        String newS="";
        int i;
        for (i = 0; i < len; i++) {
            int ascii = (int) s.charAt(i);
            if ((ascii >= 65 && ascii <= 90) || (ascii >= 97 && ascii <= 122) || (ascii >= 48 && ascii <= 57)) {
                newS+=s.charAt(i);
            } else {
                newS+='_';
            }
        }
        return newS;
    }
}

