package us.careydevelopment.ecosystem.business.model;

public enum BusinessType {
    //some B2B payments even go to individuals,
    //like "John Smith" for Smith's Lawn Care, Inc.
    //That's what this type is for
    INDIVIDUAL,

    //for corporations, LLCs, partnerships, etc.
    BUSINESS;
}
