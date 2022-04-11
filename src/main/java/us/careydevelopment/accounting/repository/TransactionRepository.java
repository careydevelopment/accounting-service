package us.careydevelopment.accounting.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import us.careydevelopment.accounting.model.Transaction;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    
}
