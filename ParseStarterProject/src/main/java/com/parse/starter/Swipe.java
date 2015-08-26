package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Swipe extends ActionBarActivity {

    ArrayList<String> final_list = new ArrayList<String>();

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


    public void dislike_user(View view) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        //query.getInBackground(ParseUser.getCurrentUser().get("profile_object_id").toString(), new GetCallback<ParseObject>() {
        query.getInBackground("115TKypy3w", new GetCallback<ParseObject>() {
            public void done(ParseObject user_seen, ParseException e) {
                if (e == null) {

                    //user_seen.addUnique("users_seen", final_list.get(final_list.size() - 1));
                    user_seen.addUnique("users_seen", "yooooo2");

                    user_seen.saveInBackground();

                    final_list.remove(final_list.size() - 1);
                    final_list.trimToSize();
                }
            }
        });
    }

    public void like_user(View view) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        //query.getInBackground(ParseUser.getCurrentUser().get("profile_object_id").toString(), new GetCallback<ParseObject>() {
        query.getInBackground("115TKypy3w", new GetCallback<ParseObject>() {
            public void done(ParseObject user_seen, ParseException e) {
                if (e == null) {

                    //user_seen.addUnique("users_like", final_list.get(final_list.size() - 1));
                    //user_seen.addUnique("users_seen", final_list.get(final_list.size() - 1));
                    user_seen.addUnique("users_like", "yooooo2");
                    user_seen.addUnique("users_seen", "yooooo2");

                    user_seen.saveInBackground();

                    final_list.remove(final_list.size() - 1);
                    final_list.trimToSize();
                }
            }
        });
    }

    public void get_data(View view) {

        final ArrayList<String> seen_list = new ArrayList<String>();
        final ArrayList<String> result_list = new ArrayList<String>();

        ParseQuery<ParseObject> users_seen_query = ParseQuery.getQuery("Profile");

        //users_seen_query.whereEqualTo("user_object_id", ParseUser.getCurrentUser().getObjectId());
        users_seen_query.whereEqualTo("user_object_id", "r22mHwUeTu");

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
                    if (ParseUser.getCurrentUser().get("intrested_in_females") == true) {
                        users_list_query.whereEqualTo("gender", "female");

                    }
                    if (ParseUser.getCurrentUser().get("intrested_in_males") == true) {

                        users_list_query.whereEqualTo("gender", "male");
                    }
                    */
                    users_list_query.whereEqualTo("gender", "male");

                    users_list_query.whereNotContainedIn("user_object_id", seen_list);

                    //users_list_query.whereNotEqualTo("user_object_id", ParseUser.getCurrentUser().getObjectId());

                    users_list_query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> userlist, ParseException e) {
                            if (e == null) {
                                for (ParseObject s : userlist) {
                                    result_list.add(s.getObjectId());
                                }
                                for (String s : result_list) {
                                    final_list = result_list;
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

    public void view_profiles(View view) {
        System.out.println(final_list);
        final TextView swipe_name = (TextView) findViewById(R.id.swipe_name);

        if (final_list.size() > 0) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
            //query.whereEqualTo("objectId" , final_list.get(0))
            query.getInBackground(final_list.get(final_list.size() - 1), new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        // object will be your game score
                        String name = object.getString("name");
                        swipe_name.setText(name);
                        //Set profile picture
                    } else {
                    }
                }
            });

        }
    }
}

