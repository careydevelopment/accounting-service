package us.careydevelopment.ecosystem.business.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import us.careydevelopment.ecosystem.business.model.Industry;
import us.careydevelopment.ecosystem.business.repository.IndustryRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
public class IndustryController {
	
    private static final Logger LOG = LoggerFactory.getLogger(IndustryController.class);

    @Autowired
    private IndustryRepository industryRepository;

    @GetMapping("/industries")
    public List<Industry> fetchAllIndustries(HttpServletRequest request) {
        LOG.debug("Fetching all industries");
        LOG.debug("Remote IP is " + request.getRemoteAddr());
        return industryRepository.findByOrderByNameAsc();
    }
}
