package us.careydevelopment.accounting.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import us.careydevelopment.accounting.model.UserLightweight;

import javax.servlet.http.HttpServletRequest;


@Component
@RequestScope
public class SessionUtil {

    private UserLightweight userLightweight;
    private String bearerToken;

    public void init(HttpServletRequest request) {
        bearerToken = request.getHeader("Authorization");
    }

    public void init(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public UserLightweight getCurrentUser() {
        if (userLightweight == null) {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String username = authentication.getPrincipal().toString();
            if (authentication.getPrincipal() instanceof User) {
                username = (((User) authentication.getPrincipal()).getUsername());
            }

            //TODO: may need to go to the user service to get the whole object
            userLightweight = new UserLightweight();
            userLightweight.setUsername(username);
            userLightweight.setAuthorities(authentication.getAuthorities());
        }

        return userLightweight;
    }
}
