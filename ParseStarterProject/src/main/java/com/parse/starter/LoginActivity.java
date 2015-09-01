package com.parse.starter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import java.util.Random;

public class LoginActivity extends Activity {

    private Button signUpButton;
    private Button loginButton;
    private EditText usernameField;
    private EditText passwordField;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ar = getActionBar();
        ar.hide();
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);
        signUpButton = (Button) findViewById(R.id.signupButton);
        usernameField = (EditText) findViewById(R.id.loginUsername);
        passwordField = (EditText) findViewById(R.id.loginPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();

                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            if (user.getBoolean("firsttime")) {
                                gotoProfile();
                            } else {
                                gotoMatch();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Email & password do not match. Try again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailPopup();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    private void gotoMatch() {
        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);
    }

    private void gotoSignup(String email) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        intent.putExtra("Email", email);
        startActivity(intent);
    }

    private void gotoProfile() {
        Intent intent = new Intent(this, ProfilePageActivity.class);
        startActivity(intent);
    }

    protected void emailPopup() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your student email.").setIcon(android.R.drawable.ic_dialog_email).setView(input).setPositiveButton("Verify", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                final String k = generateKey(5);
//                System.out.println(k);
                if (isEmailValid(input)) {
                    //send email
                    sendEmail(input.getText().toString().trim().toLowerCase(),k);
                    //PopUp
                    AccessCodePopup(input.getText().toString().trim().toLowerCase(), k);
                } else {
                    if (isEmpty(input.getText().toString())) {
                        Toast.makeText(getApplicationContext(),
                                "Empty Input",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                input.getText().toString() + "is not valid",
                                Toast.LENGTH_SHORT).show();
                    }
                    emailPopup();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    protected void AccessCodePopup(final String email, final String key) {
        final EditText input = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Email: " + email);
        builder.setTitle("Please Enter Your Access Code ").setIcon(android.R.drawable.ic_dialog_alert).setView(input).setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (key.equals(input.getText().toString().trim())) {
                    gotoSignup(email);
                } else {
                    if (isEmpty(input.getText().toString())) {
                        Toast.makeText(getApplicationContext(),
                                "Empty Input",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Access Code is not valid",
                                Toast.LENGTH_SHORT).show();
                    }
                    AccessCodePopup(email, key);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private boolean isEmailValid(EditText et) {
        boolean res = true;
        String email_s = "";
        String s = et.getText().toString();
        if (s.length() > 3) {
            email_s = s.substring(s.length() - 3);
            email_s.replaceAll("\\s+", "");
        }
        if (isEmpty(s) || s.length() < 3 || !email_s.equals("edu")) {
            res = false;
        }
        return res;
    }

    public boolean isEmpty(String s) {
        return s.trim().length() > 0 ? false : true;
    }

    public static String generateKey(int length) {
        String alphabet =
                new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        int n = alphabet.length();

        String result = new String();
        Random r = new Random();

        for (int i = 0; i < length; i++)
            result = result + alphabet.charAt(r.nextInt(n));

        return result;
    }

    public static void sendEmail(final String email, final String code) {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Mail m = new Mail("campusdateapp@gmail.com", "campusdateapp123");

                    String[] toArr = {email};
                    m.setTo(toArr);
                    m.setFrom("campusdateapp@gmail.com");
                    m.setSubject("CampusDate Access Code");
                    m.setBody("Dear New CampusDate User:" + "\n" + email + "\n" +
                            "Here is Your Access Code:" + "\n" + code + "\n" +
                                    "Enjoy & Welcome!" + "\n"
                            + "CampusDate Team"
                    );

                    try {
                        m.send();
                    } catch(Exception e) {
                        Log.e("MailApp", "Could not send email", e);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}



