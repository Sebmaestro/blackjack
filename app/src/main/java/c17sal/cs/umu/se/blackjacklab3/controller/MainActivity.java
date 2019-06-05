/**
 * C17sal Sebastian Arledal
 * Ej färdigt program än
 * Saknas: Multitouch, pengasystem, databas, nån form av information till användaren hur spelet
 * fungerar.
 * Eventuella buggar kan finnas
 *
 */

package c17sal.cs.umu.se.blackjacklab3.controller;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.io.Serializable;

import c17sal.cs.umu.se.blackjacklab3.R;
import c17sal.cs.umu.se.blackjacklab3.controller.gameActivity.GameActivity;
import c17sal.cs.umu.se.blackjacklab3.model.Money;
import c17sal.cs.umu.se.blackjacklab3.model.database.MoneyHelper;

public class MainActivity extends AppCompatActivity
{

    private TextView moneyTextView;
    private RadioGroup radioGroup;
    private double bet;
    private Money money;
    private Button reset;


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case R.id.helpButtonReal:
                infoPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        money = money.getInstance();
        moneyTextView = findViewById(R.id.moneyTextView);
        moneyTextView.setText("Current money: " + Double.toString(money.getMoney()));
        radioGroup = findViewById(R.id.radioGroup);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (money.getMoney() < 50)
        {
            Toast.makeText(this,
                    "You lost the game!", Toast.LENGTH_LONG).show();

            money.setMoney(1000);
        }

        radioGroup.clearCheck();
        money = Money.getInstance();
        moneyTextView.setText("Current money: " + Double.toString(money.getMoney()));

    }

    public void checkButton(View view)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();

        RadioButton radioButton = findViewById(radioId);

        CharSequence i = radioButton.getText();
        if ("50".equals(i))
        {
            bet = 50;
        }
        else if ("100".equals(i))
        {
            bet = 100;
        }
        else if ("200".equals(i))
        {
            bet = 200;
        }
        else if ("500".equals(i))
        {
            bet = 500;
        }
    }

    public void startGame(View view)
    {
        if (bet > money.getMoney())
        {
            Toast.makeText(this,
                    "Not enough money", Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(MainActivity.this,
                    GameActivity.class);
            intent.putExtra("bet", bet);
            startActivity(intent);
            //finish();
        }
    }

    public void infoPressed()
    {
        Intent intent = new Intent(MainActivity.this, HelpActivity.class);
        startActivity(intent);
    }

    public void resetMoney(View view)
    {
        money.setMoney(1000);
    }

    private void extractFromDatabase(MoneyHelper dbHelper)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection =
                    {
                        BaseColumns._ID,
                        MoneyHelper.PlayerEntry.COLUMN_NAME_MONEY
                    };

        String selection = MoneyHelper.PlayerEntry.COLUMN_NAME_MONEY + " = ?";
        String[] selectionArgs = {"money"};

        Cursor cursor = db.query(
                MoneyHelper.PlayerEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null, null, null);

        while(cursor.moveToNext())
        {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(MoneyHelper.PlayerEntry._ID));
            System.out.println(itemId);
        }

    }

    private void insertToDatabase(MoneyHelper dbHelper)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MoneyHelper.PlayerEntry.COLUMN_NAME_MONEY, 1000);
        long newRowId = db.insert(MoneyHelper.PlayerEntry.TABLE_NAME, null, values);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
