package com.parse.starter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity implements View.OnClickListener {

    protected ProgressDialog proDialog;
    Button cancelButton;
    Button confirmButton;
    String username;
    String password1;
    String password2;
    String firstname;
    String lastname;
    boolean finishTag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar bar = getActionBar();
        bar.setIcon(android.R.color.transparent);
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03A9F4")));
        bar.setTitle(Html.fromHtml("<font color='#ffffff'>Campusdate</font>"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Intent intent = getIntent();
        username = intent.getStringExtra("Email");


        confirmButton = (Button) findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(this);
        cancelButton = (Button) findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);

        TextView txtEmail = ((TextView) findViewById(R.id.signup_email));
        txtEmail.setText("Email: " + username);
    }

    @Override
    public void onClick(View view) {
        password1 = ((EditText) findViewById(R.id.signup_password)).getText().toString();
        password2 = ((EditText) findViewById(R.id.signup_password2)).getText().toString();
        firstname = ((EditText) findViewById(R.id.signup_firstname)).getText().toString();
        lastname = ((EditText) findViewById(R.id.signup_lastname)).getText().toString();

        if (findViewById(R.id.button_cancel).equals(view)) {
            finish();
        } else {
            checkInputFields();
        }
    }

    private void checkInputFields() {
        if (isEmpty(firstname)) {
            alertMsg("Sign up failed.", "Enter your first name.");
        } else if (isEmpty(lastname)) {
            alertMsg("Sign up failed.", "Enter your last name");
        } else if (!(firstname.matches("[a-zA-Z]+")) || !(lastname.matches("[a-zA-Z]+"))) {
            alertMsg("Sign up failed.", "Only use letters in your name.");
        } else if (isEmpty(password1) || isEmpty(password2)) {
            alertMsg("Sign up failed.", "Enter a password.");
        } else if (!password1.equals(password2)) {
            alertMsg("Sign up failed.", "Passwords do not match.");
        } else {
            processSignup();
        }
    }

    /**
     * Networking with Parse for signup
     */
    private void processSignup() {
        startLoading();
        setupParse();
    }

    /**
     * initialize database object for this user.
     */
    private void setupParse() {
        final ParseUser user = new ParseUser();

        user.setUsername(username);
        user.setPassword(password1);
        user.setEmail(username);
        user.put("firsttime", true);
        user.put("email_domain","ucsd");
        user.put("first_name",firstname);
        user.put("last_name",lastname);
        // Call the Parse signup method
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                stopLoading();
                if (e != null) {
                    if (e.getCode() == 100)
                        alertMsg("Connection failed.", "Check your internet connection.");
                    else
                        alertMsg("Sign up failed.", e.getMessage());
                } else {
                    ParseObject profile = new ParseObject("Profile");
                    profile.put("user_object_id", user.getObjectId());
                    profile.put("first_name", firstname);
                    profile.put("last_name", lastname);
                    profile.put("email_domain","ucsd");
                    // email domain
                    profile.saveInBackground();

                    clearAlltext();
                    finishTag = true;
                    alertMsg("Sign up successful.", "You have successfully signed up. You can now login.");
                }
            }
        });
    }

    /**
     * Helper function to clear all text fields in current activity
     */
    //this function will be using when need to clear the text user entered in the textfields
    protected void clearAlltext() {
        ViewGroup textFields = (ViewGroup) findViewById(R.id.signup_textFields);
        for (int i = 0, count = textFields.getChildCount(); i < count; ++i) {
            View view = textFields.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
        }
    }

    /**
     * show an alert message on current activity.
     *
     * @param title
     * @param msg
     */
    protected void alertMsg(String title, String msg) {
        //build dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //clear msg
                        clearAlltext();
                        if (finishTag) {
                            finish();
                        }
                    }
                });
        //create alert dialog
        AlertDialog alert = builder.create();
        //show dialog on screen
        alert.show();
    }

    protected void startLoading() {
        proDialog = new ProgressDialog(this);
        proDialog.setMessage("Signing up...");
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        proDialog.setCancelable(false);
        proDialog.show();
    }

    protected void stopLoading() {
        proDialog.dismiss();
        proDialog = null;
    }

    public boolean isEmpty(String s) {
        return s.trim().length() > 0 ? false : true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
}
