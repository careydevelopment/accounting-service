package us.careydevelopment.ecosystem.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import us.careydevelopment.ecosystem.business.exception.InvalidRequestException;
import us.careydevelopment.ecosystem.business.exception.NotAuthorizedException;
import us.careydevelopment.ecosystem.business.exception.NotFoundException;
import us.careydevelopment.ecosystem.business.model.Business;
import us.careydevelopment.ecosystem.business.model.UserLightweight;
import us.careydevelopment.ecosystem.business.repository.BusinessRepository;
import us.careydevelopment.ecosystem.business.util.SecurityUtil;
import us.careydevelopment.ecosystem.business.util.SessionUtil;

import java.util.Optional;

@Service
public class BusinessService {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessService.class);

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessValidationService validationService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private SecurityUtil securityUtil;

    public Business save(final Business business, final BindingResult bindingResult) throws InvalidRequestException {
        validationService.validate(business, bindingResult);

        final UserLightweight userLightweight = sessionUtil.getCurrentUserLightweight();
        business.setOwner(userLightweight);

        final Business returnedBusiness = businessRepository.save(business);
        return returnedBusiness;
    }

    public Business update(final Business business, final BindingResult bindingResult) throws InvalidRequestException,
            NotFoundException, NotAuthorizedException {

        validationService.validate(business, bindingResult, false);

        final String id = business.getId();
        final Optional<Business> oldBusinessOpt = businessRepository.findById(id);

        if (oldBusinessOpt.isPresent()) {
            final Business persistedBusiness = oldBusinessOpt.get();

            final boolean authorized = securityUtil.isAuthorizedByUserId(persistedBusiness.getOwner().getId());

            if (authorized) {
                BeanUtils.copyProperties(business, persistedBusiness);
                businessRepository.save(persistedBusiness);

                return persistedBusiness;
            } else {
                throw new NotAuthorizedException("You don't have authority to update that business");
            }
        } else {
            throw new NotFoundException("Business with ID " + id + " doesn't exist");
        }
    }

    public Business retrieve(final String id) throws NotFoundException {
        Optional<Business> business = businessRepository.findById(id);

        if (business.isEmpty()) throw new NotFoundException("Business with ID " + id + " doesn't exist");

        return business.get();
    }
}
