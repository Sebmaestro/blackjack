package c17sal.cs.umu.se.blackjacklab3.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable
{
    private Suit suit;
    private int value;
    private int imageId;
    private boolean hasBeenCounted;

    public enum Suit
    {
        SPADES, CLUBS, HEARTS, DIAMONDS
    }



    public Card(Suit suit, int value, int imageId)
    {
        this.suit = suit;
        this.imageId = imageId;
        hasBeenCounted = false;

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

    public void setValue(int value)
    {
        this.value = value;
    }

    public void setHasBeenCounted(boolean hasBeenCounted)
    {
        this.hasBeenCounted = hasBeenCounted;
    }

    public boolean isHasBeenCounted()
    {
        return hasBeenCounted;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.suit == null ? -1 : this.suit.ordinal());
        dest.writeInt(this.value);
        dest.writeInt(this.imageId);
        dest.writeByte(this.hasBeenCounted ? (byte) 1 : (byte) 0);
    }

    protected Card(Parcel in)
    {
        int tmpSuit = in.readInt();
        this.suit = tmpSuit == -1 ? null : Suit.values()[tmpSuit];
        this.value = in.readInt();
        this.imageId = in.readInt();
        this.hasBeenCounted = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>()
    {
        @Override
        public Card createFromParcel(Parcel source)
        {
            return new Card(source);
        }

        @Override
        public Card[] newArray(int size)
        {
            return new Card[size];
        }
    };
}
