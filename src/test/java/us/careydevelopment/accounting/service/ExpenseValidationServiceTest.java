package us.careydevelopment.accounting.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.harness.ExpenseHarness;
import us.careydevelopment.accounting.model.Expense;
import us.careydevelopment.util.api.model.ValidationError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseValidationServiceTest {

    @Mock
    private BusinessService businessService;

    @InjectMocks
    private ExpenseValidationService expenseValidationService;

    @Test
    public void testValidateBusinessExistsWithoutId() {
        final List<ValidationError> errors = new ArrayList<>();

        final Expense expense = ExpenseHarness.getValidTelephoneExpense();
        expense.getPayee().setId(null);

        expenseValidationService.validateBusinessExists(expense, errors);

        verifyNoInteractions(businessService);
    }

    @Test
    public void testValidateBusinessExistsWithInvaliId() {
        final List<ValidationError> errors = new ArrayList<>();

        final Expense expense = ExpenseHarness.getValidTelephoneExpense();

        when(businessService.fetchBusiness(anyString())).thenReturn(null);

        expenseValidationService.validateBusinessExists(expense, errors);

        verify(businessService).fetchBusiness(anyString());

        assertEquals(1, errors.size());
    }

    @Test
    public void testValidateBusinessExistsWithException() {
        final List<ValidationError> errors = new ArrayList<>();

        final Expense expense = ExpenseHarness.getValidTelephoneExpense();

        when(businessService.fetchBusiness(anyString())).thenThrow(new RuntimeException("Hi!"));

        assertThrows(ServiceException.class, ()-> expenseValidationService.validateBusinessExists(expense, errors));
    }

    @Test
    public void testValidateValidateNameOrIdWithNeither() {
        final List<ValidationError> errors = new ArrayList<>();

        final Expense expense = ExpenseHarness.getValidTelephoneExpense();
        expense.getPayee().setName(null);
        expense.getPayee().setId(null);

        expenseValidationService.validateNameOrId(expense.getPayee(), errors);

        assertEquals(1, errors.size());
    }

    @Test
    public void testValidateValidateNameOrIdWithJustName() {
        final List<ValidationError> errors = new ArrayList<>();

        final Expense expense = ExpenseHarness.getValidTelephoneExpense();
        expense.getPayee().setId(null);

        expenseValidationService.validateNameOrId(expense.getPayee(), errors);

        assertEquals(0, errors.size());
    }

    @Test
    public void testValidateValidateNameOrIdWithJustId() {
        final List<ValidationError> errors = new ArrayList<>();

        final Expense expense = ExpenseHarness.getValidTelephoneExpense();
        expense.getPayee().setName(null);

        expenseValidationService.validateNameOrId(expense.getPayee(), errors);

        assertEquals(0, errors.size());
    }
}
