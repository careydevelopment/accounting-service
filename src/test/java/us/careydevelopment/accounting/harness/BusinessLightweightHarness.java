package us.careydevelopment.accounting.harness;

import us.careydevelopment.accounting.model.Business;
import us.careydevelopment.accounting.model.BusinessType;

public class BusinessLightweightHarness {

    public static final String BUSINESS_NAME = "My Widgets, Inc.";
    public static final BusinessType BUSINESS_TYPE = BusinessType.BUSINESS;
    public static final String ID = "14444";

    public static Business getValidBusinessLightweightNoPerson() {
        Business lightweight = new Business();
        lightweight.setName(BUSINESS_NAME);
        lightweight.setBusinessType(BUSINESS_TYPE);
        lightweight.setId(ID);
        lightweight.setPerson(null);

        return lightweight;
    }
}
