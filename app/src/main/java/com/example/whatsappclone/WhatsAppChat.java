package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppChat extends AppCompatActivity implements View.OnClickListener {

    private ListView chatListView;
    private ArrayList<String> chatsList;
    private ArrayAdapter adapter;
    private String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_chat);

        selectedUser = getIntent().getStringExtra("selectedUser");
        findViewById(R.id.sendMessageBtn).setOnClickListener(this);
        chatListView = findViewById(R.id.chatListView);
        chatsList = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chatsList);
        chatListView.setAdapter(adapter);
        final SwipeRefreshLayout swipeRefreshLayoutt = findViewById(R.id.swipe_container2);

        try {
            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

            firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("waTargetRecipient", selectedUser);

            secondUserChatQuery.whereEqualTo("waSender", selectedUser);
            secondUserChatQuery.whereEqualTo("waTargetRecipient", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject chatObject : objects) {
                            String waMessage = chatObject.get("waMessage") + "";
                            if (chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())) {
                                waMessage = ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                            }
                            if (chatObject.get("waSender").equals(selectedUser)) {
                                waMessage = selectedUser + ": " + waMessage;
                            }
                            chatsList.add(waMessage);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        swipeRefreshLayoutt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try{

                    ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
                    ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

                    firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
                    firstUserChatQuery.whereEqualTo("waTargetRecipient", selectedUser);

                    secondUserChatQuery.whereEqualTo("waSender", selectedUser);
                    secondUserChatQuery.whereEqualTo("waTargetRecipient", ParseUser.getCurrentUser().getUsername());

                    ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
                    allQueries.add(firstUserChatQuery);
                    allQueries.add(secondUserChatQuery);
                    ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);



                    myQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (objects.size() > 0 ) {

                                if (e == null) {
                                    chatsList.clear();
                                    for (ParseObject chatObject : objects) {
                                        String waMessage = chatObject.get("waMessage") + "";
                                        if (chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())) {
                                            waMessage = ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                                        }
                                        if (chatObject.get("waSender").equals(selectedUser)) {
                                            waMessage = selectedUser + ": " + waMessage;
                                        }

                                        chatsList.add(waMessage);
                                    }
                                    adapter.notifyDataSetChanged();
                                    if (swipeRefreshLayoutt.isRefreshing()) {
                                        swipeRefreshLayoutt.setRefreshing(false);
                                    }
                                }else{
                                    if (swipeRefreshLayoutt.isRefreshing()) {
                                        swipeRefreshLayoutt.setRefreshing(false);
                                    }
                                }

                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



    }

    @Override
    public void onClick(View view) {
        final EditText editMeassage = findViewById(R.id.messageEdt);

        ParseObject chat = new ParseObject("Chat");
        chat.put("waSender", ParseUser.getCurrentUser().getUsername());
        chat.put("waTargetRecipient", selectedUser);
        chat.put("waMessage", editMeassage.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    chatsList.add(ParseUser.getCurrentUser().getUsername() + ": " + editMeassage.getText().toString());
                    adapter.notifyDataSetChanged();
                    editMeassage.setText("");
                }
            }
        });

    }
}