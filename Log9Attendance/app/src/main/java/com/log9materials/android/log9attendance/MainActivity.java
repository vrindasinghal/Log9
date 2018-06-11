package com.log9materials.android.log9attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText mEmployeeCode;
    private Button mContinue,adminProfile;
    String code = null;

    TextView loginLink;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mContinue = (Button) findViewById(R.id.btnContinue);
        adminProfile= (Button) findViewById(R.id.btnAdminProfile);
        loginLink = (TextView) findViewById(R.id.textView_login_link);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        adminProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,AdminMainActivity.class);
                startActivity(i);
            }
        });

        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmployeeCode = (EditText) findViewById(R.id.editText_employee_code_input);

                mDatabaseReference = mFirebaseDatabase.getReference("Employee");
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = 0;

                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                            Map<String, String> map = (Map<String, String>) postSnapShot.getValue();
                            String counter = map.get("Counter");
                            String id = map.get("ID");
                            //Toast.makeText(MainActivity.this,mEmployeeCode.getText().toString(),Toast.LENGTH_LONG).show();
                            if ("0".equals(counter) && id.equals(mEmployeeCode.getText().toString().trim())) {
                                count = 1;
                                code = id;
                                break;
                            }
                        }
                        if (count == 0) {
                            Toast.makeText(MainActivity.this, "Please enter correct employee code", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "You can now sign up", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, ActivitySignUp.class);
                            intent.putExtra("User_Code", code);
                            Log.v("Usercode:",code);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

}
