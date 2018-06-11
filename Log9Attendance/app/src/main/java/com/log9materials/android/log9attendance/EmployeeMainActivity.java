package com.log9materials.android.log9attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * Created by vrinda on 15-01-2018.
 */

public class EmployeeMainActivity extends AppCompatActivity {

    private Button btnSignOut,  mMarkAttendance, mgetDate;

    private FirebaseAuth auth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef, myRefAttendance;

    private int mYear;
    private int mMonth;
    private int mDay;

    private TextView dateDisplayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main_activity);

        mRef = mDatabase.getInstance().getReference("Mail");
        myRefAttendance = mDatabase.getInstance().getReference("Attendance");

        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        mMarkAttendance = (Button) findViewById(R.id.btnMarkAttendance);
        auth = FirebaseAuth.getInstance();
        mgetDate = (Button) findViewById(R.id.getDate);
        dateDisplayTextView = (TextView) findViewById(R.id.textView_Date_display);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(EmployeeMainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mgetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentDateTimeForCheck();
            }
        });

        mMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(EmployeeMainActivity.this,"YES",Toast.LENGTH_SHORT).show();
                String str = getIntent().getStringExtra("EmailIdOfEmployee");
                str = editEmail(str);
                //Log.v("MyStr",str);
                mRef = mRef.child(str);
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String myCode = (String) dataSnapshot.getValue();
                        String myDate = pickCurrentDate();
                        Log.v("Mycode", myCode);
                        Log.v("Mydate", myDate);
                        myRefAttendance.child(myCode).child(myDate).setValue("P");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private String pickCurrentDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        String sbd = "";
        sbd += String.valueOf(mYear) + String.valueOf(mMonth + 1) + String.valueOf(mDay);
        return sbd;
    }

    private void setCurrentDateTimeForCheck() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        StringBuilder sbd = new StringBuilder();
        sbd.append(mYear).append(mMonth + 1).append(mDay);
        dateDisplayTextView.setText(sbd.toString());
    }

    @Override
    public void onBackPressed() {
        auth.signOut();
        Toast.makeText(this, "SignedOut", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EmployeeMainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public String editEmail(String s) {

        if (s == null || s.trim().isEmpty()) {
            System.out.println("Incorrect format of string");
            return null;
        }
        int len = s.length();
        String newS = "";
        int i;
        for (i = 0; i < len; i++) {
            int ascii = (int) s.charAt(i);
            if ((ascii >= 65 && ascii <= 90) || (ascii >= 97 && ascii <= 122) || (ascii >= 48 && ascii <= 57)) {
                newS += s.charAt(i);
            } else {
                newS += '_';
            }
        }
        return newS;
    }

}