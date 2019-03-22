package c17sal.cs.umu.se.blackjacklab3.model;

import java.util.ArrayList;

public class Game
{
    public void getStartingValue(Card playerValue1, Card playerValue2, Card dealerValue1,
                                 Card dealerValue2)
    {

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
            return "alive";
        }
    }

    public boolean canDealerDraw(ArrayList<Card> dealerHand)
    {
        int value = 0;
        for (Card card : dealerHand)
        {
            value = value + card.getValue();
        }

        if (value >=17)
        {
            return false;
        }
        else
            return true;

    }
}
