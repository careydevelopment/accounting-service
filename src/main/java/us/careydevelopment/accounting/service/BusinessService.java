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
import us.careydevelopment.accounting.model.Business;
import us.careydevelopment.accounting.util.SessionUtil;
import us.careydevelopment.util.api.model.RestResponse;


@Service
public class BusinessService {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessService.class);

    private static final int SECONDS_DELAY_BETWEEN_RETRIES = 3;
    private static final int MAX_RETRIES = 1;

    private WebClient businessClient;

    @Autowired
    private SessionUtil sessionUtil;

    public BusinessService(@Value("${ecosystem-business-service.endpoint}") String endpoint) {
        businessClient = WebClient
	        		.builder()
	        		.baseUrl(endpoint)
	        		.filter(WebClientFilter.logRequest())
	        		.filter(WebClientFilter.logResponse())
	        		//.filter(WebClientFilter.handleError())
	        		.build();
    }

    public Business fetchBusiness(String id) {
        final String bearerToken = sessionUtil.getBearerToken();
        final Business business = fetchBusiness(bearerToken, id);
        return business;
    }

    public Business fetchBusiness(String bearerToken, String id) {
        try {
            RestResponse<Business> response = businessClient.get()
                    .uri("/businesses/" + id)
                    .header(HttpHeaders.AUTHORIZATION, bearerToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<RestResponse<Business>>() {
                    })
                    .block();

            Business business = (response != null) ? response.getResponse() : null;

            return business;
        } catch (WebClientResponseException.NotFound nfe) {
            LOG.error("Business with ID " + id + " doesn't exist");
            throw new NotFoundException("Business with ID " + id + " doesn't exist");
        } catch (Exception e) {
            LOG.error("Problem trying to retrieve Business ID " + id);
            throw new ServiceException(e.getMessage());
        }
    }
}
