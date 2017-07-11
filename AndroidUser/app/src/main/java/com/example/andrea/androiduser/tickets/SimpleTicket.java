package com.example.andrea.androiduser.tickets;

/**
 * Created by Andrea on 08/07/2017.
 */

public class SimpleTicket implements Product{

    private final String description;
    private final String type;
    private double cost;
    private int duration;

    public SimpleTicket(String description, String type,double cost, int duration) {
        this.description = description;
        this.type=type;
        this.cost = cost;
        this.duration = duration;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public int getDuration() {
        return duration;
    }


    public String toString(){

        StringBuilder sb = new StringBuilder("");

        sb.append("\tDescription: ").append(this.description);
        sb.append("\n\tType: ").append(type);
        sb.append("\n\tCost:").append(cost);
        sb.append("\n\tDuration: ").append(duration);

        return sb.toString();
    }
}
