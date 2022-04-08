package us.careydevelopment.ecosystem.business.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import us.careydevelopment.ecosystem.business.model.Business;
import us.careydevelopment.ecosystem.business.repository.BusinessRepository;
import us.careydevelopment.util.api.model.ValidationError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BusinessValidationServiceTest {

    @Mock
    private BusinessRepository businessRepository;

    @InjectMocks
    private BusinessValidationService validationService;

    @Test
    public void testDuplicateBusinessName() {
        final List<ValidationError> validationErrors = new ArrayList<>();

        final Business business = new Business();
        business.setName("Widgets, Inc.");

        when(businessRepository.findByName(business.getName())).thenReturn(new Business());

        validationService.validateUniqueName(business, validationErrors);

        assertEquals(1, validationErrors.size());
    }
}
