package us.careydevelopment.ecosystem.business.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import us.careydevelopment.ecosystem.business.model.Business;
import us.careydevelopment.ecosystem.business.model.Industry;
import us.careydevelopment.ecosystem.business.repository.BusinessRepository;
import us.careydevelopment.ecosystem.business.repository.IndustryRepository;
import us.careydevelopment.ecosystem.business.util.JsonLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Component
public class ApplicationListenerInitialize implements ApplicationListener<ApplicationReadyEvent>  {

    @Autowired
    private BusinessRepository repo;

    @Autowired
    private JsonLoader jsonLoader;

    public void onApplicationEvent(ApplicationReadyEvent event) {
        repo.deleteAll();

        List<Business> list = repo.findAll();
        list.forEach(System.err::println);

        jsonLoader.load();
    }
}