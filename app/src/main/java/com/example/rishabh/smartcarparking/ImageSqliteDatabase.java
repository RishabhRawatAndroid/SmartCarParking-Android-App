package com.example.rishabh.smartcarparking;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.widget.Toast;

public class ImageSqliteDatabase {

    static final String ID="Id";
    static final String IMAGE="Image";

    static final String DATABASE_NAME="MyBarCodeDb";
    static final int DATABASE_VERSION=1;
    static final String DATABASE_TABLE="BarCodeImage";

    static final String DATABASE_CREATE="create table BarCodeImage(Id text primary key,Image BLOB not null);";

    //Create context and Sqlite database
    Context context;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelperClass databasehelperclass;


    public ImageSqliteDatabase(Context context)
    {
        this.context=context;
        databasehelperclass=new DatabaseHelperClass(context);

    }


    class DatabaseHelperClass extends SQLiteOpenHelper
    {
        public DatabaseHelperClass(Context context) {
            super(context, DATABASE_NAME,null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            }
            catch (Exception ex)
            {
                Toast.makeText(context, "SOME ERROR IS OCCURED WHILE CREATING DATABASE", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXITS Contact");
            onCreate(db);
        }
    }


    public ImageSqliteDatabase opendatabase() throws SQLException
    {
        sqLiteDatabase=databasehelperclass.getWritableDatabase();
        return this;
    }

    public void closedatabase()
    {
        databasehelperclass.close();
    }



    public long insertImage(byte[] imageBytes,String Id) {
        ContentValues cv = new ContentValues();
        cv.put(ID,Id);
        cv.put(IMAGE, imageBytes);
        return sqLiteDatabase.insert(DATABASE_TABLE, null, cv);
    }


    public byte[] getimage()
    {
        Cursor cursor=sqLiteDatabase.rawQuery("select * from BarCodeImage",null);
        byte[] image=null;
        if(cursor.moveToFirst())
        {
            image=cursor.getBlob(1);
            return image;
        }
        return image;
    }

    public Cursor Getimage(String id)
    {
        Cursor cursor=sqLiteDatabase.query(true,DATABASE_TABLE,new String[]{ID,IMAGE},ID+"="+id,null,null,null,null,null);
        return cursor;
    }


    public String getID()
    {
        Cursor cursor=sqLiteDatabase.rawQuery("select * from BarCodeImage",null);
        String rand=null;
        if(cursor.moveToFirst())
        {
            rand=cursor.getString(0);
            return rand;
        }
        return rand;
    }
}
