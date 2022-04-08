package us.careydevelopment.ecosystem.business.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import us.careydevelopment.ecosystem.business.exception.UnknownUserException;
import us.careydevelopment.ecosystem.business.model.User;
import us.careydevelopment.ecosystem.business.model.UserLightweight;
import us.careydevelopment.ecosystem.business.repository.UserRepository;


@Component
public class SessionUtil {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        if (user == null) throw new UnknownUserException("Do not recognize " + authentication.getName());

        return user;
    }

    public UserLightweight getCurrentUserLightweight() {
        User user = getCurrentUser();

        UserLightweight lightweight = new UserLightweight();
        BeanUtils.copyProperties(user, lightweight);

        return lightweight;
    }

}
