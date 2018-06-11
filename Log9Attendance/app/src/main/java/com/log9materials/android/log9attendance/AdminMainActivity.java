package com.log9materials.android.log9attendance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vrinda on 16-01-2018.
 */

public class AdminMainActivity extends AppCompatActivity {

    FirebaseDatabase mEmployeeDatabase;
    DatabaseReference mEmployeeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        mEmployeeDatabase = FirebaseDatabase.getInstance();
    }

    public void AddNewEmployee(View view) {

        EditText mEmployeeID = (EditText) findViewById(R.id.adminEmployeeCode);

        String IDs = mEmployeeID.getText().toString();
        mEmployeeRef = mEmployeeDatabase.getReference("Employee").child(IDs);
        mEmployeeRef.child("Name").setValue("");
        mEmployeeRef.child("Email").setValue("");
        mEmployeeRef.child("Age").setValue("");
        mEmployeeRef.child("Contact").setValue("");
        mEmployeeRef.child("Counter").setValue("0");
        mEmployeeRef.child("ID").setValue(IDs);

        mEmployeeRef = mEmployeeDatabase.getReference("Attendance");
        mEmployeeRef.child(IDs).child("ID").setValue(IDs);

        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
    }

}
