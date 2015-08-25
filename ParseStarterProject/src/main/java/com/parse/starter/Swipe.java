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
/*
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        //query.whereEqualTo("email_domain", ParseUser.getCurrentUser().get("email_domain"));
        //TEST
        query.whereEqualTo("email_domain", "hotmail");
        //query.whereEqualTo("gender", "female");
        query.whereNotEqualTo("objectId","bob");

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        System.out.println(object);
                    }
                } else {
                    System.out.println("FAIL: " + objects);
                }
            }
        });

        */


        final ArrayList<String> seen_list = new ArrayList<String>();
        final ArrayList<String> r_list = new ArrayList<String>();


        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Profile");
        query3.whereEqualTo("Me", "r22mHwUeTu");
        query3.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    for (Object s : object.getList("users_seen")) {
                        seen_list.add(s.toString());
                    }
                    // fetch user
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    //query.whereContainsAll("objectId", seen_list);
                    query.whereNotContainedIn("objectId", seen_list);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        public void done(List<ParseUser> userlist, ParseException e) {
                            if (e == null) {
                                for (ParseUser s : userlist) {
                                    //checking
                                    r_list.add(s.getObjectId());
                                }
                                // do stuff with r_list here
                                for(String s:r_list) {
                                    System.out.println(s);
                                }


                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.d("score", "Retrieved the object.");
                }
            }
        });




//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.findInBackground(new FindCallback<ParseUser>() {
//            public void done(List<ParseUser> userlist, ParseException e) {
//                if (e == null) {
//                    for (ParseUser s : userlist) {
//                        //checking
//                        r_list.add(s.getObjectId());
//                    }
//                    // do stuff with r_list here
//
//                    System.out.println(r_list.get(2));
//
//
//
//                } else {
//                    Log.d("score", "Error: " + e.getMessage());
//                }
//            }
//        });







//
//        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Profile");
//        //query.whereEqualTo("email_domain", ParseUser.getCurrentUser().get("email_domain"));
//        //query.whereEqualTo("email_domain", "hotmail");
//        //query.whereEqualTo("gender", ParseUser.getCurrentUser().get("InterestinFemale"));
//        //query.whereEqualTo("Gender", "male");
//        //query.whereEqualTo("email_domain", ParseUser.getCurrentUser().get("email_domain"));
//        //query.whereEqualTo("Me",ParseUser.getCurrentUser().getObjectId());
//        query.whereEqualTo("Me", "r22mHwUeTu");
//        query.getFirstInBackground(new GetCallback<ParseObject>() {
//            public void done(ParseObject object, ParseException e) {
//                if (e == null) {
//
//                    for (Object s : object.getList("users_seen")) {
//                        if(s.toString().equals("3t3g")){
//                            // not add to list
//                            //System.out.println("WORKS");
//                        }
//                        else{
//                            r_list.add(s.toString());
//                        }
//                        //System.out.println(s.toString());
//                    }
//
//
//
//
//                } else {
//                    Log.d("score", "Retrieved the object.");
//                }
//            }
//        });


    }


}
