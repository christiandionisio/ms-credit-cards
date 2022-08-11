package com.example.mscard.error;

public class CustomerHasCreditDebtException extends Exception {
    public CustomerHasCreditDebtException() {
        super("Customer has credit debt");
    }
}
