package com.parse.starter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;


public class ProfilePageActivity extends Activity implements AdapterView.OnItemSelectedListener {

    /******************************
     * Variable Declairation
     ******************************/
    protected ParseImageView parseImageView;
    protected String photoPath;
    protected Bitmap bitmap;

    protected EditText firstNameEditText;
    protected EditText lastNameEditText;
    protected EditText ageEditText;
    protected EditText majorEditText;
    protected EditText whatsupEditText;

    protected Spinner genderSpinner;
    protected CheckBox maleInterestCheckBox;
    protected CheckBox femaleInterestCheckBox;

    protected Button saveButton;

    private static int RESULT_LOAD_IMG = 1;

    boolean finishTag = false;

    protected String firstName;
    protected String lastName;
    protected String age;
    protected String major;
    protected String whatsUp;
    protected String gender;
    protected Boolean interestedInMale;
    protected Boolean interestedInFemale;

    /******************************
     * onCreate
     ******************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar bar = getActionBar();
        bar.setIcon(android.R.color.transparent);
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03A9F4")));
        bar.setTitle(Html.fromHtml("<font color='#ffffff'>Campusdate</font>"));

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
        final ParseUser currentUser = ParseUser.getCurrentUser();
        final String currentUserObjectIdID = currentUser.getObjectId();
        //String currentUserObjectIdID = "test";

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo("user_object_id", currentUserObjectIdID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    //add profile object Id to User Table
                    currentUser.put("profile_object_id",object.getObjectId());
                    currentUser.put("firsttime",false);
                    currentUser.saveInBackground();

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

        /********** Access Image Gallery **********/

        parseImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });



        /************* Save Data to Parse Database *************/
        //Listen to save button click
        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                //get Current user's objectId
                final ParseUser currentUser = ParseUser.getCurrentUser();
                final String currentUserObjectIdID = currentUser.getObjectId();
                //String currentUserObjectIdID = "test";

                //update an object
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
                // Retrieve the object by id
                query.whereEqualTo("user_object_id", currentUserObjectIdID);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject profile, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed
                            firstName = firstNameEditText.getText().toString();
                            lastName = lastNameEditText.getText().toString();

                            gender = genderSpinner.getSelectedItem().toString().toLowerCase();
                            interestedInMale = maleInterestCheckBox.isChecked();
                            interestedInFemale = femaleInterestCheckBox.isChecked();

                            age = ageEditText.getText().toString();
                            major = majorEditText.getText().toString();
                            whatsUp = whatsupEditText.getText().toString();

                            checkInputFields(profile);


                        } else {
                            System.out.println("Fail");
                        }
                    }
                });

            }
        });
    }

    /**********************functions for checking input***********************/
    public boolean isEmpty(String s) {
        return s.trim().length() > 0 ? false : true;
    }


    private void checkInputFields(ParseObject profile) {

        if(profile.get("profile_picture") == null && bitmap == null){
            alertMsg("Saving Profile Failed", "Please upload a profile photo");
        }
        else if(isEmpty(firstName)){
            alertMsg("Saving Profile Failed", "Please enter your First Name");
        }else if(!(firstName.matches("[a-zA-Z]+"))){
            alertMsg("Saving Profile Failed", "Please " +
                    "only enter letters for First Name.");
        }else if(isEmpty(lastName)){
            alertMsg("Saving Profile Failed", "Please enter your Last Name");
        }else if(!(lastName.matches("[a-zA-Z]+"))){
            alertMsg("Saving Profile Failed", "Please " +
                    "only enter letters for Last Name.");
        }else if(gender.equals("Not Selected")){
            alertMsg("Saving Profile Failed", "You must enter your gender");
        }else if(interestedInFemale == false && interestedInMale == false){
            alertMsg("Saving Profile Failed", "You must check at least one " +
                    "gender you are interested in");
        }else if(!(age.matches("[0-9]+" )) && !(age.equals(""))) {
            alertMsg("Saving Profile Failed", "Please " +
                    "enter a number for age or leave it blank");
        }else {
            updateProfile(profile);
        }
    }
    /*
     * updateProfile
     */
    //This function update user profile after all user inputs are verified
    protected void updateProfile (ParseObject profile){
        if(bitmap != null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            byte[] image = stream.toByteArray();

            ParseFile file = new ParseFile("prof_pic.jpg",image);

            file.saveInBackground();

            profile.put("profile_picture", file);
        }

        profile.put("first_name", firstName);
        profile.put("last_name", lastName);
        profile.put("age", age);
        profile.put("major", major);
        profile.put("whats_up", whatsUp);
        profile.put("gender", gender);
        profile.put("interested_in_males", interestedInMale);
        profile.put("interested_in_females", interestedInFemale);

        profile.saveInBackground();

        saveGenderInterestToUserTable();

        // Show a simple toast message
        Toast.makeText(ProfilePageActivity.this, "Profile Saved",
                Toast.LENGTH_SHORT).show();

    }

    protected void saveGenderInterestToUserTable(){
        final ParseUser currentUser = ParseUser.getCurrentUser();
        final String currentUserObjectIdID = currentUser.getObjectId();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        // Retrieve the object by id
        //query.whereEqualTo("objectId", currentUserObjectIdID);
        query.getInBackground(currentUserObjectIdID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("interested_in_females", interestedInFemale);
                    object.put("interested_in_males", interestedInMale);

                    object.saveInBackground();
                }else{

                }
            }
        });
    }
    protected void alertMsg(String title, String msg) {
        //build dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        //create alert dialog
        AlertDialog alert = builder.create();
        //show dialog on screen
        alert.show();
    }

    /**
     * Helper function to clear all text fields in current activity
     */
    //this function will be using when need to clear the text user entered in the textfields
    /*protected void clearAlltext() {
        ViewGroup textFields = (ViewGroup) findViewById(R.id.signup_textFields);
        for (int i = 0, count = textFields.getChildCount(); i < count; ++i) {
            View view = textFields.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
        }
    }*/

    /******************************
     * onActivityResult
     ******************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK &&  null != data) {
                //get image from data
                Uri targetUri = data.getData();

                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(targetUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                photoPath = cursor.getString(columnIndex);
                cursor.close();

                // Set the Image in ImageView after decoding the String
                bitmap = BitmapFactory.decodeFile(photoPath);
                bitmap = getRoundedCornerBitmap(bitmap, 53);
                parseImageView.setImageBitmap(bitmap);

            } else {
                    Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
                //photoPath = getRealPathFromURI(targetUri);
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }


    }

    /******************************
     * getRealPathFromURI
     ******************************/
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




    /******************************
     * onCreateOptionsMenu
     ******************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_page, menu);
        return true;
    }

    /******************************
     * onOptionsItemSelected
     ******************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /******************************
     * onItemSelected
     ******************************/
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        parent.getItemAtPosition(pos);
    }


    /******************************
     * onNothingSelected
     ******************************/
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void go_to_profile(View view) {
        //nothing
    }


    public void go_to_matches(View view) {
        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);
    }


    public void go_to_swipe(View view) {
        Intent intent = new Intent(this, Swipe.class);
        startActivity(intent);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}