package us.careydevelopment.accounting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.BusinessLightweight;
import us.careydevelopment.util.api.model.RestResponse;

import java.time.Duration;


@Service
public class BusinessService {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessService.class);

    private WebClient businessClient;

    public BusinessService(@Value("${ecosystem-business-service.endpoint}") String endpoint) {
        businessClient = WebClient
	        		.builder()
	        		.baseUrl(endpoint)
	        		.filter(WebClientFilter.logRequest())
	        		.filter(WebClientFilter.logResponse())
	        		.filter(WebClientFilter.handleError())
	        		.build();
    }

    public BusinessLightweight fetchBusiness(String bearerToken, String id) {
        RestResponse<BusinessLightweight> response = businessClient.get()
                                        .uri("/businesses/" + id)
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                                        .retrieve()
                                        .bodyToMono(new ParameterizedTypeReference<RestResponse<BusinessLightweight>>() {})
                                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                                                .filter(ex -> WebClientFilter.is5xxException(ex))
                                                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                                    new ServiceException("Max retry attempts reached")))
                                        .block();

        BusinessLightweight business = response.getResponse();

        return business;
    }
}
