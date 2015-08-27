package com.parse.starter;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.parse.starter.R;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.FileNotFoundException;
import java.net.URI;


public class ProfilePageActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {


    protected ParseImageView parseImageView;
    protected String photoPath;

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

        //profile photoes
        parseImageView = (ParseImageView) findViewById(R.id.profile_photo);

        // /Spinner
        genderSpinner = (Spinner) findViewById(R.id.genderSpinnerProfile);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(this);

        //Connect objects with ids
        firstNameEditText = (EditText) findViewById(R.id.firstNameProfile);
        lastNameEditText = (EditText) findViewById(R.id.lastNameProfile);
        ageEditText = (EditText) findViewById(R.id.ageProfile);
        majorEditText = (EditText) findViewById(R.id.majorProfile);
        whatsupEditText = (EditText) findViewById(R.id.whatsupProfile);

        //check boxes
        maleInterestCheckBox = (CheckBox) findViewById(R.id.maleCheckBoxProfile);
        femaleInterestCheckBox = (CheckBox) findViewById(R.id.femaleCheckBoxProfile);

        //save button
        saveButton = (Button) findViewById(R.id.saveButtonProfile);


        /************* Retrieve Data From Parse Database *************/
        ParseUser currentUser = ParseUser.getCurrentUser();
        //String currentUserObjectIdID = currentUser.getObjectId();
        String currentUserObjectIdID = "r22mHwUeTu";

        //ParseObject obj = ParseObject.createWithoutData("_User", currentUserObjectIdID);



        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo("user_object_id", currentUserObjectIdID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    //get profile picture
                    ParseFile image = object.getParseFile("profile_picture");
                    parseImageView.setParseFile(image);
                    parseImageView.loadInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                        }
                    });


                    firstNameEditText.setText(object.getString
                            ("first_name"), TextView.BufferType.EDITABLE);
                    lastNameEditText.setText(object.getString
                            ("last_name"), TextView.BufferType.EDITABLE);
                    ageEditText.setText(object.getString
                            ("age"), TextView.BufferType.EDITABLE);
                    majorEditText.setText(object.getString
                            ("major"), TextView.BufferType.EDITABLE);
                    whatsupEditText.setText(object.getString
                            ("whats_up"), TextView.BufferType.EDITABLE);

                    genderSpinner.setSelection(adapter.getPosition(object.getString("gender")));

                    boolean maleChecked = object.getBoolean("interested_in_males");
                    boolean femaleChecked = object.getBoolean("interested_in_females");

                    if (maleChecked == true) {
                        maleInterestCheckBox.setChecked(true);
                    } else {
                        maleInterestCheckBox.setChecked(false);
                    }

                    if (femaleChecked == true) {
                        femaleInterestCheckBox.setChecked(true);
                    } else {
                        femaleInterestCheckBox.setChecked(false);
                    }

                } else {
                    // something went wrong
                }
            }
        });

        /********** Upload an image to Parse Database **********/


        parseImageView.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,0);
            }
        });



        /************* Save Data to Parse Database *************/
        //Listen to save button click
        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                //get Current user's objectId
                ParseUser currentUser = ParseUser.getCurrentUser();
                //String currentUserObjectIdID = currentUser.getObjectId();
                String currentUserObjectIdID = "r22mHwUeTu";

                //ParseObject obj = ParseObject.createWithoutData("_User", currentUserObjectIdID);

                //update an object
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
                // Retrieve the object by id

                query.whereEqualTo("user_object_id", currentUserObjectIdID);

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

                            byte[] image = "Working at Parse is great!".getBytes();
                            ParseFile file = new ParseFile(photoPath,image);

                            file.saveInBackground();
                            profile.put("profile_picture", file);


                            profile.put("first_name", firstName);
                            profile.put("last_name", lastName);
                            profile.put("age", age);
                            profile.put("major", major);
                            profile.put("whats_up", whatsup);

                            profile.put("gender", gender);

                            profile.put("interested_in_males", maleInterest);
                            profile.put("interested_in_females", femaleInterest);

                            profile.saveInBackground();

                            System.out.println("\nSuccess");

                        } else {
                            System.out.println("Fail");
                        }
                    }
                });

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            //photoPath = getRealPathFromURI(targetUri);
            Bitmap bitmap;
            try{
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                parseImageView.setImageBitmap(bitmap);
                photoPath = getRealPathFromURI(targetUri);


                //ParseUser currentUser = ParseUser.getCurrentUser();
                //String currentUserObjectIdID = currentUser.getObjectId();
                //String currentUserObjectIdID = "r22mHwUeTu";
                //ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
                //query.whereEqualTo("user_object_id", currentUserObjectIdID);
                //query.getFirst().put("profile_picture", file);

            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    protected String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
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
                               int pos, long id) {
        parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}