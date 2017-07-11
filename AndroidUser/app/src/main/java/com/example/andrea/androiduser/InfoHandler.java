package com.example.andrea.androiduser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Map;

/**
 * Created by Andrea on 31/05/2017.
 */

public class InfoHandler {

    public static String HOST = "10.87.251.254:7070";

    public static String USERLOGIN_API = "http://"+HOST+"/ticket/webapi/secured/user/login/";

    public static String BUY_TICKET_API = "http://"+HOST+"/ticket/webapi/secured/user/buy/";

    public static String MY_TICKETS_API = "http://"+HOST+"/ticket/webapi/secured/user/mytickets/";

    public static String MY_VALID_TICKETS_API = "http://"+HOST+"/ticket/webapi/secured/user/myvalidtickets/";

    public static String REGISTRATION_API = "http://"+HOST+"/ticket/webapi/registration/";

    public static String TYPES_API = "http://"+HOST+"/ticket/webapi/types/";




    public InfoHandler(){
    }

    public static void saveLogin(Context myContext, String username, String password){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.apply();
    }

    public static String getRootUrl(Context myContext){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        return sharedPref.getString("username","");
    }

    public static String getUsername(Context myContext){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        return sharedPref.getString("username","");
    }

    public static String getPassword(Context myContext){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        return sharedPref.getString("password","");
    }

    public static void setUsername(Context myContext,String username){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username",username);
        editor.apply();
    }

    public static void setPassword(Context myContext,String password){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("password",password);
        editor.apply();
    }

    public static void logout(Context myContext){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username","");
        editor.putString("password","");
        editor.apply();
    }

    public static void updateHOST(String configuration) {
        HOST = configuration;
        USERLOGIN_API = "http://"+HOST+"/ticket/webapi/secured/user/login/";
        BUY_TICKET_API = "http://"+HOST+"/ticket/webapi/secured/user/buy/";
        MY_TICKETS_API = "http://"+HOST+"/ticket/webapi/secured/user/mytickets/";
        MY_VALID_TICKETS_API = "http://"+HOST+"/ticket/webapi/secured/user/myvalidtickets/";
        REGISTRATION_API = "http://"+HOST+"/ticket/webapi/registration/";
        TYPES_API = "http://"+HOST+"/ticket/webapi/types/";
    }
}
