// package management.subscription.repository;

// import java.util.List;
// import java.util.Map;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.data.mongodb.core.query.Criteria;
// import org.springframework.data.mongodb.core.query.Query;
// import org.springframework.stereotype.Repository;

// import management.subscription.entity.Subscription;

// @Repository
// public class SubscriptionRepositoryImpl implements SubscriptionRepositoryCustom {

//     private final MongoTemplate mongoTemplate;

//     public SubscriptionRepositoryImpl(MongoTemplate mongoTemplate) {
//         this.mongoTemplate = mongoTemplate;
//     }

//     @Override
//     public Page<Subscription> findByFieldsPaginated(Map<String, Object> filterFields, Pageable pageable) {
//         Query query = buildQuery(filterFields);

//         long count = mongoTemplate.count(query, Subscription.class);
//         List<Subscription> subscriptions = mongoTemplate.find(query.with(pageable), Subscription.class);

//         return new PageImpl<>(subscriptions, pageable, count);
//     }

//     private Query buildQuery(Map<String, Object> filterFields) {
//         Query query = new Query();
//         Criteria criteria = new Criteria();

//         for (Map.Entry<String, Object> entry : filterFields.entrySet()) {
//             criteria.and(entry.getKey()).is(entry.getValue());
//         }

//         query.addCriteria(criteria);

//         return query;
//     }
// }
