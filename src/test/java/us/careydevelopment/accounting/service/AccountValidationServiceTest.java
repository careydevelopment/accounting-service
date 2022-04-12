package us.careydevelopment.accounting.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import us.careydevelopment.accounting.harness.AccountHarness;
import us.careydevelopment.accounting.harness.UserLightweightHarness;
import us.careydevelopment.accounting.repository.AccountRepository;
import us.careydevelopment.accounting.util.SecurityUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountValidationServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private AccountValidationService accountValidationService;

    @Test
    public void testValidateOwnershipAsOwner() {
        when(securityUtil.isAuthorizedByUserName(UserLightweightHarness.USERNAME)).thenReturn(true);
        when(accountRepository.findById(AccountHarness.TELEPHONE_ACCOUNT_ID)).thenReturn(Optional.of(AccountHarness.getTelephoneExpenseAccount()));

        final boolean isOwner = accountValidationService.validateOwnership(AccountHarness.TELEPHONE_ACCOUNT_ID);

        assertTrue(isOwner);
    }

    @Test
    public void testValidateOwnershipAsNonOwner() {
        when(securityUtil.isAuthorizedByUserName(UserLightweightHarness.USERNAME)).thenReturn(false);
        when(accountRepository.findById(AccountHarness.TELEPHONE_ACCOUNT_ID)).thenReturn(Optional.of(AccountHarness.getTelephoneExpenseAccount()));

        final boolean isOwner = accountValidationService.validateOwnership(AccountHarness.TELEPHONE_ACCOUNT_ID);

        assertFalse(isOwner);
    }
}
