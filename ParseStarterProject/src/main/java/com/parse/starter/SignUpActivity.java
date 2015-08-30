package com.parse.starter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        confirmButton = (Button) findViewById(R.id.button_confirm);
        confirmButton.setOnClickListener(this);
        cancelButton = (Button) findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        username = ((EditText) findViewById(R.id.signup_username)).getText().toString().toLowerCase();
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
        String email_s = username.substring(username.length() - 3);
        email_s.replaceAll("\\s+", "");
        if (isEmpty(username)) {
            alertMsg("Sign Up Failed", "Please Enter an Email Address");
        } else if (isEmpty(password1) || isEmpty(password2)) {
            alertMsg("Sign Up Failed", "Please Enter Password");
        } else if (!password1.equals(password2)) {
            alertMsg("Sign Up Failed", "The passwords do not match");
        } else if (isEmpty(firstname) || isEmpty(lastname)) {
            alertMsg("Sign Up Failed", "Please Enter Firstname or Lastname");
        } else if (!(firstname.matches("[a-zA-Z]+")) || !(lastname.matches("[a-zA-Z]+"))) {
            alertMsg("Sign Up Failed", "Please only enter letters for names.");
        } else if (!email_s.equals("edu")) {
            alertMsg("Sign Up Failed", "Please enter a valid College Email Address.");
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
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password1);
        user.setEmail(username);
        user.put("firstname", firstname);
        user.put("lastname", lastname);
        user.put("firsttime", true);
        // Call the Parse signup method
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                stopLoading();
                if (e != null) {
                    if (e.getCode() == 100)
                        alertMsg("Connection Failed", "Please check your Internet connection");
                    else
                        alertMsg("User Sign Up Failed", e.getMessage());
                } else {
                    clearAlltext();
                    finishTag = true;
                    alertMsg("Success!", "You have successfully signed up.");
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
        proDialog.setMessage("Signing Up...Please Wait");
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
