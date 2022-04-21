package us.careydevelopment.accounting.service;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import us.careydevelopment.accounting.exception.InvalidRequestException;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.SalesReceipt;
import us.careydevelopment.accounting.repository.SalesReceiptRepository;
import us.careydevelopment.accounting.util.DateUtil;

@Component
public class SalesService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(SalesService.class);

    @Autowired
    private SalesValidationService salesValidationService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SalesReceiptRepository salesReceiptRepository;

    @Transactional
    public SalesReceipt postSalesReceipt(final SalesReceipt salesReceipt, final BindingResult bindingResult) throws InvalidRequestException {
        sanitizeData(salesReceipt);

        salesValidationService.validateNew(salesReceipt, bindingResult);

        final SalesReceipt returnedReceipt = postSalesReceipt(salesReceipt);
        return returnedReceipt;
    }

    @VisibleForTesting
    void sanitizeData(final SalesReceipt salesReceipt) {
        if (salesReceipt.getDate() == null) {
            salesReceipt.setDate(DateUtil.currentTime());
        }

        if (salesReceipt.getSales() != null) {
            salesReceipt.getSales().forEach(sale -> {
                if (sale.getDate() == null) {
                    sale.setDate(salesReceipt.getDate());
                }

                if (sale.getAmount() == null) {
                    sale.setAmount(0l);
                }

                setOwner(sale);
            });
        }

        setOwner(salesReceipt);
    }

    private SalesReceipt postSalesReceipt(final SalesReceipt salesReceipt) {
        try {
            transactionService.transact(salesReceipt);
            final SalesReceipt returnedSalesReceipt = add(salesReceipt);

            return returnedSalesReceipt;
        } catch (Exception e) {
            LOG.error("Problem posting sales receipt!", e);
            throw new ServiceException(e.getMessage());
        }
    }

    private SalesReceipt add(final SalesReceipt salesReceipt) {
        final SalesReceipt returnedReceipt = salesReceiptRepository.save(salesReceipt);
        return returnedReceipt;
    }
}
