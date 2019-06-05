/**
 *
 */


//TODO - Debugga hela programmet men även fixa rotationsgrejen
//TODO - Rotationen fungerar inte alls i nuläget


package c17sal.cs.umu.se.blackjacklab3.controller.gameActivity;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
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
import c17sal.cs.umu.se.blackjacklab3.model.Money;
import c17sal.cs.umu.se.blackjacklab3.model.Multitouch;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener
{
    private double bet;

    private Game game;
    private Multitouch multitouch;
    private Money money;

    private TypedArray clubsArray;
    private TypedArray spadesArray;
    private TypedArray heartsArray;
    private TypedArray diamondsArray;

    private ArrayList<Card> allDecks;
    private ArrayList<Card> dealerHand;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> splitHand;

    private LinearLayout dealerLinear;
    private LinearLayout playerLinear;
    private LinearLayout playerLinearSplit;

    private TextView dealerScoreTextView;
    private TextView playerScoreTextView;
    private TextView splitScoreTextView;
    private TextView currentMoneyTextView;

    private TextView dealerCardsTextView;
    private TextView playerCardsTextView;
    private TextView splitCardsTextView;

    private ImageView imageView;

    private Button hitButton;
    private Button standButton;
    private Button doubleDownButton;
    private Button splitButton;
    private Button playAgainButton;

    private int dealerScore;
    private int playerScore;
    private int dealerStartScore;
    private int playerStartScore;
    private int splitScore1;
    private int splitScore2;
    private int nrOfStands;
    private int nrOfDoubles;
    private int nrOfSplitDoubles;

    private boolean firstDouble = false;
    private boolean doNotCount = false;
    private boolean hasBeenCounted = true;
    private boolean gameOver = false;
    private boolean startScores = true;
    private boolean hasBeenSplit;
    private boolean firstHand;
    private boolean secondHandDone = false;
    private boolean wasDoubled = false;
    private boolean doubleSplitOneAvailable = false;
    private boolean doubleSplitTwoAvailable = false;
    private boolean firstSplitDoubleExecuted = false;
    private boolean secondSplitDoubleExecuted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bet = getIntent().getDoubleExtra("bet", 0);

        /*--------------------------------------------------*/


        initialize(savedInstanceState);


        if (savedInstanceState != null)
        {
            useSavedState(savedInstanceState);
        }
        else
        {
            newGame();
        }
    }

    /**
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_POINTER_UP:
            {
                int count = event.getPointerCount();
                if (count == 2)
                {
                    System.out.println("2 finger");
                    hit();
                }
                else if (count == 3)
                {
                    System.out.println("3 finger");
                    //stand();
                }
                break;
            }

            /*
            case MotionEvent.ACTION_DOWN: {
                int count = event.getPointerCount();
                if (count == 1)
                {
                    System.out.println("walla");
                }
            }*/
        }
        return true;
    }

    /**
     *
     * @param savedInstanceState
     */
    private void useSavedState(Bundle savedInstanceState)
    {
        playAgainButton.setVisibility(View.INVISIBLE);
        firstHand = savedInstanceState.getBoolean("firsthand");
        game = new Game();

        allDecks = savedInstanceState.getParcelableArrayList("alldecks");

        restoreCardsView(savedInstanceState);

        playerHand.get(0).setHasBeenCounted(false);
        playerHand.get(1).setHasBeenCounted(false);

        if (playerScore <= 21)
        {
            playerScore = savedInstanceState.getInt("playerscore");
            playerScoreTextView.setText("Player: " + playerScore);
        }

        if (dealerHand.size() == 1)
        {
            dealerStartScore = savedInstanceState.getInt("dealerstartscore");
            dealerScoreTextView.setText("Dealer: " + dealerStartScore);
        }
        else
        {
            dealerScore = savedInstanceState.getInt("dealerscore");
            dealerScoreTextView.setText("Dealer: " + dealerScore);
        }

        boolean[] arr = savedInstanceState.getBooleanArray("buttonStateArr");


        checkButtonsForTrueOrFalse(arr);


        //TODO Debugga split vid rotation
        hasBeenSplit = savedInstanceState.getBoolean("hasbeensplit");
        if (hasBeenSplit)
        {
            splitHand.get(0).setHasBeenCounted(false);
            splitHand.get(1).setHasBeenCounted(false);

            splitScore1 = savedInstanceState.getInt("splitscore1");
            playerScoreTextView.setText("Split1: " + splitScore1);
            splitScore2 = savedInstanceState.getInt("splitscore2");
            splitScoreTextView.setText("Split2: " + splitScore2);
            splitScoreTextView.setVisibility(View.VISIBLE);

            playerCardsTextView.setText("Split1");
            playerCardsTextView.setTextColor(Color.GREEN);
            playerScoreTextView.setTextColor(Color.GREEN);
            splitCardsTextView.setText("Split2");
            splitCardsTextView.setVisibility(View.VISIBLE);

            //setScores("bothSplit");

            if (game.checkPlayerValue(splitScore2).equals("bust"))
            {
                playAgainButton.setVisibility(View.VISIBLE);
            }

        }

        //Här går något fel vid rotation
        if (game.checkPlayerValue(playerScore).equals("bust") || !game.canDealerDraw(dealerHand))
        {
            playAgainButton.setVisibility(View.VISIBLE);
        }

    }

    private void checkButtonsForTrueOrFalse(boolean[] arr)
    {
        if (arr[0])
            enableDoubleOrSplitButton(doubleDownButton);
        else
            disableDoubleorSplitButton(doubleDownButton);

        if (arr[1])
            enableDoubleOrSplitButton(splitButton);
        else
            disableDoubleorSplitButton(splitButton);

        if (arr[2])
            hitButton.setEnabled(true);
        else
            hitButton.setEnabled(false);

        if (arr[3])
            standButton.setEnabled(true);
        else
            standButton.setEnabled(false);
    }

    /**
     *
     * @param savedInstanceState
     */
    private void restoreCardsView(Bundle savedInstanceState)
    {
        playerHand = savedInstanceState.getParcelableArrayList("playerhand");
        for (int i = 0; i < playerHand.size(); i++)
        {
            createImageAndAddToLayout(playerHand, i, playerLinear);
        }

        dealerHand = savedInstanceState.getParcelableArrayList("dealerhand");
        if (dealerHand.size() != 0)
        {
            for (int i = 0; i < dealerHand.size(); i++)
            {
                createImageAndAddToLayout(dealerHand, i, dealerLinear);
            }
        }

        if (savedInstanceState.getParcelableArrayList("splithand") != null)
        {
            splitHand = savedInstanceState.getParcelableArrayList("splithand");

            for (int i = 0; i < splitHand.size(); i++)
            {
                createImageAndAddToLayout(splitHand, i, playerLinearSplit);
            }
        }
    }

    /**
     *
     * @param bundle
     */
    @Override
    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("alldecks", allDecks);
        bundle.putParcelableArrayList("playerhand", playerHand);
        bundle.putParcelableArrayList("dealerhand", dealerHand);
        bundle.putBoolean("firsthand", firstHand);

        if (splitHand != null)
            bundle.putParcelableArrayList("splithand", splitHand);

        bundle.putInt("playerscore", playerScore);
        bundle.putInt("dealerscore", dealerScore);
        bundle.putInt("splitscore1", splitScore1);
        bundle.putInt("splitscore2", splitScore2);
        bundle.putInt("dealerstartscore", dealerStartScore);
        bundle.putInt("playerstartscore", playerStartScore);
        bundle.putInt("nrofstands", nrOfStands);
        bundle.putInt("nrofdoubles", nrOfDoubles);
        bundle.putBoolean("hasbeensplit", hasBeenSplit);
        boolean[] boolArr = new boolean[4];

        boolArr[0] = doubleDownButton.isEnabled();

        boolArr[1] = splitButton.isEnabled();

        boolArr[2] = hitButton.isEnabled();

        boolArr[3] = standButton.isEnabled();

        bundle.putBooleanArray("buttonStateArr", boolArr);

        //Mer grejer kanske
    }

    private void initialize(Bundle state)
    {
        dealerScoreTextView = findViewById(R.id.dealerScoreTextView);
        playerScoreTextView = findViewById(R.id.playerScoreTextView);

        money = money.getInstance();
        currentMoneyTextView = findViewById(R.id.currentMoneyTextField);
        currentMoneyTextView.setText("Current money = " + Double.toString(money.getMoney()));

        if (state == null) money.subtractMoney(bet);

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

        for (int i = 0; i < 4; i++)
        {
            decks.addDeck(initDeck());
        }
        allDecks = decks.getDecks();

        playAgainButton = findViewById(R.id.playAgainButton);
        standButton = findViewById(R.id.standButton);
        hitButton = findViewById(R.id.hitButton);
        doubleDownButton = findViewById(R.id.doubleButton);

        splitButton = findViewById(R.id.splitButton);

        dealerLinear = findViewById(R.id.linearLayoutDealer);
        playerLinear = findViewById(R.id.linearLayoutPlayer);
        playerLinearSplit = findViewById(R.id.linearLayoutSplit);
    }

    public void newGame()
    {
        currentMoneyTextView.setText("Current money = " + Double.toString(money.getMoney()));

        disableDoubleorSplitButton(doubleDownButton);
        disableDoubleorSplitButton(splitButton);

        playAgainButton.setVisibility(View.INVISIBLE);
        standButton.setEnabled(true);
        hitButton.setEnabled(true);

        firstHand = true;
        hasBeenSplit = false;
        doNotCount = false;

        nrOfStands = 0;
        nrOfDoubles = 0;

        playerCardsTextView.setText("Player cards");
        splitCardsTextView.setVisibility(View.INVISIBLE);
        splitScoreTextView.setVisibility(View.INVISIBLE);

        game = new Game();
        dealerHand = new ArrayList<>();
        playerHand = new ArrayList<>();

        dealerLinear.removeAllViews();
        playerLinear.removeAllViews();
        playerLinearSplit.removeAllViews();

        dealStartingCardsForDealer();
        dealStartingCardsForPlayer();
        dealStartingCardsForPlayer();

        /*
        playerHand.get(0).setValue(5);
        playerHand.get(1).setValue(5);
        allDecks.get(0).setValue(5);
        allDecks.get(1).setValue(5);
        allDecks.get(2).setValue(11);
        allDecks.get(3).setValue(11);
*/
        game.calculatePlayerScore(playerHand, "starting");

        dealerStartScore = dealerHand.get(0).getValue();
        playerStartScore = game.getPlayerValue();
        playerScore = playerStartScore;
        setScores("startScores");

        if (game.checkSplit(playerHand))
        {
            enableDoubleOrSplitButton(splitButton);
        }
        if (game.checkDouble(playerHand))
        {
            enableDoubleOrSplitButton(doubleDownButton);
        }

        if (game.checkPlayerValue(playerStartScore).equals("blackjack"))
        {
            Toast.makeText(GameActivity.this,
                    "You got blackjack!", Toast.LENGTH_LONG).show();
            stand();
        }
    }

    private void disableDoubleorSplitButton(Button button)
    {
        button.setEnabled(false);
        button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
    }

    public void split()
    {
        if (money.getMoney() >= bet)
        {
            money.subtractMoney(bet);
            currentMoneyTextView.setText("Current money: "+Double.toString(money.getMoney()));

            splitHand = new ArrayList<>();

            disableDoubleorSplitButton(doubleDownButton);


            playerScoreTextView.setText("Split1: ");
            playerLinear.removeViewAt(1);
            createImageAndAddToLayout(allDecks, 0, playerLinear);
            splitHand.add(playerHand.remove(1));
            playerHand.add(allDecks.remove(0));
            playerCardsTextView.setText("Split1");
            playerCardsTextView.setTextColor(Color.GREEN);
            playerScoreTextView.setTextColor(Color.GREEN);


            createImageAndAddToLayout(splitHand, 0, playerLinearSplit);
            createImageAndAddToLayout(allDecks, 0, playerLinearSplit);
            splitHand.add(allDecks.remove(0));
            splitCardsTextView.setText("Split2");
            splitCardsTextView.setVisibility(View.VISIBLE);


            game.setPlayerValue(playerHand.get(0).getValue());
            game.calculatePlayerScore(playerHand, "starting");
            splitScore1 = game.getPlayerValue();

            game.setSplitValue(splitHand.get(0).getValue());
            game.calculatePlayerScore(splitHand, "splitButton");
            splitScore2 = game.getSplitValue();
            splitScoreTextView.setVisibility(View.VISIBLE);
            setScores("bothSplit");


            hasBeenSplit = true;
            disableDoubleorSplitButton(splitButton);

            if (game.checkDouble(playerHand))
            {
                doubleSplitOneAvailable = true;
                enableDoubleOrSplitButton(doubleDownButton);

                if(game.checkDouble(splitHand))
                {
                    doubleSplitTwoAvailable = true;
                }
            }

            if (game.checkPlayerValue(splitScore1).equals("blackjack"))
            {
                Toast.makeText(GameActivity.this,
                        "You got blackjack!", Toast.LENGTH_LONG).show();
                stand();
            }
            if (game.checkPlayerValue(splitScore2).equals("blackjack") && game.checkPlayerValue(splitScore1).equals("blackjack"))
            {
                Toast.makeText(GameActivity.this,
                        "You got double blackjack!", Toast.LENGTH_LONG).show();
                stand();
            }
        } else
        {
            Toast.makeText(GameActivity.this,
                    "Not enough money for split", Toast.LENGTH_LONG).show();
        }
    }

    public void hit()
    {
        if (game.canPlayerDraw(firstHand))
        if (game.canPlayerDraw(firstHand))
        {
            //Första handen i splitten eller ingen split
            if (hasBeenSplit && firstHand || !hasBeenSplit)
            {
                playerHand.add(allDecks.get(0));
                game.calculatePlayerScore(playerHand, "starting");
                createImageAndAddToLayout(allDecks, 0, playerLinear);
                playerScore = game.getPlayerValue();
                setScores("player");
                allDecks.remove(0);

                if (hasBeenSplit)
                {
                    splitScore1 = game.getPlayerValue();
                }

                //Första handen i split
                if (hasBeenSplit)
                {
                    if (game.checkPlayerValue(splitScore1).equals("blackjack"))
                    {
                        splitCardsTextView.setTextColor(Color.GREEN);
                        splitScoreTextView.setTextColor(Color.GREEN);

                        playerCardsTextView.setTextColor(Color.BLACK);
                        playerScoreTextView.setTextColor(Color.BLACK);

                        Toast.makeText(GameActivity.this,
                                "You got blackjack!", Toast.LENGTH_LONG).show();
                        firstDouble = true;
                        stand(); //TODO - Debugga här när det e tänkt att båda dubblar o får 21
                    }
                    else if (game.checkPlayerValue(splitScore1).equals("bust"))
                    {
                        firstHand = false;
                        playerCardsTextView.setTextColor(Color.BLACK);
                        playerScoreTextView.setTextColor(Color.BLACK);
                        playerScoreTextView.setText("Bust");
                        splitCardsTextView.setTextColor(Color.GREEN);
                        splitScoreTextView.setTextColor(Color.GREEN);
                    }
                }
                //ingen split första handen
                else
                {
                    if (game.checkPlayerValue(playerScore).equals("blackjack"))
                    {
                        Toast.makeText(GameActivity.this,
                                "You got blackjack!", Toast.LENGTH_LONG).show();
                        stand();
                    }
                    else if (game.checkPlayerValue(playerScore).equals("bust"))
                    {
                        playerScoreTextView.setText("Bust");
                        playAgainButton.setVisibility(View.VISIBLE);
                    }
                }
            }
            //Andra handen i splitten
            else if (hasBeenSplit && !firstHand)
            {
                splitHand.add(allDecks.get(0));
                game.calculatePlayerScore(splitHand, "splitButton");
                createImageAndAddToLayout(allDecks, 0, playerLinearSplit);
                splitScore2 = game.getSplitValue();
                setScores("splitButton");
                allDecks.remove(0);
                if (game.checkPlayerValue(splitScore2).equals("blackjack"))
                {
                    Toast.makeText(GameActivity.this,
                            "You got blackjack!", Toast.LENGTH_LONG).show();
                    stand();
                }
                else if (game.checkPlayerValue(splitScore2).equals("bust"))
                {
                    playAgainButton.setVisibility(View.VISIBLE);
                    splitScoreTextView.setText("Bust");
                    splitCardsTextView.setTextColor(Color.BLACK);
                    splitScoreTextView.setTextColor(Color.BLACK);
                }
            }

            //allDecks.remove(0);

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
        nrOfStands++;
        //Dealern drar kort
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
            playAgainButton.setVisibility(View.VISIBLE);
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            splitButton.setEnabled(false);
            doubleDownButton.setEnabled(false);

        }
        //The second splithand is going
        else
        {
            if (game.checkDouble(splitHand))
            {
                doubleSplitTwoAvailable = true;

                enableDoubleOrSplitButton(doubleDownButton);

            }
            else
            {
                disableDoubleorSplitButton(doubleDownButton);
            }
            //playAgainButton.setVisibility(View.VISIBLE);
            firstHand = false;
            game.setPlayerValue(splitHand.get(0).getValue() + splitHand.get(1).getValue());
            splitHand.get(0).setHasBeenCounted(true);
            splitHand.get(1).setHasBeenCounted(true);
            playerCardsTextView.setTextColor(Color.BLACK);
            playerScoreTextView.setTextColor(Color.BLACK);
            splitCardsTextView.setTextColor(Color.GREEN);
            splitScoreTextView.setTextColor(Color.GREEN);
        }

        if (!hasBeenSplit)
        {
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
        } else if (hasBeenSplit && nrOfStands == 2)
        {
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            splitButton.setEnabled(false);
            doubleDownButton.setEnabled(false);
            playAgainButton.setVisibility(View.VISIBLE);
        }

        //--------------------------------------------------

        if (!doNotCount || nrOfStands != 3)
        {
            game.calculateBet(money, bet, hasBeenSplit, playerScore, splitScore1, splitScore2,
                    dealerScore, wasDoubled, firstSplitDoubleExecuted, secondSplitDoubleExecuted,
                    playerHand);
        }
    }

    private void enableDoubleOrSplitButton(Button button)
    {
        button.setEnabled(true);
        button.getBackground().clearColorFilter();
    }

    public void doubleDown()
    {

        if (money.getMoney() >= bet)
        {
            money.subtractMoney(bet);
            currentMoneyTextView.setText("Current money: "+Double.toString(money.getMoney()));
            wasDoubled = true;
            nrOfDoubles++;


            //TODO - Borde va fixad
            if (doubleSplitOneAvailable && doubleSplitTwoAvailable)
            {
                nrOfSplitDoubles++;
            }
            else if (doubleSplitOneAvailable && !doubleSplitTwoAvailable)
            {
                firstSplitDoubleExecuted = true;
            }
            else if (!doubleSplitOneAvailable && doubleSplitTwoAvailable)
            {
                secondSplitDoubleExecuted = true;
            }

            if (nrOfSplitDoubles == 2)
            {
                firstSplitDoubleExecuted = true;
                secondSplitDoubleExecuted = true;
            }


            hit();
            if (!hasBeenSplit)
            {
                disableDoubleorSplitButton(doubleDownButton);
            }
            else if (nrOfDoubles == 2)
            {
                disableDoubleorSplitButton(doubleDownButton);
            }
            if (hasBeenSplit)
            {
                if (nrOfDoubles == 2)
                {
                    doNotCount = true;
                }
            }

            if (!firstDouble)
            {
                stand();
            }

        }
        else
        {
            Toast.makeText(this,
                    "Not enough money to double", Toast.LENGTH_LONG).show();
        }

    }


    private void setScores(String id)
    {
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
                playerScoreTextView.setText("Split1: " + splitScore1);
                splitScoreTextView.setText("Split2: " + splitScore2);
                break;

            case ("splitButton"):
                splitScoreTextView.setText("Split2: " + splitScore2);
        }
    }

    private void dealStartingCardsForDealer()
    {
        createImageAndAddToLayout(allDecks, 0, dealerLinear);
        dealerHand.add(allDecks.remove(0));
    }

    private void dealStartingCardsForPlayer()
    {
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
                        deck.add(new Card(Card.Suit.CLUBS, (i + 2),
                                clubsArray.getResourceId(i,
                                        -1)));
                        break;

                    //Spades
                    case (1):
                        deck.add(new Card(Card.Suit.SPADES, (i + 2), spadesArray.getResourceId(i,
                                -1)));
                        break;

                    //Hearts
                    case (2):
                        deck.add(new Card(Card.Suit.HEARTS, (i + 2), heartsArray.getResourceId(i,
                                -1)));
                        break;

                    //Diamonds
                    case (3):
                        deck.add(new Card(Card.Suit.DIAMONDS, (i + 2), diamondsArray.getResourceId(i,
                                -1)));
                }
            }
        }

        Collections.shuffle(deck);
        return deck;
    }

    public void roundOver(View view)
    {
        finish();
    }

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        final int action = event.getAction();


        switch (action & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
            {
                int pointerCount = event.getPointerCount();
                System.out.println(pointerCount);
                break;
            }
        }
        return true;
    }
}
