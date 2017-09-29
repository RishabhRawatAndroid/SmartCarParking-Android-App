package com.example.rishabh.smartcarparking;
import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefrenceData {

    SharedPreferences preferences;  //read data
    SharedPreferences.Editor editor;  //write data

    SharedPrefrenceData(Context context)
    {
        preferences=context.getSharedPreferences("Myfile",0);
        editor=preferences.edit();
    }

    public void setTag(Boolean tag)
    {
        editor.putBoolean("TAG",tag);
        editor.commit();
    }

    public void setTime(String time)
    {
        editor.putString("TIME",time);
        editor.commit();
    }

    public String getTime()
    {
        return  preferences.getString("TIME",null);
    }

    public void setrandomcode(String code)
    {
        editor.putString("CODE",code);
        editor.commit();
    }

    public Boolean getTag()
    {
        return preferences.getBoolean("TAG",false);
    }

    public String getrandomcode()
    {
        return preferences.getString("CODE",null);
    }
}
