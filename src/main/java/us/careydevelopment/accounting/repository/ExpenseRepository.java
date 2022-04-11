package us.careydevelopment.accounting.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import us.careydevelopment.accounting.model.Expense;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {

}
