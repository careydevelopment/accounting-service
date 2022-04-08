package us.careydevelopment.ecosystem.business.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.careydevelopment.ecosystem.business.controller.BusinessController;
import us.careydevelopment.ecosystem.business.model.Country;
import us.careydevelopment.ecosystem.business.model.UsState;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JsonLoader {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessController.class);

    private static final String STATES = "states.json";
    private static final String COUNTRIES = "countries.json";

    private final ObjectMapper mapper = new ObjectMapper();

    public void load() {
        loadStates();
        loadCountries();
    }

    private void loadStates() {
        InputStream is = getClass().getClassLoader().getResourceAsStream(STATES);
        String json = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        try {
            List<UsState> states = mapper.readValue(json, new TypeReference<List<UsState>>() {});
            UsStates.set(states);
        } catch (Exception e) {
            LOG.error("Problem loading states!", e);
        }
    }

    private void loadCountries() {
        InputStream is = getClass().getClassLoader().getResourceAsStream(COUNTRIES);
        String json = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        try {
            List<Country> countries = mapper.readValue(json, new TypeReference<List<Country>>() {});
            Countries.set(countries);
        } catch (Exception e) {
            LOG.error("Problem loading countries!", e);
        }
    }
}
