package us.careydevelopment.accounting.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import us.careydevelopment.accounting.model.Settings;

@Repository
public interface SettingsRepository extends MongoRepository<Settings, String> {

    Settings findByOwnerUsername(String ownerUsername);

}
