package us.careydevelopment.ecosystem.business.util;

import us.careydevelopment.ecosystem.business.model.UsState;

import java.util.ArrayList;
import java.util.List;

public class UsStates {

    private static List<UsState> STATES = new ArrayList<>();

    static void set(List<UsState> usStates) {
        STATES = usStates;
    }

    public static List<UsState> get() {
        return STATES;
    }

    public static boolean isValid(String state) {
        boolean valid = false;

        for (UsState usState : STATES) {
            if (usState.getTwoLetterCode().equalsIgnoreCase(state.trim())) {
                valid = true;
                break;
            }
        }

        return valid;
    }
}
