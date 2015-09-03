package com.parse.starter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class SwipeActivity extends Activity {

    private ArrayList<CardMode> al;
    private CardAdapter adapter;
    private int i;
    private SwipeFlingAdapterView flingContainer;
    private List<List<String>> list = new ArrayList<>();
    private ImageView left, right;

    private ArrayList<String> name;
    private ArrayList<Integer> age;
    private ArrayList<String> user_id;
    protected ProgressDialog proDialog;
    private Boolean finishtag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);


        ActionBar bar = getActionBar();
        bar.setIcon(android.R.color.transparent);
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03A9F4")));
        bar.setTitle(Html.fromHtml("<font color='#ffffff'>Campusdate</font>"));

        left = (ImageView) findViewById(R.id.left);
        right = (ImageView) findViewById(R.id.right);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right();
            }
        });

        al = new ArrayList<>();
        name = new ArrayList<>();
        age = new ArrayList<>();
        user_id = new ArrayList<>();

        startLoading();
        //add data to al

        final ParseQuery<ParseObject> user_profile_query = ParseQuery.getQuery("Profile");
        user_profile_query.whereEqualTo("user_object_id", ParseUser.getCurrentUser().getObjectId());
        user_profile_query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(final ParseObject object, ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
                    query.whereNotEqualTo("user_object_id", ParseUser.getCurrentUser().getObjectId());
                    query.whereEqualTo("email_domain", ParseUser.getCurrentUser().getString("email_domain"));
                    query.whereNotContainedIn("user_object_id", object.getList("users_like"));

                    if (ParseUser.getCurrentUser().getBoolean("interested_in_females"))
                        query.whereEqualTo("gender", "Female");

                    if (ParseUser.getCurrentUser().getBoolean("interested_in_males"))
                        query.whereEqualTo("gender", "Male");

                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> query_list, ParseException e) {
                            if (e == null) {
                                for (int j = 0; j < query_list.size(); j++) {
                                    ParseFile fileObject = query_list.get(j).getParseFile("profile_picture");
                                    name.add(query_list.get(j).getString("first_name") + " " + query_list.get(j).get("last_name"));
                                    age.add(Integer.parseInt(query_list.get(j).getString("age")));
                                    user_id.add(query_list.get(j).getString("user_object_id"));
                                    List<String> s = new ArrayList<>();
                                    s.add(fileObject.getUrl());
                                    list.add(s);
                                }
                                for (int z = 0; z < list.size(); z++) {
                                    al.add(new CardMode(name.get(z), age.get(z), list.get(z), user_id.get(z)));
                                }
                                adapter.notifyDataSetChanged();
                                stopLoading();
                            }
                            // use data for something
                            else {
                                Log.d("test", "There was a problem downloading the data.");
                            }
                        }
                    });

                } else {
                    //error
                }
            }
        });






//
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
//        query.whereNotEqualTo("user_object_id", ParseUser.getCurrentUser().getObjectId());
//        query.whereEqualTo("email_domain", ParseUser.getCurrentUser().getString("email_domain"));
//        //query.whereNotContainedIn("user_object_id", query.getList("users_matched_with"));
//
//        if(ParseUser.getCurrentUser().getBoolean("interested_in_females"))
//            query.whereEqualTo("gender","Female");
//
//        if(ParseUser.getCurrentUser().getBoolean("interested_in_males"))
//            query.whereEqualTo("gender","Male");
//
//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> query_list, ParseException e) {
//                if (e == null) {
//                    for (int j = 0; j < query_list.size(); j++) {
//                        ParseFile fileObject = query_list.get(j).getParseFile("profile_picture");
//                        name.add(query_list.get(j).getString("first_name") + " " + query_list.get(j).get("last_name"));
//                        age.add(Integer.parseInt(query_list.get(j).getString("age")));
//                        user_id.add(query_list.get(j).getString("user_object_id"));
//                        List<String> s = new ArrayList<>();
//                        s.add(fileObject.getUrl());
//                        list.add(s);
//                    }
//                    for(int z=0;z<list.size();z++){
//                        al.add(new CardMode(name.get(z), age.get(z), list.get(z),user_id.get(z)));
//                        adapter.notifyDataSetChanged();
//                    }
//                    stopLoading();
//                }
//                // use data for something
//                else{
//                    Log.d("test", "There was a problem downloading the data.");
//                }
//            }
//        });


//        for (int i = 0; i < imageUrls.length; i++) {
//            List<String> s = new ArrayList<>();
//            s.add(imageUrls[i]);
//            list.add(s);
//        }
//        List<String> yi;
//        al.add(new CardMode("胡欣语", 21, list.get(0)));
//        al.add(new CardMode("Norway", 21, list.get(1)));
//        al.add(new CardMode("王清玉", 18, list.get(2)));
//        al.add(new CardMode("测试1", 21, list.get(3)));
//        al.add(new CardMode("测试2", 21, list.get(4)));
//        al.add(new CardMode("测试3", 21, list.get(5)));
//        al.add(new CardMode("测试4", 21, list.get(6)));
//        al.add(new CardMode("测试5", 21, list.get(7)));
//        al.add(new CardMode("测试6", 21, list.get(8)));
//        al.add(new CardMode("测试7", 21, list.get(9)));
//        al.add(new CardMode("测试8", 21, list.get(10)));
//        al.add(new CardMode("测试9", 21, list.get(11)));
//        al.add(new CardMode("测试10", 21, list.get(12)));
//        al.add(new CardMode("测试11", 21, list.get(13)));
//        al.add(new CardMode("测试12", 21, list.get(14)));


        // done with data


        //setAdapter();
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_swipe_item, R.id.helloText, al);
        adapter = new CardAdapter(this, al);
        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                if(adapter.getCount()>1) {
                    al.remove(0);
                    adapter.notifyDataSetChanged();
                }else if(adapter.getCount()==1){
                    al.remove(0);
                    adapter.notifyDataSetChanged();
                    makeToast(SwipeActivity.this, "No More User");
                }else{
                    makeToast(SwipeActivity.this, "No More User");
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Dislike

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                     final CardMode temp = (CardMode) dataObject;


                    //Like
                    final String currentUserId = ParseUser.getCurrentUser().getObjectId();
                    final ParseQuery<ParseObject> update_user_arrays_query = ParseQuery.getQuery("Profile");
                    update_user_arrays_query.whereEqualTo("user_object_id", currentUserId);
                    update_user_arrays_query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(final ParseObject currentuser_object, ParseException e) {
                            if (e == null) {
                                currentuser_object.addUnique("users_like", temp.getUserId());
                                currentuser_object.saveInBackground();

                                //check match
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
                                query.whereEqualTo("user_object_id", temp.getUserId());
                                query.getFirstInBackground(new GetCallback<ParseObject>() {
                                    public void done(ParseObject otheruser_object, ParseException e) {
                                        if (e == null) {
                                            if (otheruser_object.getList("users_like").contains(currentUserId)) {
                                                otheruser_object.addUnique("users_matched_with", currentUserId);
                                                otheruser_object.saveInBackground();

                                                currentuser_object.addUnique("users_matched_with", temp.getUserId());
                                                currentuser_object.saveInBackground();
                                                makeToast(SwipeActivity.this, "You Have a New Match!");
                                            }
                                        } else {
                                            // error
                                        }
                                    }
                                });
                                //error
                            } else {
                                //error
                            }
                        }
                    });
                }


            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


//        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClicked(int itemPosition, Object dataObject) {
//                makeToast(SwipeActivity.this, "点击图片");
//            }
//        });
    }

    protected void startLoading() {
        proDialog = new ProgressDialog(this);
        proDialog.setMessage("Loading...Please Wait");
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        proDialog.setCancelable(false);
        proDialog.show();
    }

    protected void stopLoading() {
        proDialog.dismiss();
        proDialog = null;
        finishtag = true;
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    public void right() {
        flingContainer.getTopCardListener().selectRight();
    }

    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }

    /**
     * Goes to profile tab.
     **/
    public void go_to_profile(View view) {
        Intent intent = new Intent(this, ProfilePageActivity.class);
        startActivity(intent);
    }


    /**
     * Goes to matches tab.
     **/
    public void go_to_matches(View view) {
        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);
    }


    /**
     * Goes to swipe tab.
     **/
    public void go_to_swipe(View view) {
        //nothing
    }

    /**
     * Adds logout button to menu.
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Inflates menu.
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_swipe, menu);
        return true;
    }
}




