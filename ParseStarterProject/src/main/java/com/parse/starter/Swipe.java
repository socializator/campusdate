package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Swipe extends Activity {

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
        Intent intent = new Intent(this, MatchActivity.class);
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

                    //user_seen.addUnique("users_seen", (final_list.get(final_list.size() - 1).toString()));
                    user_seen.addUnique("users_seen", "seen you");

                    user_seen.saveInBackground();

                    final_list.remove(final_list.size() - 1);
                    final_list.trimToSize();
                }
            }
        });
    }

    public void like_user(View view) {
        ParseQuery<ParseObject> update_user_arrays_query = ParseQuery.getQuery("Profile");
        //update_user_arrays_query.getInBackground(ParseUser.getCurrentUser().get("profile_object_id").toString(), new GetCallback<ParseObject>() {
        update_user_arrays_query.getInBackground("115TKypy3w", new GetCallback<ParseObject>() {
            public void done(ParseObject user_seen, ParseException e) {
                if (e == null) {

                    //user_seen.addUnique("users_seen", (final_list.get(final_list.size() - 1)).toString());
                    //user_seen.addUnique("users_like", (final_list.get(final_list.size() - 1)).toString());

                    user_seen.addUnique("users_seen", "seen you");
                    user_seen.addUnique("users_like", "like you");

                    user_seen.saveInBackground();
                }
            }
        });


        //Create match if mutual like
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo("objectId", final_list.get(final_list.size() - 1).toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> users_they_like, ParseException e) {
                if (e == null) {

                    //if (users_they_like.contains(ParseUser.getCurrentUser().get("profile_object_id").toString())) {
                    if (users_they_like.contains("like you")) {


                        //create a match
                        //add to my users_matched_with list
                        ParseQuery<ParseObject> add_to_my_matches = ParseQuery.getQuery("Profile");
                        //add_to_their_matches.getInBackground(ParseUser.getCurrentUser().get("profile_object_id").toString(), new GetCallback<ParseObject>() {
                        add_to_my_matches.getInBackground("115TKypy3w", new GetCallback<ParseObject>() {
                            public void done(ParseObject my_profile, ParseException e) {
                                if (e == null) {
                                    //my_profile.addUnique("users_matched_with", final_list.get(final_list.size() - 1).toString());
                                    my_profile.addUnique("users_matched_with", "users_matched_with working");
                                } else {
                                    System.out.println("fail");
                                }
                            }
                        });


                        ///add to their users_matched_with list
                        ParseQuery<ParseObject> add_to_their_matches = ParseQuery.getQuery("Profile");
                        //add_to_their_matches.getInBackground(final_list.get(final_list.size() - 1).toString(), new GetCallback<ParseObject>() {
                        add_to_their_matches.getInBackground("115TKypy3w", new GetCallback<ParseObject>() {
                            public void done(ParseObject their_profile, ParseException e) {
                                if (e == null) {
                                    //their_profile.addUnique("users_matched_with", ParseUser.getCurrentUser().get("profile_object_id").toString());
                                    their_profile.addUnique("users_matched_with", "users_matched_with working");
                                } else {
                                    System.out.println("fail");
                                }
                            }
                        });
                    }
                } else {
                    System.out.println("FAIL");
                }
            }
        });

        final_list.remove(final_list.size() - 1);
        final_list.trimToSize();
    }

    public void get_data(View view) {

        final ArrayList<String> seen_list = new ArrayList<String>();
        final ArrayList<String> result_list = new ArrayList<String>();

        ParseQuery<ParseObject> users_seen_query = ParseQuery.getQuery("Profile");

        //users_seen_query.whereEqualTo("user_object_id", ParseUser.getCurrentUser().getObjectId().toString());
        users_seen_query.whereEqualTo("user_object_id", "r22mHwUeTu");

        users_seen_query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    for (Object s : object.getList("users_seen")) {
                        seen_list.add(s.toString());
                    }

                    ParseQuery<ParseObject> users_list_query = ParseQuery.getQuery("Profile");

                    //users_list_query.whereEqualTo("email_domain", ParseUser.getCurrentUser().get("email_domain").toString());
                    users_list_query.whereEqualTo("email_domain", "hotmail");

                    /*
                    if ((boolean)ParseUser.getCurrentUser().get("interested_in_females") == true) {
                        users_list_query.whereEqualTo("gender", "female");
                    }
                    if ((boolean)ParseUser.getCurrentUser().get("interested_in_males") == true) {
                        users_list_query.whereEqualTo("gender", "male");
                    }
                    */

                    users_list_query.whereEqualTo("gender", "female");

                    users_list_query.whereNotContainedIn("user_object_id", seen_list);

                    //users_list_query.whereNotEqualTo("user_object_id", ParseUser.getCurrentUser().getObjectId().toString());
                    users_list_query.whereNotEqualTo("user_object_id", "r22mHwUeTu");

                    users_list_query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> user_list, ParseException e) {
                            if (e == null) {
                                for (ParseObject s : user_list) {
                                    result_list.add(s.getObjectId().toString());
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
            query.getInBackground(final_list.get(final_list.size() - 1), new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        System.out.println("PULLING PROFILE");
                        //Set Name
                        String first_name = object.getString("first_name");
                        String last_name = object.getString("first_name");
                        String full_name = first_name + " " + last_name;
                        swipe_name.setText(full_name);

                        //Set profile picture
                        ParseFile image = object.getParseFile("profile_picture");
                        final ParseImageView imageView = (ParseImageView) findViewById(R.id.swipe_photo);
                        imageView.setParseFile(image);
                        imageView.loadInBackground(new GetDataCallback() {
                            public void done(byte[] data, ParseException e) {
                            }
                        });
                    } else {
                        System.out.println("FAILLLL");
                    }
                }
            });

        }
    }
}

