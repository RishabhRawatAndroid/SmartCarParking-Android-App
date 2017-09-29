package com.example.rishabh.smartcarparking;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerClientApp extends AppCompatActivity {

    Button serverbtn,clientbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_client_app);

        serverbtn=(Button)findViewById(R.id.serverbtn);
        clientbtn=(Button)findViewById(R.id.clientbtn);


        clientbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ServerClientApp.this,MainActivity.class);
                startActivity(intent);
            }
        });
        serverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator=new IntentIntegrator(ServerClientApp.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("SCAN");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "SCANNING FAILED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                SendDatatoPakingNode(result.getContents());

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void SendDatatoPakingNode(final String data)
    {
        //data show the scan code string

        final DatabaseReference uroot= FirebaseDatabase.getInstance().getReference().getRoot().child("USERDATANODE");
        final DatabaseReference proot= FirebaseDatabase.getInstance().getReference().getRoot().child("PARKINGNODE");

        //lets us suppose the user id not match with the qr code it means qr code is wrong
        uroot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(data))
                {
                    Toast.makeText(ServerClientApp.this, "This QR Code is WRONG", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(this, "PASS UROOT", Toast.LENGTH_SHORT).show();


        //now checking the parking node if user is present in parking node it means user car is stay in parking area
        proot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(data))
                {
                    uroot.child(data).addListenerForSingleValueEvent(new ValueEventListener() { //find out particular user and reterive data
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator i=dataSnapshot.getChildren().iterator();
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            while (i.hasNext())
                            {
                                String plate =(String)((DataSnapshot)i.next()).getValue();  //retrive data from usernode
                                String email=(String)((DataSnapshot)i.next()).getValue();   //retrive data from usernode
                                String name=(String)((DataSnapshot)i.next()).getValue();    //retrive data from usernode
                                String phone=(String)((DataSnapshot)i.next()).getValue();   //retrive data from usernode

                                Map<String,Object> map=new HashMap<>();   //putting these data into hashmap
                                map.put("PLATE",plate);
                                map.put("EMAIL",email);
                                map.put("NAME",name);
                                map.put("PHONE",phone);
                                map.put("TIME",sdf.format(cal.getTime()));
                                proot.child(data).updateChildren(map);//finally adding userdata to the parking node


                                //we create a intent which takes the data from cloud and send to the showinfo activity which is show in server side
                                Intent intent=new Intent(ServerClientApp.this,ShowInfoAtServerSideActivity.class);
                                intent.putExtra("NAME",name);
                                intent.putExtra("EMAIL",email);
                                intent.putExtra("PHONE",phone);
                                intent.putExtra("PLATE",plate);
                                intent.putExtra("TIME",sdf.format(cal.getTime()));
                                intent.putExtra("Checking",true);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    proot.child(data).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String time=null;
                            Iterator i=dataSnapshot.getChildren().iterator();
                            while(i.hasNext())
                            {
                                String plate =(String)((DataSnapshot)i.next()).getValue();  //retrive data from parkingnode
                                String email=(String)((DataSnapshot)i.next()).getValue();   //retrive data from parkingnode
                                String name=(String)((DataSnapshot)i.next()).getValue();    //retrive data from parkingnode
                                String phone=(String)((DataSnapshot)i.next()).getValue();   //retrive data from parkingnode
                                time=(String)((DataSnapshot)i.next()).getValue();

                                Log.e("RISHABHPLATE",plate);
                                Log.e("RISAHBHEMAIL",email);
                                Log.e("RISHABHNAME",name);
                                Log.e("RISHABHPhone",phone);

                                Intent intent=new Intent(ServerClientApp.this,ShowInfoAtServerSideActivity.class);
                                intent.putExtra("NAME",name);
                                intent.putExtra("EMAIL",email);
                                intent.putExtra("PHONE",phone);
                                intent.putExtra("PLATE",plate);
                                intent.putExtra("TIME",time);
                                intent.putExtra("Checking",false);
                                startActivity(intent);

                                final DatabaseReference troot= FirebaseDatabase.getInstance().getReference().getRoot().child("TEMPNODE");
                                troot.child(data).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Map<String,Object> map=new HashMap<>();
                                        map.put("DATA",String.valueOf("NOTIFICATIONALERT"));
                                        troot.updateChildren(map);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                proot.child(data).removeValue();
                            }

                            //proot.setValue(null);
                           // Toast.makeText(ServerClientApp.this, String.format("Your Total Cost is "+50), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
