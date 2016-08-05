package com.example.pushpika.voicerecordchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EmailAddressEntryActivity extends AppCompatActivity {
    public static String EmailAddress;
    public String tempEmailAddress;
    EditText email_entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_address_entry);
        email_entry = (EditText) findViewById(R.id.Email_address);
    }

    public void sendEmailAddress(View view){
        View focusView = null;
        tempEmailAddress = email_entry.getText().toString();
        // Check for a valid email address.
        if (TextUtils.isEmpty(tempEmailAddress)) {
            email_entry.setError(getString(R.string.error_field_required));
            focusView = email_entry;
            focusView.requestFocus();
        } else if (!isEmailValid(tempEmailAddress)) {
            email_entry.setError(getString(R.string.error_invalid_email));
            focusView = email_entry;
            focusView.requestFocus();
        }
        else{
            EmailAddressEntryActivity.EmailAddress = tempEmailAddress;
            Intent intent = new Intent(this,UserRegister.class);
            startActivity(intent);
        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
}
