package com.example.andrea.androiduser.tickets;

/**
 *
 * @author Manuele
 */
public class MultiType implements TicketType{
    private int numberOfTravels;
    private final double cost;
    
    public MultiType(int numberOfTravels) {
        cost = 1.5*numberOfTravels - 1;
        this.numberOfTravels = numberOfTravels;
    }

    public MultiType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getNumberOfTravels() {
        return numberOfTravels;
    }
    
    @Override
    public double getCost() {
        return cost;
    }
    
    @Override
    public String getType() {
        return "Multi";
    }
}
