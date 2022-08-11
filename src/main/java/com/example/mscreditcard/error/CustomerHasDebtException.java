package com.example.mscreditcard.error;

public class CustomerHasDebtException extends Exception{

    public CustomerHasDebtException() {
        super("Customer has debt");
    }
}
