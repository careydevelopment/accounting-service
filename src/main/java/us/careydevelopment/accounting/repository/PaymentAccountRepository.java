package us.careydevelopment.accounting.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import us.careydevelopment.accounting.model.PaymentAccount;

@Repository
public interface PaymentAccountRepository extends MongoRepository<PaymentAccount, String> {

}
