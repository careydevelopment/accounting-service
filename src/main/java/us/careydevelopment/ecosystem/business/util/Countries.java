package us.careydevelopment.ecosystem.business.util;

import org.apache.commons.lang3.StringUtils;
import us.careydevelopment.ecosystem.business.model.Country;
import us.careydevelopment.ecosystem.business.model.UsState;

import java.util.ArrayList;
import java.util.List;

public class Countries {

    private static List<Country> COUNTRIES = new ArrayList<>();

    static void set(List<Country> countries) {
        COUNTRIES = countries;
    }

    public static List<Country> get() {
        return COUNTRIES;
    }

    public static boolean isValid(String country) {
        boolean valid = false;

        for (Country c : COUNTRIES) {
            if (c.getTwoLetterCode().equalsIgnoreCase(country.trim())) {
                valid = true;
                break;
            }
        }

        return valid;
    }
}
