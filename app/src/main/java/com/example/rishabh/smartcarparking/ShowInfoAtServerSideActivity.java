package com.example.rishabh.smartcarparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ShowInfoAtServerSideActivity extends AppCompatActivity {

    String name,email,phone,plate,time;
    TextView uname,uemail,uphone,uplate,utime,umoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_at_server_side);
        Intent intent=this.getIntent();

        name=intent.getStringExtra("NAME");
        email=intent.getStringExtra("EMAIL");
        phone=intent.getStringExtra("PHONE");
        plate=intent.getStringExtra("PLATE");
        time=intent.getStringExtra("TIME");

        uname=(TextView)findViewById(R.id.drivername);
        uemail=(TextView)findViewById(R.id.driveremail);
        uphone=(TextView)findViewById(R.id.driverphone);
        uplate=(TextView)findViewById(R.id.drivercar);
        utime=(TextView)findViewById(R.id.drivertime);
        umoney=(TextView)findViewById(R.id.drivermoney);

        SharedPrefrenceData data=new SharedPrefrenceData(ShowInfoAtServerSideActivity.this);
        if(intent.getBooleanExtra("Checking",true)) {
            uname.setText(name);
            uemail.setText(email);
            uphone.setText(phone);
            uplate.setText(plate);
            utime.setText(time);
            data.setTime(time);
        }
        else
        {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            uname.setText(name);
            uemail.setText(email);
            uphone.setText(phone);
            uplate.setText(plate);
            utime.setText("Parking in time "+data.getTime()+"\nParking out time "+sdf.format(cal.getTime()));
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date date1 = null;
            try {
                date1 = format.parse(data.getTime().toString());
            } catch (ParseException e) {
                Toast.makeText(this, "Time Wrong", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Date date2 = null;
            try {
                date2 = format.parse(sdf.format(cal.getTime()));
            } catch (ParseException e) {
                Toast.makeText(this, "Time Wrong", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            final long difference = (date2.getTime() - date1.getTime())/ 1000 % 60;
            umoney.setText("TOTAL MONEY IS "+difference*0.05+"₹");

            final DatabaseReference troot= FirebaseDatabase.getInstance().getReference().getRoot().child("TEMPNODE").child(data.getrandomcode());
            troot.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String,Object> map=new HashMap<>();
                    map.put("RESULTTIME",difference);
                    troot.updateChildren(map);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
