package us.careydevelopment.accounting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AccountingController {
	
    private static final Logger LOG = LoggerFactory.getLogger(AccountingController.class);

//    @Autowired
//    private BusinessService businessService;
//
//    @Autowired
//    private SessionUtil sessionUtil;
//
//    @PostMapping("/businesses")
//    public ResponseEntity<IRestResponse<Business>> createBusiness(final HttpServletRequest request,
//                                                                  @Valid @RequestBody final Business business,
//                                                                  final BindingResult bindingResult)
//                                                                    throws InvalidRequestException {
//        LOG.debug("Adding business: " + business);
//
//        final Business returnedBusiness = businessService.save(business, bindingResult);
//
//        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully created!",
//                HttpStatus.CREATED.value(),
//                returnedBusiness);
//    }
//
//    @PutMapping("/businesses")
//    public ResponseEntity<IRestResponse<Business>> updateBusiness(final HttpServletRequest request,
//                                                                  @Valid @RequestBody final Business business,
//                                                                  final BindingResult bindingResult)
//                                                                    throws InvalidRequestException,
//                                                                            NotFoundException, NotAuthorizedException {
//        LOG.debug("Updating business: " + business);
//
//        final Business returnedBusiness = businessService.update(business, bindingResult);
//
//        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully updated!",
//                HttpStatus.OK.value(),
//                returnedBusiness);
//    }
//
//    @GetMapping("/businesses/{id}")
//    public ResponseEntity<IRestResponse<Business>> retrieveBusiness(final HttpServletRequest request,
//                                                                  final @PathVariable String id) throws NotFoundException {
//        LOG.debug("Locating business by ID: " + id);
//
//        final Business business = businessService.retrieve(id);
//
//        return ResponseEntityUtil.createSuccessfulResponseEntity("Found business",
//                HttpStatus.OK.value(),
//                business);
//    }
}
