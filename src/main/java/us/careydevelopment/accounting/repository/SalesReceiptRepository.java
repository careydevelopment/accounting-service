package us.careydevelopment.accounting.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import us.careydevelopment.accounting.model.SalesReceipt;

@Repository
public interface SalesReceiptRepository extends MongoRepository<SalesReceipt, String> {

}
