package c17sal.cs.umu.se.blackjacklab3.controller.gameActivity;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayList<Card> splitHand;

    private LinearLayout dealerLinear;
    private LinearLayout playerLinear;
    private LinearLayout playerLinearSplit;

    private TextView dealerScoreTextView;
    private TextView playerScoreTextView;
    private TextView splitScoreTextView;

    private TextView dealerCardsTextView;
    private TextView playerCardsTextView;
    private TextView splitCardsTextView;

    private ImageView imageView;

    private Button hit;
    private Button stand;
    private Button doubleDown;
    private Button split;
    private Button playAgain;

    private int dealerScore;
    private int playerScore;
    private int dealerStartScore;
    private int playerStartScore;
    private int splitScore1;
    private int splitScore2;


    private boolean gameOver = false;
    private boolean startScores = true;
    private boolean canDouble = true;
    private boolean hasBeenSplit = false;
    private boolean firstHand = true;

    private ConstraintLayout layout;

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

        layout = findViewById(R.id.Constraintlayout);
        initialize();
        newGame();



        /*game.getStartingValue((Card) allDecks.get(0).get(0), (Card) allDecks.get(0).get(1),
                (Card) allDecks.get(0).get(2), (Card) allDecks.get(0).get(3));*/
        /*
        do
        {

        }while (!gameOver);*/








    }

    private void initialize()
    {
        playerCardsTextView = findViewById(R.id.playerCardsTextView);
        splitCardsTextView = findViewById(R.id.splitCardsTextView);
        splitCardsTextView.setVisibility(View.INVISIBLE);
        splitScoreTextView = findViewById(R.id.splitScoreTextView);
        splitScoreTextView.setVisibility(View.INVISIBLE);

        Decks decks = new Decks();
        clubsArray = getResources().obtainTypedArray(R.array.clubs);
        spadesArray = getResources().obtainTypedArray(R.array.spades);
        heartsArray = getResources().obtainTypedArray(R.array.hearts);
        diamondsArray = getResources().obtainTypedArray(R.array.diamonds);

        for (int i = 0; i < 2; i++)
        {
            decks.addDeck(initDeck());
        }
        allDecks = decks.getDecks();

        doubleDown = findViewById(R.id.doubleButton);
        //doubleDown.setVisibility(View.INVISIBLE);
        doubleDown.setEnabled(false);
        doubleDown.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        split = findViewById(R.id.splitButton);
        //split.setVisibility(View.INVISIBLE);
        split.setEnabled(false);
        split.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);

        playAgain = findViewById(R.id.playAgainButton);
        playAgain.setVisibility(View.INVISIBLE);

        dealerLinear = findViewById(R.id.linearLayoutDealer);
        playerLinear = findViewById(R.id.linearLayoutPlayer);
        playerLinearSplit = findViewById(R.id.linearLayoutSplit);
    }

    public void newGame()
    {

        canDouble = true;

        game = new Game();
        dealerHand = new ArrayList<>();
        playerHand = new ArrayList<>();


        dealerLinear.removeAllViews();
        playerLinear.removeAllViews();
        playerLinearSplit.removeAllViews();


        playAgain.setVisibility(View.INVISIBLE);



        dealStartingCardsForDealer();
        dealStartingCardsForPlayer();
        dealStartingCardsForPlayer();
        playerHand.get(0).setValue(11);
        playerHand.get(1).setValue(11);

        game.calculatePlayerScore(playerHand, "starting");

        dealerStartScore = allDecks.get(0).getValue();
        playerStartScore = game.getPlayerValue();
        setScores("startScores");

        if (game.checkDoubleAndSplit(playerHand).equals("splitPossible"))
        {
            split.setEnabled(true);
            split.getBackground().clearColorFilter();
        }
        if (game.checkDoubleAndSplit(playerHand).equals("doublePossible"))
        {
            doubleDown.setEnabled(true);
            doubleDown.getBackground().clearColorFilter();
        }

    }

    public void split()
    {
        splitHand = new ArrayList<>();
        playerScoreTextView.setText("Split1: ");

        //first or normal hand
        playerLinear.removeViewAt(1);
        createImageAndAddToLayout(allDecks, 0, playerLinear);
        splitHand.add(playerHand.remove(1));
        playerHand.add(allDecks.get(0));
        allDecks.remove(0);
        playerCardsTextView.setText("Split1");
        playerCardsTextView.setTextColor(Color.GREEN);
        playerScoreTextView.setTextColor(Color.GREEN);
        //game.setPlayerValue((game.getPlayerValue()/2)+playerHand.get(1).getValue());
        //playerHand.get(1).setHasBeenCounted(true);


        //split hand
        createImageAndAddToLayout(splitHand, 0, playerLinearSplit);
        createImageAndAddToLayout(allDecks, 0, playerLinearSplit);
        splitHand.add(allDecks.remove(0));
        splitCardsTextView.setText("Split2");
        splitCardsTextView.setVisibility(View.VISIBLE);


        game.setPlayerValue(playerHand.get(0).getValue());
        game.calculatePlayerScore(playerHand, "starting");
        splitScore1 = game.getPlayerValue();

        game.setSplitValue(splitHand.get(0).getValue());
        game.calculatePlayerScore(splitHand, "split");
        splitScore2 = game.getSplitValue();
        splitScoreTextView.setVisibility(View.VISIBLE);
        setScores("bothSplit");



        hasBeenSplit = true;
        split.setEnabled(false);
        split.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
    }

    public void hit()
    {
        if (game.canPlayerDraw(firstHand))
        {
            if (hasBeenSplit && firstHand || !hasBeenSplit)
            {
                playerHand.add(allDecks.get(0));
                game.calculatePlayerScore(playerHand, "starting");
                createImageAndAddToLayout(allDecks, 0, playerLinear);
                playerScore = game.getPlayerValue();
                setScores("player");
            }
            else if (hasBeenSplit && !firstHand)
            {
                splitHand.add(allDecks.get(0));
                game.calculatePlayerScore(splitHand, "split");
                createImageAndAddToLayout(allDecks, 0, playerLinearSplit);
                splitScore2 = game.getSplitValue();
                setScores("split");
            }

            //playerStartScore = playerStartScore + allDecks.get(0).getValue();
            //playerCurrentCard++;

            allDecks.remove(0);

        }


        if (game.checkPlayerValue(playerScore).equals("bust") && firstHand)
        {
            playAgain.setVisibility(View.VISIBLE);
            firstHand = false;
            //game.setPlayerValue(splitHand.get(0).getValue()+splitHand.get(1).getValue());//
            splitHand.get(0).setHasBeenCounted(true);
            splitHand.get(1).setHasBeenCounted(true);
            playerCardsTextView.setTextColor(Color.BLACK);
            playerScoreTextView.setTextColor(Color.BLACK);
            playerScoreTextView.setText("Bust");
            splitCardsTextView.setTextColor(Color.GREEN);
            splitScoreTextView.setTextColor(Color.GREEN);
            //stand();
        }
        else if (game.checkPlayerValue(splitScore2).equals("bust"))
        {
            playAgain.setVisibility(View.VISIBLE);
            splitScoreTextView.setText("Bust");
            splitCardsTextView.setTextColor(Color.BLACK);
            splitScoreTextView.setTextColor(Color.BLACK);
        }
    }

    private void createImageAndAddToLayout(ArrayList<Card> playerHand, int i, LinearLayout linearLayout)
    {
        imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(150, 250));
        imageView.setImageResource(playerHand.get(i).getImageId());
        linearLayout.addView(imageView);
    }

    public void stand()
    {
        if (!hasBeenSplit || hasBeenSplit && !firstHand)
        {
            splitCardsTextView.setTextColor(Color.BLACK);
            splitScoreTextView.setTextColor(Color.BLACK);
            while (game.canDealerDraw(dealerHand))
            {
                createImageAndAddToLayout(allDecks, 0, dealerLinear);
                dealerScore = game.getDealerValue();
                dealerHand.add(allDecks.remove(0));
            }
            dealerScore = game.getDealerValue();
            setScores("dealer");
            playAgain.setVisibility(View.VISIBLE);

        }
        //The second split hand is going
        else
        {
            playAgain.setVisibility(View.VISIBLE);
            firstHand = false;
            game.setPlayerValue(splitHand.get(0).getValue()+splitHand.get(1).getValue());
            splitHand.get(0).setHasBeenCounted(true);
            splitHand.get(1).setHasBeenCounted(true);
            playerCardsTextView.setTextColor(Color.BLACK);
            playerScoreTextView.setTextColor(Color.BLACK);
            splitCardsTextView.setTextColor(Color.GREEN);
            splitScoreTextView.setTextColor(Color.GREEN);
        }
    }

    public void doubleDown()
    {
        if (canDouble)
        {
            hit();
            /*
            playerHand.add(allDecks.get(0));
            game.calculatePlayerScore(playerHand, "starting");
            createImageAndAddToLayout(allDecks, 0, playerLinear);

            playerScore = game.getPlayerValue();
            setScores("player");
            allDecks.remove(0);*/

            canDouble = false;
        }
    }



    private void setScores(String id)
    {
        dealerScoreTextView = findViewById(R.id.dealerScoreTextView);
        playerScoreTextView = findViewById(R.id.playerScoreTextView);

        switch (id)
        {
            case ("player"):
                playerScoreTextView.setText("Player: " + playerScore);
                break;

            case ("dealer"):
                dealerScoreTextView.setText("Dealer: " + dealerScore);
                break;

            case ("startScores"):
                dealerScoreTextView.setText("Dealer: " + dealerStartScore);
                playerScoreTextView.setText("Player: " + playerStartScore);
                break;

            case ("bothSplit"):
                playerScoreTextView.setText("Split1: "+splitScore1);
                splitScoreTextView.setText("Split2: "+splitScore2);
                break;

            case ("split"):
                splitScoreTextView.setText("Split2: "+splitScore2);
        }
    }

    private void dealStartingCardsForDealer()
    {
        //ImageView imageview = findViewById(id);
        createImageAndAddToLayout(allDecks, 0, dealerLinear);

        dealerHand.add(allDecks.remove(0));
    }

    private void dealStartingCardsForPlayer()
    {
        //Card card = (Card) firstDeck.get(0);
        //ImageView imageview = findViewById(id);
        //imageView = new ImageView(this);
        //imageView.setLayoutParams(new ViewGroup.LayoutParams(150,250));
        //imageView.setMaxHeight(75);
        //imageView.setMinimumHeight(75);
        //imageView.setMaxWidth(50);
        //imageView.setMinimumWidth(50);
        //imageView.setImageResource(allDecks.get(0).getImageId());
        //imageView.setVisibility(View.VISIBLE);
        //imageView.setImageResource(allDecks.get(0).getImageId());
        //imageView.setVisibility(View.VISIBLE);
        //playerLinear.addView(imageView);

        createImageAndAddToLayout(allDecks, 0, playerLinear);



        playerHand.add(allDecks.remove(0));
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
