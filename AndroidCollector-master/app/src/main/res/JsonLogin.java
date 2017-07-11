package com.ticket.iseimoschettieri.tickettestagain;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

/**
 * Created by Francesco on 09/05/2017.
 */

public class JsonLogin extends AsyncTask<String,Void,Void> {

    private final String JsonMethod = "LOGIN";

    private String answerString = null;

    JSONObject login = null;

    JSONObject data = null;

    JSONObject answer = null;

    String loginSuccess = null;

    Socket clientSocket = null;

    Socket serverSocket = null;

    Socket socket = null;

    PrintWriter out;

    BufferedReader in;

    protected void doInBackground(String... args){

        clientSocket = new Socket("10.65.11.112",5000);

        login = new JSONObject();

        login.put("method",JsonMethod);

        data = new JSONObject();

        data.put("username",args[0]);

        data.put("password",args[1]);

        login.put("data",data);

        out = new PrintWriter(clientSocket.getOutputStream(),true);

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        out.println(login.toString());

        answerString = in.readLine();

        answer = new JSONObject(answerString);

        Toast.makeText(getActivity(), answer.toString(), Toast.LENGTH_LONG).show();

        clientSocket.close();

    }

}
