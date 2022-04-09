package us.careydevelopment.accounting.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import us.careydevelopment.accounting.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    public User findByUsername(String username);

}
