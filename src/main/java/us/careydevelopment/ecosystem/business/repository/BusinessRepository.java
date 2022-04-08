package us.careydevelopment.ecosystem.business.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import us.careydevelopment.ecosystem.business.model.Business;
import us.careydevelopment.ecosystem.business.model.Industry;

import java.util.List;

public interface BusinessRepository extends MongoRepository<Business, String> {

    Business findByName(String name);

}
