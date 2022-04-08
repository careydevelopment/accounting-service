package us.careydevelopment.ecosystem.business.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import us.careydevelopment.ecosystem.business.model.Industry;

import java.util.List;

public interface IndustryRepository extends MongoRepository<Industry, String> {

	Industry findByNaics();

	List<Industry> findByOrderByNameAsc();
}
