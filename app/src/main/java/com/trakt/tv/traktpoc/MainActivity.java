package com.trakt.tv.traktpoc;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText userId, password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userId = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(this);
    }

    public void onClick(View view) {
        String authString="hemanth";
        if(userId.getText().toString().equalsIgnoreCase(authString) && password.getText().toString().equalsIgnoreCase(authString))
        {
            Intent i= new Intent(MainActivity.this,MovieList.class);
            startActivity(i);
        }
        else
        {
            Toast.makeText(MainActivity.this, "Incorrect Login Credentials, Please try again.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
