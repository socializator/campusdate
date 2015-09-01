package com.parse.starter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sinch.android.rtc.messaging.WritableMessage;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MessagingActivity extends Activity {

    private String recipientId;
    private EditText messageBodyField;
    private String messageBody;
    private MessageAdapter messageAdapter;
    private ListView messagesList;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar bar = getActionBar();
        bar.setIcon(android.R.color.transparent);
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03A9F4")));
        bar.setTitle(Html.fromHtml("<font color='#ffffff'>Campusdate</font>"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);


        Intent intent = getIntent();
        recipientId = intent.getStringExtra("RECIPIENT_ID");
        currentUserId = ParseUser.getCurrentUser().getObjectId();

        messagesList = (ListView) findViewById(R.id.listMessages);
        messageAdapter = new MessageAdapter(this);
        messagesList.setAdapter(messageAdapter);
        actionBarSetup(recipientId);
        populateMessageHistory();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");
                query.whereNotEqualTo("new", false);
                query.whereNotEqualTo("senderId", currentUserId);
                query.orderByAscending("createdAt");
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> list, com.parse.ParseException e) {
                        if (e == null) {
                            for (ParseObject o : list) {
                                WritableMessage message = new WritableMessage(o.get("recipientId").toString(), o.get("messageText").toString());
                                message.addHeader("date", currentTimeString(o.getCreatedAt()));
                                messageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
                                o.put("new", false);
                                o.saveInBackground();
                            }
                        }
                    }
                });
                //messageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
            }
        }, 2000, 2000);//put here time 1000 milliseconds=1 second

        messageBodyField = (EditText) findViewById(R.id.messageBodyField);

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    //get previous messages from parse & display
    private void populateMessageHistory() {
        final String[] userIds = {currentUserId, recipientId};
        ParseQuery<ParseObject> message_query = ParseQuery.getQuery("ParseMessage");
        message_query.whereContainedIn("senderId", Arrays.asList(userIds));
        message_query.whereContainedIn("recipientId", Arrays.asList(userIds));
        message_query.orderByAscending("createdAt");
        message_query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messageList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < messageList.size(); i++) {
                        messageList.get(i).put("new", false);
                        messageList.get(i).saveInBackground();
                        WritableMessage message = new WritableMessage(messageList.get(i).get("recipientId").toString(), messageList.get(i).get("messageText").toString());
                        message.addHeader("date", currentTimeString(messageList.get(i).getCreatedAt()));
                        if (messageList.get(i).get("senderId").toString().equals(currentUserId)) {
                            messageAdapter.addMessage(message, MessageAdapter.DIRECTION_OUTGOING);
                        } else {
                            messageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
                        }
                    }
                }
            }
        });
    }

    private void sendMessage() {
        messageBody = messageBodyField.getText().toString();
        if (messageBody.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_LONG).show();
            return;
        } else {
            final WritableMessage writableMessage = new WritableMessage(recipientId, messageBody);

            //messageService.sendMessage(recipientId, messageBody);

            ParseObject parseMessage = new ParseObject("ParseMessage");
            parseMessage.put("senderId", currentUserId);
            parseMessage.put("recipientId", recipientId);
            parseMessage.put("messageText", messageBody);
            parseMessage.put("new", true);
            parseMessage.put("MessageId", writableMessage.getMessageId());
            parseMessage.saveInBackground();

            writableMessage.addHeader("date", currentTimeString());
            messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_OUTGOING);
            messageBodyField.setText("");
        }
    }

    public String currentTimeString() {
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat("MMM dd HH:mm a");
        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return df.format(currentTime);
    }

    public String currentTimeString(Date date) {
        Date currentTime = date;
        SimpleDateFormat df = new SimpleDateFormat("MMM dd HH:mm a");
        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return df.format(currentTime);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_messaging, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void actionBarSetup(String Id) {
        final String [] result = new String[3];
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo("user_object_id", Id);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    result[0] = object.getString("first_name");
                    result[1] = object.getString("last_name");
                    result[2] = object.getString("whats_up");
                    ActionBar ab = getActionBar();
                    ab.setTitle(result[0] + " "+ result[1]);
                    ab.setSubtitle(result[2]);
                }
            }
        });
    }
}