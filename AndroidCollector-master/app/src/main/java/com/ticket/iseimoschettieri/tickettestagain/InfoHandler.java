package com.ticket.iseimoschettieri.tickettestagain;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import java.util.StringTokenizer;

/**
 * Created by Andrea on 31/05/2017.
 */

public class InfoHandler {

    public static String HOST = "192.168.1.4:8080";

    public static String COLLECTORLOGIN_API = "http://"+HOST+"/ticket/webapi/secured/collector/login";

    public static String CHECK_TICKET_API = "http://"+HOST+"/ticket/webapi/secured/collector/tickets/";

    public static String MAKE_FINE_API = "http://"+HOST+"/ticket/webapi/secured/collector/fine/";

    public static String ASK_STARTING_NUMBER = "http://"+HOST+"/ticket/webapi/secured/collector/startnumber/";

    public static String PING = "http://"+HOST+"/ticket/webapi/ping/";

    public InfoHandler(){
    }

    public static void saveLogin(Context myContext, String username, String password){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putString("offlineFine", "");
        editor.apply();
    }

    public static String getRootUrl(Context myContext){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        return sharedPref.getString("username","");
    }

    public static synchronized String getUsername(Context myContext){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        return sharedPref.getString("username","");
    }

    public static synchronized String getPassword(Context myContext){
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

    public static void setStartingNumber(Context myContext,Long l){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("startingNumber",l.toString());
        editor.apply();
    }

    public static void incrementStartingNumber(Context myContext){
        setStartingNumber(myContext, getStartingNumber(myContext) +1);
    }

    public static Long getStartingNumber(Context myContext){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        return Long.valueOf(sharedPref.getString("startingNumber",""));
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
        COLLECTORLOGIN_API = "http://"+HOST+"/ticket/webapi/secured/collector/login";
        CHECK_TICKET_API = "http://"+HOST+"/ticket/webapi/secured/collector/tickets/";
        MAKE_FINE_API = "http://"+HOST+"/ticket/webapi/secured/collector/fine/";
        ASK_STARTING_NUMBER = "http://"+HOST+"/ticket/webapi/secured/collector/startnumber/";
        PING = "http://"+HOST+"/ping/";
    }

    public static void addOfflineFine(Context myContext,String fine){       //fine esempio: codicefiscale/ammontaremulta già attaccati
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        StringBuilder sb = new StringBuilder();

        if(sharedPref.getString("offlineFine","").equals("")==true){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("offlineFine",fine);
            editor.apply();
        }else{
            sb.append(InfoHandler.getAllOfflineFines(myContext));
            sb.append(",").append(fine);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("offlineFine",sb.toString());
            editor.apply();
        }
    }

    public static String getAllOfflineFines(Context myContext){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        return sharedPref.getString("offlineFine","");
    }

    public static boolean isOfflineFineEmpty(Context myContext){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        return sharedPref.getString("offlineFine","").equals("");
    }

    public static String getOneOfflineFine(Context myContext){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        String offlineFines = InfoHandler.getAllOfflineFines(myContext);
        InfoHandler.resetOfflineFines(myContext);
        StringTokenizer tokenizer = new StringTokenizer(offlineFines, ",");
        Log.i("TOKEN ",tokenizer.countTokens()+"");
        String stringToReturn=tokenizer.nextToken();

        while(tokenizer.hasMoreTokens()){
            addOfflineFine(myContext,tokenizer.nextToken());
        }

        return stringToReturn;
    }

    public static int getOfflineFineSize(Context myContext){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        String offlineFines = InfoHandler.getAllOfflineFines(myContext);
        StringTokenizer tokenizer = new StringTokenizer(offlineFines, ",");

        return tokenizer.countTokens();
    }



    public static void resetOfflineFines(Context myContext){
        SharedPreferences sharedPref = myContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("offlineFine","");
        editor.apply();
    }


}
