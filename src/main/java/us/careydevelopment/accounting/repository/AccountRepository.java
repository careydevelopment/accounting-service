package us.careydevelopment.accounting.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import us.careydevelopment.accounting.model.Account;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

}
