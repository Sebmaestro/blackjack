package c17sal.cs.umu.se.blackjacklab3.model;

public class Card
{
    private Suit suit;
    private String value;
    private int imageId;

    public enum Suit
    {
        SPADES, CLUBS, HEARTS, DIAMONDS
    }

    public Card(Suit suit, String value, int imageId)
    {
        this.suit = suit;
        this.imageId = imageId;


        switch (value)
        {
            case "14":
                this.value = "ace";
                break;
            case "13":
                this.value = "king";
                break;
            case "12":
                this.value = "queen";
                break;
            case "11":
                this.value = "jack";
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

    public String getValue()
    {
        return value;
    }

    public int getImageId()
    {
        return imageId;
    }
}
