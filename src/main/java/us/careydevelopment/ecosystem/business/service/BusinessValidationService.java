package us.careydevelopment.ecosystem.business.service;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import us.careydevelopment.ecosystem.business.exception.InvalidRequestException;
import us.careydevelopment.ecosystem.business.model.Business;
import us.careydevelopment.ecosystem.business.repository.BusinessRepository;
import us.careydevelopment.util.api.model.ValidationError;
import us.careydevelopment.util.api.validation.ValidationUtil;

import java.util.List;

@Service
public class BusinessValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessValidationService.class);

    @Autowired
    private BusinessRepository businessRepository;

    public void validate(final Business business, final BindingResult bindingResult) throws InvalidRequestException {
        validate(business, bindingResult, true);
    }

    public void validate(final Business business, final BindingResult bindingResult, final boolean isNew) throws InvalidRequestException {
        final List<ValidationError> validationErrors = ValidationUtil.convertBindingResultToValidationErrors(bindingResult);
        LOG.error("Validation errors: " + validationErrors);

        customValidation(business, validationErrors, isNew);

        if (validationErrors.size() > 0) throw new InvalidRequestException("Validation errors!", validationErrors);
    }

    private void customValidation(final Business business, List<ValidationError> validationErrors, final boolean isNew) {
        if (isNew) {
            validateUniqueName(business, validationErrors);
        }
    }

    @VisibleForTesting
    void validateUniqueName(final Business business, List<ValidationError> validationErrors) {
        final String name = business.getName();
        if (!StringUtils.isBlank(name)) {
            final Business match = businessRepository.findByName(name);

            if (match != null) {
                ValidationUtil.addError(validationErrors, "Business name is taken", "name", null);
            }
        }
    }
}
