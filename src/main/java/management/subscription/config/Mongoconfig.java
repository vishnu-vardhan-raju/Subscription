package management.subscription.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableMongoAuditing

public class Mongoconfig {

    // @Bean
    // public AuditorAware<String> auditorProvider() {
    //     return new AuditorAwareImpl();
    // }

    // @Bean
    // public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDbFactory) {
    //     return new MongoTemplate(mongoDbFactory);
    // }

    // @Bean
    // public LocalValidatorFactoryBean validator() {
    //     return new LocalValidatorFactoryBean();
    // }
    // @Bean
    // public javax.validation.Validator localValidatorFactoryBean() {
    //     return new LocalValidatorFactoryBean();
    // }
}
