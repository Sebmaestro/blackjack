package c17sal.cs.umu.se.blackjacklab3.controller.gameActivity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

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

    private ArrayList<Card> allDecks;
    //private ArrayList<Card> firstDeck;
    //private ArrayList<Card> secondDeck;
    private ArrayList<Card> dealerHand;
    private ArrayList<Card> playerHand;

    private LinearLayout dealerLinear;
    private LinearLayout playerLinear;

    private TextView dealerScoreTextView;
    private TextView playerScoreTextView;

    private Button hit;
    private Button stand;
    private Button doubleDown;
    private Button split;

    private int dealerScore;
    private int playerScore;
    private int playerCurrentCard;
    private int dealerCurrentCard;

    private boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*--------------------------------------------------*/
        initialize();



        /*game.getStartingValue((Card) allDecks.get(0).get(0), (Card) allDecks.get(0).get(1),
                (Card) allDecks.get(0).get(2), (Card) allDecks.get(0).get(3));*/
        /*
        do
        {

        }while (!gameOver);*/








    }

    private void initialize()
    {
        game = new Game();
        dealerHand = new ArrayList<>();
        playerHand = new ArrayList<>();

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
        //firstDeck = allDecks.get(0);
        //secondDeck = allDecks.get(1);

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

        dealerScore = allDecks.get(0).getValue();
        playerScore = allDecks.get(1).getValue() + allDecks.get(2).getValue();

        setScores();

        dealStartingCardsForDealer(R.id.dealer1);
        //dealStartingCardsForPlayer(R.id.dealer2);
        dealStartingCardsForPlayer(R.id.player1);
        dealStartingCardsForPlayer(R.id.player2);
    }

    public void stand()
    {
        if (game.canDealerDraw(dealerHand))
        {
            ImageView imageView =
                    (ImageView) dealerLinear.getChildAt(dealerLinear.getChildCount()-dealerCurrentCard);
            imageView.setImageResource(allDecks.get(0).getImageId());
            imageView.setVisibility(View.VISIBLE);
            dealerScore = dealerScore + allDecks.get(0).getValue();
            dealerCurrentCard++;
            setScores();
            dealerHand.add(allDecks.remove(0));
        }
    }

    public void hit()
    {
        ImageView imageView =
                (ImageView) playerLinear.getChildAt(playerLinear.getChildCount() - (playerCurrentCard));
        imageView.setImageResource(allDecks.get(0).getImageId());
        imageView.setVisibility(View.VISIBLE);
        playerScore = playerScore + allDecks.get(0).getValue();
        playerCurrentCard++;
        setScores();
        game.checkPlayerValue(playerScore);
        playerHand.add(allDecks.remove(0));
    }

    private void setScores()
    {
        dealerScoreTextView = findViewById(R.id.dealerTextView);
        playerScoreTextView = findViewById(R.id.playerTextView);

        dealerScoreTextView.setText("Dealer: " + dealerScore);
        playerScoreTextView.setText("Player: " + playerScore);
    }

    private void dealStartingCardsForDealer(int id)
    {
        ImageView imageview = findViewById(id);
        imageview.setImageResource(allDecks.get(0).getImageId());
        imageview.setVisibility(View.VISIBLE);

        dealerHand.add(allDecks.remove(0));
        dealerCurrentCard = 1;
    }

    private void dealStartingCardsForPlayer(int id)
    {
        //Card card = (Card) firstDeck.get(0);
        ImageView imageview = findViewById(id);
        imageview.setImageResource(allDecks.get(0).getImageId());
        imageview.setVisibility(View.VISIBLE);

        playerHand.add(allDecks.remove(0));
        playerCurrentCard = 2;
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
                        deck.add(new Card(Card.Suit.CLUBS, (i+2),
                                clubsArray.getResourceId(i,
                                -1)));
                        break;

                    //Spades
                    case (1):
                        deck.add(new Card(Card.Suit.SPADES, (i+2), spadesArray.getResourceId(i,
                                -1)));
                        break;

                    //Hearts
                    case (2):
                        deck.add(new Card(Card.Suit.HEARTS, (i+2), heartsArray.getResourceId(i,
                                -1)));
                        break;

                    //Diamonds
                    case (3):
                        deck.add(new Card(Card.Suit.DIAMONDS, (i+2), diamondsArray.getResourceId(i,
                                -1)));
                }
            }
        }

        Collections.shuffle(deck);
        return deck;
    }

}
