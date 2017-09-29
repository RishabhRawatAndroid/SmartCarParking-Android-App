package com.example.rishabh.smartcarparking;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScanBarCodeService extends Service {

    DatabaseReference proot;
    DatabaseReference troot;
    Handler handler=new Handler();
    public ScanBarCodeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
          super.onCreate();
        SharedPrefrenceData data=new SharedPrefrenceData(ScanBarCodeService.this);
        proot= FirebaseDatabase.getInstance().getReference().getRoot().child("PARKINGNODE").child(data.getrandomcode());
        troot= FirebaseDatabase.getInstance().getReference().getRoot().child("TEMPNODE").child(data.getrandomcode());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        SharedPrefrenceData data=new SharedPrefrenceData(ScanBarCodeService.this);
//        proot= FirebaseDatabase.getInstance().getReference().getRoot().child("PARKINGNODE");
//        troot= FirebaseDatabase.getInstance().getReference().getRoot().child("TEMPNODE");

        handler.post(new Runnable(){
            public void run(){
                BACKGROUNDWORK();
            }
        });

        return START_STICKY;
    }

    private void BACKGROUNDWORK() {

        while (true)
        {
            proot.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Intent intent=new Intent(ScanBarCodeService.this,ClientSideInfoActivity.class);
                    intent.putExtra("Checkinfo",true);
                    startActivity(intent);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            troot.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Intent intent=new Intent(ScanBarCodeService.this,ClientSideInfoActivity.class);
                    intent.putExtra("Checkinfo",false);
                    startActivity(intent);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
