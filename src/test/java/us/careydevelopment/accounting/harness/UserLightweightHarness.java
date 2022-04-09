package us.careydevelopment.accounting.harness;

import us.careydevelopment.accounting.model.UserLightweight;

public class UserLightweightHarness {

    public static final String ID = "34922";
    public static final String USERNAME = "mrsmith";

    public static UserLightweight getMrSmithUserLightweight() {
        final UserLightweight lightweight = new UserLightweight();
        lightweight.setId(ID);
        lightweight.setUsername(USERNAME);

        return lightweight;
    }
}
