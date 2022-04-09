package us.careydevelopment.accounting.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import us.careydevelopment.accounting.exception.UnknownUserException;
import us.careydevelopment.accounting.model.User;
import us.careydevelopment.accounting.model.UserLightweight;
import us.careydevelopment.accounting.repository.UserRepository;


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
