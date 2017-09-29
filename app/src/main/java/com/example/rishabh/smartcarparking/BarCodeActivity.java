package com.example.rishabh.smartcarparking;

        import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class BarCodeActivity extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stopService(new Intent(BarCodeActivity.this,ScanBarCodeService.class));
    }

    ImageView image;
    TextView text;

    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

        text=(TextView)findViewById(R.id.textView);
        image=(ImageView)findViewById(R.id.imageView);


        //startService(new Intent(BarCodeActivity.this,ScanBarCodeService.class));

        SharedPrefrenceData data=new SharedPrefrenceData(BarCodeActivity.this);

        if(!data.getTag()) {
            Intent intent = this.getIntent();
            String random = intent.getStringExtra("RANDOM");
            text.setText(random);

            Bitmap qrcode = net.glxn.qrgen.android.QRCode.from(random).bitmap();
            image.setImageBitmap(qrcode);

//            int bytes = qrcode.getByteCount();
//            ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
//            qrcode.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
//            byte[] array = buffer.array(); //Get the underlying array containing the data.

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            qrcode.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byte[] array= stream.toByteArray();


            ImageSqliteDatabase database=new ImageSqliteDatabase(BarCodeActivity.this);
            database.opendatabase();
            long check=database.insertImage(array,random);
            if(check<0)
            {
                Toast.makeText(this, "ERROR UPLOADING DATA", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "RISHABH YOUR DATA SAVED", Toast.LENGTH_SHORT).show();
                database.closedatabase();
            }

            data.setTag(true);

            Toast.makeText(this, "DATA INSERT SUCCESSFULLY", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ImageSqliteDatabase database=new ImageSqliteDatabase(BarCodeActivity.this);
            database.opendatabase();
            byte[] imagebyte =database.getimage();
            Bitmap bmp= BitmapFactory.decodeByteArray(imagebyte,0,imagebyte.length);
            image.setImageBitmap(bmp);

            String randstr=database.getID();
            text.setText(randstr);

//            Cursor cur=database.Getimage(randstr);
//            if(cur.moveToFirst())
//            {
//                byte[] mydata=cur.getBlob(1);
//                Bitmap bmp= BitmapFactory.decodeByteArray(mydata,0,mydata.length);
//                image.setImageBitmap(bmp);
//            }
            database.closedatabase();
        }


    }
}
