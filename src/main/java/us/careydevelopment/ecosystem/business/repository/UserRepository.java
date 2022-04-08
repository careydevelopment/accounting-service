package us.careydevelopment.ecosystem.business.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import us.careydevelopment.ecosystem.business.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    public User findByUsername(String username);

}
