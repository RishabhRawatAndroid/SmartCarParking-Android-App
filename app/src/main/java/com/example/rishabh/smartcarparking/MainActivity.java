package com.example.rishabh.smartcarparking;

        import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
        import android.util.Log;
        import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //DatabaseReference rootnode= FirebaseDatabase.getInstance().getReference().getRoot();

    EditText name,email,phoneno,carplate;
    TextView userid;
    Button loginbtn;
    String random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=(EditText)findViewById(R.id.nametext);
        email=(EditText)findViewById(R.id.emailtext);
        phoneno=(EditText)findViewById(R.id.phonetext);
        carplate=(EditText)findViewById(R.id.carnametext);
        userid=(TextView)findViewById(R.id.idtext);
        loginbtn=(Button)findViewById(R.id.loginbtn);

        SharedPrefrenceData data1=new SharedPrefrenceData(MainActivity.this);
        //This will create a random unique id for every user
        //random=RandomStringGenerator.generateRandomString();
         random=data1.getrandomcode();
         Log.e("RISHABH",String.valueOf(random));

        userid.setText(random);

        SharedPrefrenceData data=new SharedPrefrenceData(MainActivity.this);
        data.setrandomcode(random);
        if(!data.getTag()) {
        }
        else
        {
            Intent intent = new Intent(MainActivity.this, BarCodeActivity.class);
            startActivity(intent);
        }


    }

    public void LoginButtonMethod(View view)
    {

        SharedPrefrenceData data=new SharedPrefrenceData(MainActivity.this);
        if(!data.getTag()) {
            //let's us suppose if user forget to type anything
            if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(phoneno.getText().toString()) || TextUtils.isEmpty(carplate.getText().toString())) {
                Toast.makeText(this, "Please enter all Details", Toast.LENGTH_SHORT).show();
            } else {

                DatabaseReference usernode = FirebaseDatabase.getInstance().getReference().child("USERDATANODE");
                DatabaseReference parkingnode = FirebaseDatabase.getInstance().getReference().child("PARKINGNODE");
                Map<String, Object> useruniqueID = new HashMap<String, Object>();
                useruniqueID.put(random, "");
                usernode.updateChildren(useruniqueID);
                // parkingnode.updateChildren();

                DatabaseReference userinfo = usernode.child(random);
                Map<String, Object> userdetails = new HashMap<String, Object>();
                userdetails.put("NAME", name.getText().toString());
                userdetails.put("EMAIL", email.getText().toString());
                userdetails.put("PHONENO", phoneno.getText().toString());
                userdetails.put("CARPLATE", carplate.getText().toString());
                userinfo.updateChildren(userdetails);
                Toast.makeText(this, "Successfully inserted data", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, BarCodeActivity.class);
                intent.putExtra("RANDOM", random);
                startActivity(intent);
            }
        }
        else
        {
            Intent intent=new Intent(MainActivity.this,BarCodeActivity.class);
            startActivity(intent);
        }
    }
}
