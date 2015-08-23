package com.parse.starter;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class ProfilePageActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    public Spinner genderSpinner;
    protected EditText firstNameEditText;

    protected Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        // /Spinner
        genderSpinner = (Spinner) findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_items,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(this);

        //Connect objects with ids
        firstNameEditText = (EditText)findViewById(R.id.firstNameProfile);
        saveButton = (Button)findViewById(R.id.saveButtonProfile);

        //Listen to save button click
        saveButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){

                //get Current user's objectId
                ParseUser currentUser = ParseUser.getCurrentUser();
                //String currentUserObjectIdID = currentUser.getObjectId();
                String currentUserObjectIdID = "r22mHwUeTu";

                //update an object
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
                // Retrieve the object by id

                query.whereEqualTo("Me", currentUserObjectIdID);


                query.getFirstInBackground(new GetCallback<ParseObject>() {

                    String firstName = firstNameEditText.getText().toString();

                    public void done(ParseObject profile, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed
                            //if (firstName != null) {
                            profile.put("FirstName", firstName);
                            profile.saveInBackground();
                            System.out.println("Success");

                        } else {
                            System.out.println("fail");
                        }
                    }
                });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_page, menu);
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

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id){
            parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void onCheckboxClicked(View view){
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.male_checkBox:
                if (checked) {
                }
                else {
                }
                break;
            case R.id.female_checkBox:
                if (checked) {
                }
                else {
                }
                break;

        }
    }


}
