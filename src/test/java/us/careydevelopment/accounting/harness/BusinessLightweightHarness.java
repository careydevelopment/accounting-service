package us.careydevelopment.accounting.harness;

import us.careydevelopment.accounting.model.BusinessLightweight;
import us.careydevelopment.accounting.model.BusinessType;

public class BusinessLightweightHarness {

    public static final String BUSINESS_NAME = "My Widgets, Inc.";
    public static final BusinessType BUSINESS_TYPE = BusinessType.BUSINESS;
    public static final String ID = "14444";

    public static BusinessLightweight getValidBusinessLightweightNoPerson() {
        BusinessLightweight lightweight = new BusinessLightweight();
        lightweight.setName(BUSINESS_NAME);
        lightweight.setBusinessType(BUSINESS_TYPE);
        lightweight.setId(ID);
        lightweight.setPerson(null);

        return lightweight;
    }
}
