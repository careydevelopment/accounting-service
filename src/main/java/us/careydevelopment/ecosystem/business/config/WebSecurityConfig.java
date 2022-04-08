package us.careydevelopment.ecosystem.business.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import us.careydevelopment.ecosystem.jwt.config.JwtOnlySecurityConfig;
import us.careydevelopment.ecosystem.jwt.constants.Authority;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends JwtOnlySecurityConfig {

    protected String[] getAllowedAuthorities() {
        return new String[] { Authority.BASIC_ECOSYSTEM_USER, Authority.ADMIN_ECOSYSTEM_USER };
    }

//    @Override
//    protected String[] permitAllUrls() {
//        String[] permitAll = {  };
//        return permitAll;
//    }


    public WebSecurityConfig(@Autowired JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.authenticationProvider = jwtAuthenticationProvider;
    }
}
