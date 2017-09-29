package com.example.rishabh.smartcarparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClientSideInfoActivity extends AppCompatActivity {

    TextView textmessage;
    Button btnok;
    DatabaseReference proot;
    DatabaseReference troot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_side_info);



        textmessage=(TextView)findViewById(R.id.message);
        btnok=(Button)findViewById(R.id.buttonok);

        SharedPrefrenceData data=new SharedPrefrenceData(getApplicationContext());
        Intent intent=this.getIntent();
        if(intent.getBooleanExtra("Checkinfo",true)) {
            proot = FirebaseDatabase.getInstance().getReference().getRoot().child("PARKINGNODE").child(data.getrandomcode());

            proot.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String time = String.valueOf(dataSnapshot.child("TIME").getValue());
                    textmessage.setText(String.format("Hello you are in Parking mode.You recently check your own QR code in Parking+\n+  YOUR PARKING TIME START AT : " + time));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            troot = FirebaseDatabase.getInstance().getReference().getRoot().child("TEMPNODE").child(data.getrandomcode());
            troot.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long time=Long.parseLong(String.valueOf(dataSnapshot.child("RESULTTIME").getValue()));
                    textmessage.setText(String.format("Hello you are in Parking mode.You recently check your own QR code in Parking+\n+  Your Total cost is  : " + time*20));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefrenceData data=new SharedPrefrenceData(getApplicationContext());
                troot = FirebaseDatabase.getInstance().getReference().getRoot().child("TEMPNODE").child(data.getrandomcode());
                troot.setValue(null);
                finish();
            }
        });
    }
}
