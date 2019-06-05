package c17sal.cs.umu.se.blackjacklab3.model;

import java.io.Serializable;

public class Money implements Serializable
{
    private double money;
    private static Money single_instance = null;

    private Money()
    {
        money = 1000;
    }

    public static Money getInstance()
    {
        if (single_instance == null)
            single_instance = new Money();

        return single_instance;
    }

    public double getMoney()
    {
        return money;
    }

    public void setMoney(double money)
    {
        this.money = money;
    }

    public void subtractMoney(double money)
    {
        this.money = this.money - money;
    }

    public void addMoney(double money)
    {
        this.money = this.money + money;
    }
}
