package c17sal.cs.umu.se.blackjacklab3.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Game
{
    private int dealerValue;
    private int playerValue;
    private int splitValue;

    private boolean dealerHasAce;
    private boolean playerHasAce;
    private boolean hasBeenCounted = true;

    public Game()
    {
        dealerValue = 0;
        dealerHasAce = false;
        playerHasAce = false;
    }

    public void getStartingValue(Card playerValue1, Card playerValue2, Card dealerValue1,
                                 Card dealerValue2)
    {

    }

    public void calculateBet(Money money, double bet, boolean hasBeenSplit, int playerScore,
                             int splitScore1, int splitScore2, int dealerScore,
                             boolean wasDoubled, boolean firstSplitDoubleExecuted,
                             boolean secondSplitDoubleExecuted, ArrayList<Card> playerHand)
    {
        double winMoney;
        if (!hasBeenSplit)
        {
            winMoney = 0;
            // BJ
            if (playerScore == 21 && dealerScore != 21 && playerHand.size() == 2)
            {
                winMoney = bet*2.5;
            }
            // Win or dealer bust
            else if (playerScore <= 21 && (playerScore > dealerScore || dealerScore > 21))
            {
                winMoney = bet*2;
            }
            // draw
            else if(playerScore <= 21 && playerScore == dealerScore)
            {
                winMoney = bet;
            }
            // double?
            if (wasDoubled)
            {
                winMoney = winMoney *2;
            }
            money.addMoney(winMoney);
        }
        else
        {
            if (!hasBeenCounted)
            {
                winMoney = 0;
                // split 1

                if (splitScore1 <= 21 && (splitScore1 > dealerScore || dealerScore > 21))
                {
                    winMoney = bet*2;
                }
                // draw
                else if(splitScore1 <= 21 && splitScore1 == dealerScore)
                {
                    winMoney = bet;
                }
                // double?
                if (firstSplitDoubleExecuted)
                {
                    winMoney = winMoney *2;
                }
                money.addMoney(winMoney);


                winMoney = 0;
                // split 2
                if (splitScore2 <= 21 && (splitScore2 > dealerScore || dealerScore > 21))
                {
                    winMoney = bet*2;
                }
                // draw
                else if(splitScore2 <= 21 && splitScore2 == dealerScore)
                {
                    winMoney = bet;
                }
                // double?
                if (secondSplitDoubleExecuted)
                {
                    winMoney = winMoney *2;
                }
                money.addMoney(winMoney);
            }
        }
        hasBeenCounted = false;
    }


    public Boolean checkDouble(ArrayList<Card> playerHand)
    {
        if (playerHand.get(0).getValue() + playerHand.get(1).getValue() >=9 &&
                playerHand.get(0).getValue() + playerHand.get(1).getValue() <= 11)
        {
            return true;
        }
        else if ((playerHand.get(0).getValue() == 11 || playerHand.get(1).getValue() == 11) &&
                playerHand.get(0).getValue() + playerHand.get(1).getValue() != 21)
        {
            return true;
        }
        else
            return false;
    }

    public Boolean checkSplit(ArrayList<Card> playerHand)
    {
        if (playerHand.get(0).getValue() == playerHand.get(1).getValue())
        {
            return true;
        }

        /*else if (playerHand.get(0).getValue() == playerHand.get(1).getValue() &&
                 playerHand.get(0).getValue() + playerHand.get(1).getValue() >=9 &&
                 playerHand.get(0).getValue() + playerHand.get(1).getValue() <= 11)
        {
            return "splitAndDoublePossible";
        }*/
        else
            return false;
    }

    public String checkPlayerValue(int value)
    {
        if (value == 21)
        {
            return "blackjack";
        }
        else if (value > 21)
        {
            return "bust";
        }
        else
        {
            return "Make a choice";
        }
    }

    public void setPlayerValue(int playerValue)
    {
        this.playerValue = playerValue;
    }

    public void setSplitValue(int splitValue)
    {
        this.splitValue = splitValue;
    }

    public int getDealerValue()
    {
        return dealerValue;
    }

    public int getPlayerValue()
    {
        return playerValue;
    }

    public int getSplitValue()
    {
        return splitValue;
    }

    public void calculatePlayerScore(ArrayList<Card> playerHand, String hand)
    {
        if (hand.equals("starting"))
        {
            for (Card card:playerHand)
            {
                if (!card.isHasBeenCounted())
                {
                    if (card.getValue() == 11)
                    {
                        playerHasAce = true;
                    }

                    playerValue = playerValue + card.getValue();

                    if (playerValue > 21 && playerHasAce)
                    {
                        playerValue = playerValue - 10;
                        playerHasAce = false;
                    }
                    card.setHasBeenCounted(true);
                }
            }
        }
        else
        {
            for (Card card:playerHand)
            {
                if (!card.isHasBeenCounted())
                {
                    if (card.getValue() == 11)
                    {
                        playerHasAce = true;
                    }

                    splitValue = splitValue + card.getValue();

                    if (splitValue > 21 && playerHasAce)
                    {
                        splitValue = splitValue - 10;
                        playerHasAce = false;
                    }
                    card.setHasBeenCounted(true);
                }
            }
        }

    }

    public boolean canPlayerDraw(boolean firstHand)
    {
        if (firstHand)
        {
            if (playerValue >= 21)
            {
                return false;
            }
            else
                return true;
        }
        else
        {
            if (splitValue >= 21)
            {
                return false;
            }
            else
                return true;
        }
    }

    public boolean canDealerDraw(ArrayList<Card> dealerHand)
    {
        for (Card card : dealerHand)
        {
            if (!card.isHasBeenCounted())
            {
                if (card.getValue() == 11)
                {
                    dealerHasAce = true;
                }

                dealerValue = dealerValue + card.getValue();

                if (dealerValue > 21 && dealerHasAce)
                {
                    dealerValue = dealerValue - 10;
                    dealerHasAce = false;
                }
                card.setHasBeenCounted(true);
            }
        }

        if (dealerValue >=17)
        {
            return false;
        }
        else
            return true;

    }
}
