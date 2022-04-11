package us.careydevelopment.accounting.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages= {"us.careydevelopment.accounting.repository"})
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${mongo.db.name}") 
    private String ecosystemDb;
    
    @Value("${mongodb.carey-ecosystem.connection}")
    private String connectionString;

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Override
    protected String getDatabaseName() {
        return ecosystemDb;
    }
    
    @Override
    @Bean
    public MongoClient mongoClient() {
        String fullConnectionString = connectionString + "/" + ecosystemDb;
        
        MongoClient client = MongoClients.create(fullConnectionString);
        return client;
    }

}
