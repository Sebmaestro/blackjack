package c17sal.cs.umu.se.blackjacklab3.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import c17sal.cs.umu.se.blackjacklab3.R;

public class HelpActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Method onBackPressed:
     * Finishes this activity and returns to the mainActivity when back button
     * is pressed. The main activity is then started from scratch
     */
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(HelpActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }

}
