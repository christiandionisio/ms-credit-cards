package com.example.mscreditcard.error;

public class CustomerHasCreditDebtException extends Exception {
    public CustomerHasCreditDebtException() {
        super("Customer has credit debt");
    }
}
