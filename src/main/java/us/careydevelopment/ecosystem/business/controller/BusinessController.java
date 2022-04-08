package us.careydevelopment.ecosystem.business.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import us.careydevelopment.ecosystem.business.exception.InvalidRequestException;
import us.careydevelopment.ecosystem.business.exception.NotAuthorizedException;
import us.careydevelopment.ecosystem.business.exception.NotFoundException;
import us.careydevelopment.ecosystem.business.model.Business;
import us.careydevelopment.ecosystem.business.service.BusinessService;
import us.careydevelopment.ecosystem.business.util.SessionUtil;
import us.careydevelopment.util.api.model.IRestResponse;
import us.careydevelopment.util.api.response.ResponseEntityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
public class BusinessController {
	
    private static final Logger LOG = LoggerFactory.getLogger(BusinessController.class);

    @Autowired
    private BusinessService businessService;

    @Autowired
    private SessionUtil sessionUtil;

    @PostMapping("/businesses")
    public ResponseEntity<IRestResponse<Business>> createBusiness(final HttpServletRequest request,
                                                                  @Valid @RequestBody final Business business,
                                                                  final BindingResult bindingResult)
                                                                    throws InvalidRequestException {
        LOG.debug("Adding business: " + business);

        final Business returnedBusiness = businessService.save(business, bindingResult);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully created!",
                HttpStatus.CREATED.value(),
                returnedBusiness);
    }

    @PutMapping("/businesses")
    public ResponseEntity<IRestResponse<Business>> updateBusiness(final HttpServletRequest request,
                                                                  @Valid @RequestBody final Business business,
                                                                  final BindingResult bindingResult)
                                                                    throws InvalidRequestException,
                                                                            NotFoundException, NotAuthorizedException {
        LOG.debug("Updating business: " + business);

        final Business returnedBusiness = businessService.update(business, bindingResult);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully updated!",
                HttpStatus.OK.value(),
                returnedBusiness);
    }

    @GetMapping("/businesses/{id}")
    public ResponseEntity<IRestResponse<Business>> retrieveBusiness(final HttpServletRequest request,
                                                                  final @PathVariable String id) throws NotFoundException {
        LOG.debug("Locating business by ID: " + id);

        final Business business = businessService.retrieve(id);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Found business",
                HttpStatus.OK.value(),
                business);
    }
}
