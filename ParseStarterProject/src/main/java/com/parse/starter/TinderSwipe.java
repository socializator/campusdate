package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TinderSwipe extends Activity {
    ArrayList<String> final_list = new ArrayList<String>();

    @InjectView(R.id.frame)
    SwipeFlingAdapterView flingContainer;
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.inject(this);

        al = new ArrayList<>();
        //al.add("php");

        get_data();

        System.out.println("AL right before adapter" + al.toString());

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                //Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                makeToast(TinderSwipe.this, "Left!");
                //dislike
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                makeToast(TinderSwipe.this, "Right!");
                //like
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                makeToast(TinderSwipe.this, "No more users!");
                //al.add("XML ".concat(String.valueOf(i)));
                //arrayAdapter.notifyDataSetChanged();
                //Log.d("LIST", "notified");
                //i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(TinderSwipe.this, "Clicked!");
            }
        });
    }

    @OnClick(R.id.right)
    public void right() {
        /**
         * Trigger the right event manually.
         */
        flingContainer.getTopCardListener().selectRight();
    }


    @OnClick(R.id.left)
    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }

    public void get_data() {
        final ArrayList<String> seen_list = new ArrayList<String>();
        final ArrayList<String> result_list = new ArrayList<String>();

        ParseQuery<ParseObject> users_seen_query = ParseQuery.getQuery("Profile");
        users_seen_query.whereEqualTo("user_object_id", ParseUser.getCurrentUser().getObjectId());
        users_seen_query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    boolean interested_in_females = object.getBoolean("interested_in_females");
                    boolean interested_in_males = object.getBoolean("interested_in_males");
                    for (Object s : object.getList("users_seen")) {
                        seen_list.add(s.toString());
                    }

                    ParseQuery<ParseObject> users_list_query = ParseQuery.getQuery("Profile");

                    users_list_query.whereEqualTo("email_domain", ParseUser.getCurrentUser().get("email_domain"));

                    //show only females
                    if (interested_in_females == true && interested_in_males == false) {
                        users_list_query.whereEqualTo("gender", "female");
                    }
                    //show only males
                    else if (interested_in_females == false && interested_in_males == true) {
                        users_list_query.whereEqualTo("gender", "male");
                    }

                    users_list_query.whereNotContainedIn("objectId", seen_list);
                    users_list_query.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
                    users_list_query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> user_list, ParseException e) {
                            if (e == null) {
                                for (ParseObject s : user_list) {
                                    result_list.add(s.getObjectId());
                                }
                                final_list = result_list;
                                al = final_list;
                                System.out.println("AL in get data function" + al.toString());
                                //view_profiles();
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
