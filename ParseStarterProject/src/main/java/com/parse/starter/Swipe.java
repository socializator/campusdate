package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.GetCallback;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Swipe extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_swipe, menu);
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

    public void go_to_profile(View view) {
        Intent intent = new Intent(this, ProfilePageActivity.class);
        startActivity(intent);
    }

    public void go_to_matches(View view) {
        Intent intent = new Intent(this, Matches.class);
        startActivity(intent);
    }

    public void go_to_swipe(View view) {
        Intent intent = new Intent(this, Swipe.class);
        startActivity(intent);
    }

    public void get_data(View view) {
        final ArrayList<String> seen_list = new ArrayList<String>();
        final ArrayList<String> result_list = new ArrayList<String>();

        ParseQuery<ParseObject> users_seen_query = ParseQuery.getQuery("Profile");

        //users_seen_query.whereEqualTo("Me", ParseUser.getCurrentUser().getObjectId());
        users_seen_query.whereEqualTo("Me", "r22mHwUeTu");

        users_seen_query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    for (Object s : object.getList("users_seen")) {
                        seen_list.add(s.toString());
                    }

                    //ParseQuery<ParseUser> users_list_query = ParseUser.getQuery();
                    ParseQuery<ParseObject> users_list_query = ParseQuery.getQuery("Profile");

                    //users_list_query.whereEqualTo("email_domain", ParseUser.getCurrentUser().get("email_domain"));
                    users_list_query.whereEqualTo("email_domain", "hotmail");

                    /*
                    if (ParseUser.getCurrentUser().get("IntrestedInFemale") == true) {
                        users_list_query.whereEqualTo("gender", "female");

                    }
                    if (ParseUser.getCurrentUser().get("IntrestedInMale") == true) {

                        users_list_query.whereEqualTo("gender", "male");
                    }
                    */
                    users_list_query.whereEqualTo("Gender", "male");

                    users_list_query.whereNotContainedIn("Me", seen_list);

                    //users_list_query.whereNotEqualTo("Me", ParseUser.getCurrentUser().getObjectId());

                    users_list_query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> userlist, ParseException e) {
                            if (e == null) {
                                for (ParseObject s : userlist) {
                                    result_list.add(s.getObjectId());
                                }
                                for (String s : result_list) {
                                    System.out.println(s);
                                }
                            } else {
                                System.out.println("FAIL!");
                            }
                        }
                    });
                } else {
                    System.out.println("FAIL!");
                }
            }
        });
    }
}