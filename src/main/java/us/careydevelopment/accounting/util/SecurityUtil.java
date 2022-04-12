package us.careydevelopment.accounting.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.careydevelopment.accounting.model.UserLightweight;
import us.careydevelopment.ecosystem.jwt.constants.Authority;

@Component
public class SecurityUtil {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityUtil.class);

    @Autowired
    private SessionUtil sessionUtil;

    public boolean isAuthorizedByUserName(String userName) {
        boolean authorized = false;

        UserLightweight user = sessionUtil.getCurrentUser();

        if (user != null && userName != null) {
            if (user.getUsername() != null) {
                if (user.getUsername().equals(userName)) {
                    authorized = true;
                } else {
                    //if the user is an admin, can do anything
                    if (user.getAuthorityNames() != null && user.getAuthorityNames().contains(Authority.ADMIN_ECOSYSTEM_USER)) {
                        authorized = true;
                    }
                }
            }
        }

        return authorized;
    }
}
