package us.careydevelopment.accounting.harness;

import us.careydevelopment.accounting.model.User;

public class UserLightweightHarness {

    public static final String ID = "34922";
    public static final String USERNAME = "mrsmith";

    public static User getMrSmithUserLightweight() {
        final User lightweight = new User();
        lightweight.setId(ID);
        lightweight.setUsername(USERNAME);

        return lightweight;
    }
}
