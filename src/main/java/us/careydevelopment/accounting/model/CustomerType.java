package us.careydevelopment.accounting.model;

public enum CustomerType {
    //if we know the customer's details
    //like shipping address, billing address, etc.
    KNOWN_INDIVIDUAL,

    //for high-traffic retail shops or
    //restaurants where we don't know
    //much about our customers...
    //ex: Daily Sales
    AGGREGATE,

    //for rare occasions when the customer
    //is a specific individual who's also
    //part of a business
    INDIVIDUAL_AND_BUSINESS,

    //for B2B accounting
    BUSINESS;
}
