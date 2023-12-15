package management.subscription.services;   

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// // import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Service;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import management.subscription.entity.Subscription;
// import management.subscription.generic.CommonResponse;
// import management.subscription.repository.SubscriptionRepository;
// @Service
// public class SubscriptionService {
//     @Autowired
//     private SubscriptionRepository subscriptionRepository;
//     @Autowired
//     private SequenceGenerator sequenceGenerator;
//     public CommonResponse< List<Subscription>> createSubscriptions(List<Subscription> subscriptions) {
//         for (Subscription subscription : subscriptions) {
//             subscription.setId(sequenceGenerator.getSequenceNumber(Subscription.Sequence));
//         }
//         List<Subscription> savedSubscriptions = subscriptionRepository.saveAll(subscriptions);
//         int numberOfSubscriptions = savedSubscriptions.size();
//         return new CommonResponse<>(savedSubscriptions, numberOfSubscriptions + "Subscriptions saved successfully.");
//         // List<String> successMessages = new ArrayList<>();
//         // for (Subscription subscription : subscriptions) {
//         //     successMessages.add("The Subscription has been created successfully with ID " + subscription.getId());
//         // }
//         // return successMessages;
//     }
//     public ResponseEntity<Object> getSubscriptions(Map<String, Object> filterFields) {
//         try {
//             Object pageObject = filterFields.remove("page");
//             Object sizeObject = filterFields.remove("size"); 
//             Object sortFieldObject = filterFields.remove("sortField");
//             Object sortInObject = filterFields.remove("sortIn");
//             Object pagenullObject = filterFields.remove("pagenull");
//             int page = (pageObject != null) ? Integer.parseInt( pageObject.toString()) : 0;
//             int size = (sizeObject != null) ? Integer.parseInt(sizeObject.toString()) : 5;
//             String sortField = (sortFieldObject != null) ? sortFieldObject.toString() : "id";
//             String sortIn = (sortInObject != null) ? (String) sortInObject : "asc";
//             boolean pagenull = (pagenullObject != null) ? Boolean.parseBoolean(pagenullObject.toString()) : false;
//             Sort.Direction direction = sortIn.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
//             Sort sort = Sort.by(direction, sortField);
//             Pageable pageable = PageRequest.of(page, size, sort);
//             if (!filterFields.isEmpty()) {
//                 Page<Subscription> subscriptions = subscriptionRepository.findByFieldsPaginated(filterFields, pageable);
//                 if (!subscriptions.isEmpty()) {
//                     return ResponseEntity.ok(new CommonResponse<>(subscriptions, "Subscriptions retrieved successfully."));
//                 } else {
//                     return ResponseEntity.notFound().build();
//                 }
//             } else {
//                 if (pagenull) {
//                     List<Subscription> totalSubscriptions = subscriptionRepository.findAll(sort);
//                     return ResponseEntity.ok(new CommonResponse<>(totalSubscriptions, "All subscriptions retrieved."));
//                 } else {
//                     Page<Subscription> paginatedSubscriptions = subscriptionRepository.findAll(pageable);
//                     return ResponseEntity.ok(new CommonResponse<>(paginatedSubscriptions, "Paginated subscriptions retrieved."));
//                 }
//             }
//         } catch (Exception e) {
//             return ResponseEntity.ok(new CommonResponse<>(null, "Error retrieving subscriptions: " + e.getMessage()));
//         }
//     }
//     public ResponseEntity<CommonResponse<Object>> updateItems(String id, List<Subscription> updatedSubscriptions) {
//         try {
//             if (id != null) {
//                 Optional<Subscription> existingSubscription = subscriptionRepository.findById(Long.valueOf(id));
//                 if (existingSubscription.isPresent()) {
//                     Subscription updatedSubscription = updatedSubscriptions.get(0);
//                     updatedSubscription.setId(Long.valueOf(id)); // Set the ID
//                     subscriptionRepository.save(updatedSubscription);
//                     return ResponseEntity.ok(new CommonResponse<>(updatedSubscription, "Subscription updated successfully."));
//                 } else {
//                     return ResponseEntity.notFound().build();
//                 }
//             } else {
//                 for (Subscription subscription : updatedSubscriptions) {
//                     subscriptionRepository.save(subscription);
//                 }
//                 return ResponseEntity.ok(new CommonResponse<>(updatedSubscriptions, "Subscriptions updated successfully."));
//             }
//         } catch (Exception e) {
//             return ResponseEntity.ok(new CommonResponse<>(null, "Error updating subscriptions: " + e.getMessage()));
//         }
//     }
//     public ResponseEntity<CommonResponse<List<String>>> deleteItems(List<Subscription> subscriptions) {
//         try {
//             subscriptionRepository.deleteAll(subscriptions);
//             List<String> successMessages = new ArrayList<>();
//             for (Subscription subscription : subscriptions) {
//                 successMessages.add("The Subscription with ID " + subscription.getId() + " has been deleted successfully.");
//             }
//             return ResponseEntity.ok(new CommonResponse<>(successMessages, "Subscriptions deleted successfully."));
//         } catch (Exception e) {
//             return ResponseEntity.ok(new CommonResponse<>(null, "Error deleting subscriptions: " + e.getMessage()));
//         }
//     }
// }
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import management.subscription.entity.Subscription;
import management.subscription.generic.CommonResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubscriptionService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SequenceGenerator sequenceGenerator;

    public CommonResponse<List<Subscription>> createSubscriptions(List<Subscription> subscriptions) {
        long startTime = System.currentTimeMillis();
        try {
            for (Subscription subscription : subscriptions) {
                subscription.setId(sequenceGenerator.getSequenceNumber(Subscription.Sequence));
            }
            List<Subscription> savedSubscriptions = new ArrayList<>(mongoTemplate.insertAll(subscriptions));

            long endTime = System.currentTimeMillis();
            log.info("DB query executed in {} ms", endTime - startTime);

            return new CommonResponse<>(savedSubscriptions, savedSubscriptions.size() + " Subscriptions saved successfully.");
        } catch (Exception e) {
            return new CommonResponse<>(null, "Error creating subscriptions: " + e.getMessage());
        }
    }

    public ResponseEntity<CommonResponse<Object>> getSubscriptions(Map<String, Object> filterFields) {
        long startTime = System.currentTimeMillis();
        try {
            Object pageObject = filterFields.remove("page");
            Object sizeObject = filterFields.remove("size");
            Object sortFieldObject = filterFields.remove("sortField");
            Object sortInObject = filterFields.remove("sortIn");
            Object pagenullObject = filterFields.remove("pagenull");
            int page = (pageObject != null) ? Integer.parseInt(pageObject.toString()) : 0;
            int size = (sizeObject != null) ? Integer.parseInt(sizeObject.toString()) : 5;
            String sortField = (sortFieldObject != null) ? sortFieldObject.toString() : "id";
            String sortIn = (sortInObject != null) ? (String) sortInObject : "asc";
            boolean pagenull = (pagenullObject != null) ? Boolean.parseBoolean(pagenullObject.toString()) : false;
            Sort.Direction direction = sortIn.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, sortField);
            Pageable pageable = PageRequest.of(page, size, sort);

            Query query = new Query();

            // Adding criteria for each field in the filterFields map
            for (Map.Entry<String, Object> entry : filterFields.entrySet()) {
                query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
            }

            // Applying pagination settings if pagenull is false
            if (!pagenull) {
                query.with(pageable);
            }

            long count = mongoTemplate.count(query, Subscription.class);
            List<Subscription> subscriptions = mongoTemplate.find(query, Subscription.class);

            if (!subscriptions.isEmpty()) {
                if (!pagenull) {

                    long endTime = System.currentTimeMillis();
                    log.info("DB query executed in {} ms", endTime - startTime);
                    // with pagination settings
                    return ResponseEntity.ok(new CommonResponse<>(new PageImpl<>(subscriptions, pageable, count), "Subscriptions retrieved successfully."));
                } else {
                    // If pagenull is true, return all results without pagination
                    long endTime = System.currentTimeMillis();
                    log.info("DB query executed in {} ms", endTime - startTime);

                    return ResponseEntity.ok(new CommonResponse<>(subscriptions, "Subscriptions retrieved successfully."));
                }
            } else {
                throw new SubscriptionNotFoundException("No subscriptions found with the given criteria.");
            }

        } catch (Exception e) {
            throw new SubscriptionNotFoundException(e.getMessage());
        }
    }

    public ResponseEntity<CommonResponse<Object>> updateItems(List<Subscription> updatedSubscriptions) {
        long startTime = System.currentTimeMillis();
        try {
            List<Long> notFoundIds = new ArrayList<>();

            for (Subscription updatedSubscription : updatedSubscriptions) {
                long subscriptionId = updatedSubscription.getId();

                // Check if the subscription with the given ID exists in the database
                Subscription existingSubscription = mongoTemplate.findById(subscriptionId, Subscription.class);

                if (existingSubscription != null) {
                    // Update the existingSubscription with fields from updatedSubscription
                    updateSubscriptionFields(existingSubscription, updatedSubscription);

                    // Save the updated subscription back to the database
                    mongoTemplate.save(existingSubscription);
                } else {
                    // If the ID is not found, add it to the notFoundIds list
                    notFoundIds.add(subscriptionId);
                }
            }

            if (!notFoundIds.isEmpty()) {
                String errorMessage = "Subscriptions with IDs not found in the database: " +
                        notFoundIds.stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(", "));
                throw new SubscriptionNotFoundException(errorMessage);
            }
            long endTime = System.currentTimeMillis();
            log.info("DB query executed in {} ms", endTime - startTime);

            return ResponseEntity.ok(new CommonResponse<>(updatedSubscriptions, "Subscriptions updated successfully."));
        } catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ok(new CommonResponse<>(null, "Error updating subscriptions: " + e.getMessage()));
        }
    }

    private void updateSubscriptionFields(Subscription existingSubscription, Subscription updatedSubscription) {
        Class<?> subscriptionClass = Subscription.class;
        Field[] fields = subscriptionClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                // Get the value of the field from updatedSubscription
                Object updatedValue = field.get(updatedSubscription);

                // If the value is not null, update the corresponding field in existingSubscription
                if (updatedValue != null) {
                    field.set(existingSubscription, updatedValue);
                }
            } catch (IllegalAccessException e) {
                // Handle the exception as needed
                e.printStackTrace();
            }
        }
    }

    public ResponseEntity<CommonResponse<List<Subscription>>> deleteItems(List<Long> subscriptionIds) {
        long startTime = System.currentTimeMillis();
        try {
            List<Subscription> deletedSubscriptions = new ArrayList<>();
            List<Long> notFoundIds = new ArrayList<>();

            for (Long subscriptionId : subscriptionIds) {
                Query query = new Query(Criteria.where("_id").is(subscriptionId));
                Subscription deletedSubscription = mongoTemplate.findAndRemove(query, Subscription.class);

                if (deletedSubscription != null) {
                    deletedSubscriptions.add(deletedSubscription);
                } else {
                    notFoundIds.add(subscriptionId);
                }
            }

            if (!notFoundIds.isEmpty()) {
                String errorMessage = "Subscriptions with IDs not found in the database: " +
                        notFoundIds.stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(", "));
                throw new SubscriptionNotFoundException(errorMessage);
            }
            long endTime = System.currentTimeMillis();
            log.info("DB query executed in {} ms", endTime - startTime);

            return ResponseEntity.ok(new CommonResponse<>(deletedSubscriptions));

        } catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse<>(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ok(new CommonResponse<>(null, "Error deleting subscriptions: " + e.getMessage()));
        }
    }
}
