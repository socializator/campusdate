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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class ProfilePageActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {


    protected EditText firstNameEditText;
    protected EditText lastNameEditText;
    protected EditText ageEditText;
    protected EditText majorEditText;
    protected EditText whatsupEditText;

    protected Spinner genderSpinner;
    protected CheckBox maleInterestCheckBox;
    protected CheckBox femaleInterestCheckBox;

    protected Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        // /Spinner
        genderSpinner = (Spinner) findViewById(R.id.genderSpinnerProfile);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_items,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(this);

        //Connect objects with ids
        firstNameEditText = (EditText)findViewById(R.id.firstNameProfile);
        lastNameEditText = (EditText)findViewById(R.id.lastNameProfile);
        ageEditText = (EditText)findViewById(R.id.ageProfile);
        majorEditText = (EditText)findViewById(R.id.majorProfile);
        whatsupEditText = (EditText)findViewById(R.id.whatsupProfile);

        //check boxes
        maleInterestCheckBox = (CheckBox)findViewById(R.id.maleCheckBoxProfile);
        femaleInterestCheckBox = (CheckBox)findViewById(R.id.femaleCheckBoxProfile);

        //save button
        saveButton = (Button)findViewById(R.id.saveButtonProfile);

        ParseUser currentUser = ParseUser.getCurrentUser();
        //String currentUserObjectIdID = currentUser.getObjectId();
        String currentUserObjectIdID = "r22mHwUeTu";

        ParseObject obj = ParseObject.createWithoutData("_User", currentUserObjectIdID);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo("Me", obj);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    firstNameEditText.setText(object.getString
                            ("FirstName"),TextView.BufferType.EDITABLE);
                    lastNameEditText.setText(object.getString
                            ("LastName"),TextView.BufferType.EDITABLE);
                    ageEditText.setText(object.getString
                            ("Age"),TextView.BufferType.EDITABLE);
                    majorEditText.setText(object.getString
                            ("Major"), TextView.BufferType.EDITABLE);
                    whatsupEditText.setText(object.getString
                            ("WhatsUp"), TextView.BufferType.EDITABLE);

                    genderSpinner.setSelection(adapter.getPosition(object.getString("Gender")));

                    boolean maleChecked = object.getBoolean("InterestInMale");
                    boolean femaleChecked = object.getBoolean("InterestInFemale");

                    if(maleChecked == true){
                        maleInterestCheckBox.setChecked(true);
                    }else{
                        maleInterestCheckBox.setChecked(false);
                    }

                    if(femaleChecked == true){
                        femaleInterestCheckBox.setChecked(true);
                    }else{
                        femaleInterestCheckBox.setChecked(false);
                    }

                } else {
                    // something went wrong
                }
            }
        });




        //Listen to save button click
        saveButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){

                //get Current user's objectId
                ParseUser currentUser = ParseUser.getCurrentUser();
                //String currentUserObjectIdID = currentUser.getObjectId();
                String currentUserObjectIdID = "r22mHwUeTu";

                ParseObject obj = ParseObject.createWithoutData("_User", currentUserObjectIdID);

                //update an object
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
                // Retrieve the object by id

                query.whereEqualTo("Me", obj);

                query.getFirstInBackground(new GetCallback<ParseObject>() {

                    //create string to hold user input text
                    String firstName = firstNameEditText.getText().toString();
                    String lastName = lastNameEditText.getText().toString();
                    String age = ageEditText.getText().toString();
                    String major = majorEditText.getText().toString();
                    String whatsup = whatsupEditText.getText().toString();

                    String gender = genderSpinner.getSelectedItem().toString();

                    Boolean maleInterest = maleInterestCheckBox.isChecked();
                    Boolean femaleInterest = femaleInterestCheckBox.isChecked();

                    public void done(ParseObject profile, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed
                            //if (firstName != null) {
                            profile.put("FirstName", firstName);
                            profile.put("LastName", lastName);
                            profile.put("Age", age);
                            profile.put("Major", major);
                            profile.put("WhatsUp", whatsup);

                            profile.put("Gender", gender);

                            profile.put("InterestInMale", maleInterest);
                            profile.put("InterestInFemale", femaleInterest);

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



}
