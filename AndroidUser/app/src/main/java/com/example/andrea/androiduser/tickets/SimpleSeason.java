package com.example.andrea.androiduser.tickets;

/**
 * Created by Andrea on 09/07/2017.
 */

public class SimpleSeason implements Product{

    private String description;
    private String type;
    private double monthlyCost;
    private int duration;


    public SimpleSeason(String description, String type, double monthlyCost,int duration){
        this.description=description;
        this.type=type;
        this.monthlyCost=monthlyCost;
        this.duration=duration;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getType() {
        return type;
    }

    //TODO valutare se sia meglio chiamare questo metodo ora opuure nel costruttore e in set duration con aggiunta di attributo
    @Override
    public double getCost() {
        return monthlyCost*duration;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    public String toString(){
        String toString = new String(" **Product:");

        toString += " *Type: "+type;
        toString += "* *Desciption: "+description;
        toString += "* *MonthlyCost: "+monthlyCost;
        toString += "* *Duration in month: "+duration;
        toString += "**";

        return toString;
    }
}