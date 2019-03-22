package c17sal.cs.umu.se.blackjacklab3.model;

import java.util.ArrayList;

public class Decks
{
    private int deckId;
    private ArrayList<Card> decks;

    public Decks()
    {
        decks = new ArrayList();
    }

    public void addDeck(ArrayList<Card> deck)
    {
        //decks.add(deck);
        for (Card card:deck )
        {
            decks.add(card);
        }
    }

    public ArrayList<Card> getDecks()
    {
        return decks;
    }
}
