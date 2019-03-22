package c17sal.cs.umu.se.blackjacklab3.model;

public class Card
{
    private Suit suit;
    private int value;
    private int imageId;

    public enum Suit
    {
        SPADES, CLUBS, HEARTS, DIAMONDS
    }

    public Card(Suit suit, int value, int imageId)
    {
        this.suit = suit;
        this.imageId = imageId;

        switch (value)
        {
            case 14:
                this.value = 11;
                break;
            case 13:
                this.value = 10;
                break;
            case 12:
                this.value = 10;
                break;
            case 11:
                this.value = 10;
                break;
            default:
                this.value = value;
                break;
        }
    }

    public Suit getSuit()
    {
        return suit;
    }

    public void setSuit(Suit suit)
    {
        this.suit = suit;
    }

    public int getValue()
    {
        return value;
    }

    public int getImageId()
    {
        return imageId;
    }
}
