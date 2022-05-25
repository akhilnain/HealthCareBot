package com.example.healthcarebot;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ChatArrayAdapter chatArrayAdapter;
    EditText chatText;
    ListView chatList;
    Button sendbtn;
    Button begin;
    String  Authorization="";
    String Recieved="";
    Context mContext=this;
    int contentview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startingv2);

        contentview=R.layout.startingv2;
        begin =(Button) findViewById(R.id.begin);
        begin.setOnClickListener(arg0 -> {
            setContentView(R.layout.activity_main);
            contentview=R.layout.activity_main;
            int v=R.layout.activity_main;
            chatList = (ListView) findViewById(R.id.chatlist);
            chatText = (EditText) findViewById(R.id.msgtyped);
            changeview();
            chatArrayAdapter = new ChatArrayAdapter(mContext, new ArrayList<>(),chatList);
            chatList.setAdapter(chatArrayAdapter);
            chatArrayAdapter.add(new ChatMessage(1,"typing..."));
            //  chatArrayAdapter.getItem(0).setIsMine(1);
            new Welcome(mContext).execute();
        });

    }
    public void changeview(){
        sendbtn = (Button) findViewById(R.id.send_btn);
        sendbtn.setOnClickListener(arg0 -> {
            String message = chatText.getText().toString();
            ChatMessage chatMessage = new ChatMessage(0, message);

            chatArrayAdapter.add(chatMessage);

            JSONObject jsn=new JSONObject();
            try {
                jsn.put("message",message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new Chat(mContext).execute(Authorization,jsn.toString());
            chatText.setText("");
            chatArrayAdapter.add(new ChatMessage(1,"typing..."));
            chatList.setSelection(chatArrayAdapter.getCount() - 1);
        });
    }
    public void ServerWelcome(JSONObject res){
        try {
            Recieved=(res.getString("message"));
            Authorization=res.getString("uuid");
            chatArrayAdapter.remove((ChatMessage) chatArrayAdapter.chatList.getItemAtPosition(chatArrayAdapter.getCount()-1));
            chatArrayAdapter.notifyDataSetChanged();

            ChatMessage chatMessage = new ChatMessage(1, Recieved);
            chatArrayAdapter.add(chatMessage);
            chatList.setSelection(chatArrayAdapter.getCount() - 1);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    //  this method recieves the server response
    public void ServerChat(JSONObject res){
        try {
            Recieved=(res.getString("message"));
            Log.d("nfndskjfnjkdsn   ",Recieved);
            chatArrayAdapter.remove((ChatMessage) chatArrayAdapter.chatList.getItemAtPosition(chatArrayAdapter.getCount()-1));
            chatArrayAdapter.notifyDataSetChanged();

            ChatMessage chatMessage = new ChatMessage(1, Recieved);
            chatArrayAdapter.add(chatMessage);
            chatList.setSelection(chatArrayAdapter.getCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}