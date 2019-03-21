package c17sal.cs.umu.se.blackjacklab3.controller.gameActivity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import c17sal.cs.umu.se.blackjacklab3.R;
import c17sal.cs.umu.se.blackjacklab3.model.Card;
import c17sal.cs.umu.se.blackjacklab3.model.Decks;
import c17sal.cs.umu.se.blackjacklab3.model.Game;

public class GameActivity extends AppCompatActivity
{
    private Game game;
    private TypedArray clubsArray;
    private TypedArray spadesArray;
    private TypedArray heartsArray;
    private TypedArray diamondsArray;

    private ArrayList<ArrayList> allDecks;
    private LinearLayout dealerLinear;
    private LinearLayout playerLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*--------------------------------------------------*/

        clubsArray = getResources().obtainTypedArray(R.array.clubs);
        spadesArray = getResources().obtainTypedArray(R.array.spades);
        heartsArray = getResources().obtainTypedArray(R.array.hearts);
        diamondsArray = getResources().obtainTypedArray(R.array.diamonds);

        Decks decks = new Decks();

        //Nr of decks in play
        for (int i = 0; i < 2; i++)
        {
            decks.addDeck(initDeck());
        }
        allDecks = decks.getDecks();

        dealerLinear = findViewById(R.id.linearLayout4);
        playerLinear = findViewById(R.id.linearLayout3);


        for (int i = 0; i < dealerLinear.getChildCount(); i++)
        {
            dealerLinear.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        for (int i = 0; i < playerLinear.getChildCount(); i++)
        {
            playerLinear.getChildAt(i).setVisibility(View.INVISIBLE);
        }

/*
        if(dealerLinear.getChildCount() > 0)
            dealerLinear.removeAllViews();

        if(playerLinear.getChildCount() > 0)
            playerLinear.removeAllViews();*/


        Card card = (Card) allDecks.get(0).get(0);
        ImageView imageview = findViewById(R.id.startImage1);
        imageview.setImageResource(card.getImageId());
        imageview.setVisibility(View.VISIBLE);
        //dealerLinear.addView(imageview);
        allDecks.get(0).remove(0);

        card = (Card) allDecks.get(0).get(0);
        ImageView imageView2 = findViewById(R.id.startImage2);
        imageView2.setImageResource(card.getImageId());
        imageView2.setVisibility(View.VISIBLE);
        //dealerLinear.addView(imageView2);
        allDecks.get(0).remove(0);

        /*ImageView imageView = findViewById(R.id.startImage1);
        imageView.setVisibility(View.INVISIBLE);
        ImageView two = findViewById(R.id.startImage2);
        two.setVisibility(View.GONE);
        findViewById(R.id.startImage3).setVisibility(View.GONE);





        imageView.setImageResource(card.getImageId());
        imageView.setVisibility(View.VISIBLE);
        allDecks.get(0).remove(0);
        two.setImageResource(findViewById());


        int big = 10;*/
    }

    public ArrayList<Card> initDeck()
    {
        ArrayList<Card> deck = new ArrayList<>();
        for (int i = 0; i < 13; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                switch (j)
                {
                    //Clubs
                    case (0):
                        deck.add(new Card(Card.Suit.CLUBS, Integer.toString(i+2),
                                clubsArray.getResourceId(i,
                                -1)));
                        break;

                    //Spades
                    case (1):
                        deck.add(new Card(Card.Suit.SPADES, Integer.toString(i+2), spadesArray.getResourceId(i,
                                -1)));
                        break;

                    //Hearts
                    case (2):
                        deck.add(new Card(Card.Suit.HEARTS, Integer.toString(i+2), heartsArray.getResourceId(i,
                                -1)));
                        break;

                    //Diamonds
                    case (3):
                        deck.add(new Card(Card.Suit.DIAMONDS, Integer.toString(i+2), diamondsArray.getResourceId(i,
                                -1)));
                }
            }
        }

        Collections.shuffle(deck);
        return deck;
    }

}
