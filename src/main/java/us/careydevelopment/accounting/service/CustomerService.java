package us.careydevelopment.accounting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import us.careydevelopment.accounting.exception.NotFoundException;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.Customer;
import us.careydevelopment.accounting.util.SessionUtil;
import us.careydevelopment.util.api.model.RestResponse;


@Service
public class CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

    private static final int SECONDS_DELAY_BETWEEN_RETRIES = 3;
    private static final int MAX_RETRIES = 1;

    private WebClient customerClient;

    @Autowired
    private SessionUtil sessionUtil;

    public CustomerService(@Value("${ecosystem-customer-service.endpoint}") String endpoint) {
        customerClient = WebClient
	        		.builder()
	        		.baseUrl(endpoint)
	        		.filter(WebClientFilter.logRequest())
	        		.filter(WebClientFilter.logResponse())
	        		//.filter(WebClientFilter.handleError())
	        		.build();
    }

    public Customer fetchCustomer(final String id) {
        final String bearerToken = sessionUtil.getBearerToken();
        final Customer customer = fetchCustomer(bearerToken, id);
        return customer;
    }

    public Customer fetchCustomer(String bearerToken, String id) {
        try {
            RestResponse<Customer> response = customerClient.get()
                    .uri("/customers/" + id)
                    .header(HttpHeaders.AUTHORIZATION, bearerToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<RestResponse<Customer>>() {})
                    .block();

            final Customer customer = (response != null) ? response.getResponse() : null;

            return customer;
        } catch (WebClientResponseException.NotFound nfe) {
            LOG.error("Customer with ID " + id + " doesn't exist");
            throw new NotFoundException("Customer with ID " + id + " doesn't exist");
        } catch (Exception e) {
            LOG.error("Problem trying to retrieve Customer ID " + id);
            throw new ServiceException(e.getMessage());
        }
    }
}
