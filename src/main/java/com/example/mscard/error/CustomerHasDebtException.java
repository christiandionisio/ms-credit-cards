package com.example.mscard.error;

public class CustomerHasDebtException extends Exception{

    public CustomerHasDebtException() {
        super("Customer has debt");
    }
}
