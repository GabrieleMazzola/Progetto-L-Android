package com.example.andrea.androiduser.tickets;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Andrea on 08/07/2017.
 */

public class Sale implements Comparable{

    private String username;
    private long serialCode;
    private Date sellDate;
    private Product product;
    private String sellerMachineIp;


    public Sale(Date saleDate, long serialCode, String username, Product prod, String sellerMachineIp) {
        this.username = username;
        this.serialCode = serialCode;
        this.sellDate = saleDate;
        product = prod;
        this.sellerMachineIp = sellerMachineIp;
    }

    public String getSellerMachineIp() {
        return sellerMachineIp;
    }

    public String getUsername() {
        return username;
    }

    public long getSerialCode() {
        return serialCode;
    }

    public String getType() {
        return product.getType();
    }

    public Date getSellDate() {
        return sellDate;
    }
    //TODO Questo non è un easter egg
    public Date getExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(sellDate);

        String type = product.getType().toUpperCase();
        char t = type.charAt(0);
        return calculateExpiryDate(cal);

    }

    public Product getProduct() {
        return product;
    }

    private Date calculateExpiryDate(Calendar c) {
        String type = product.getType().toUpperCase();
        char t = type.charAt(0);
        switch(t) {
            case 'T':
                c.add(Calendar.MINUTE, product.getDuration());
                break;
            case 'S':
                c.add(Calendar.MONTH, product.getDuration());
        }

        return c.getTime();
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("\n\tSerial Code: ").append(this.serialCode);
        sb.append("\n\nSale Date: ").append(this.getSellDate().toString());
        sb.append("\nExpiry Date: ").append(this.getExpiryDate().toString());
        sb.append("\nSeller Machine IP: ").append(sellerMachineIp);
        sb.append("\n\nProduct:\n ").append(product.toString());

        return sb.toString();
    }


    @Override
    public int compareTo(@NonNull Object o) {
        Sale saleToCompare = (Sale) o;
        return -(getExpiryDate().compareTo(saleToCompare.getExpiryDate()));
    }

    public static Comparator<Sale> saleComparator = new Comparator<Sale>(){
        public int compare(Sale s1, Sale s2){
            return s1.compareTo(s2);
        }
    };
}