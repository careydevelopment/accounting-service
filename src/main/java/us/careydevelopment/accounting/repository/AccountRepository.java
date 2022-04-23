package us.careydevelopment.accounting.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.AccountType;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    Account findByNameAndOwnerUsername(String name, String ownerUsername);

    Account findByAccountTypeAndOwnerUsername(AccountType accountType, String ownerUsername);
}
