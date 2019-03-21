package c17sal.cs.umu.se.blackjacklab3.model;

import java.util.ArrayList;

public class Decks
{
    private int deckId;
    private ArrayList<ArrayList> decks;

    public Decks()
    {
        decks = new ArrayList();
    }

    public void addDeck(ArrayList<Card> deck)
    {
        decks.add(deck);
    }

    public ArrayList<ArrayList> getDecks()
    {
        return decks;
    }
}
