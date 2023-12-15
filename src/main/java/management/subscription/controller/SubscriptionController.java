package management.subscription.controller;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import management.subscription.entity.Subscription;
import management.subscription.generic.CommonRequest;
import management.subscription.generic.CommonResponse;
import management.subscription.services.SubscriptionService;

@RestController
@RequestMapping("/api/subscriptions")
@Slf4j
@Validated
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;
    
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CommonResponse<List<Subscription>>> createSubscriptions(@Valid @RequestBody  CommonRequest<Subscription> commonRequest) {
        long startTime = System.currentTimeMillis();
        try {
            CommonResponse< List<Subscription>>  successMessages = subscriptionService.createSubscriptions(commonRequest.getData());

            long endTime = System.currentTimeMillis();
            log.info("API response time: {} ms", endTime - startTime);

            return ResponseEntity.ok(successMessages);
        } catch (Exception e) {
            log.error("Error creating subscriptions", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse<>(null, "Error creating subscriptions: " + e.getMessage()));
        }
    }


    @GetMapping
    public ResponseEntity<CommonResponse<Object>> getSubscriptions(@RequestBody @RequestParam Map<String, Object> filterFields) {
        long startTime = System.currentTimeMillis();
        try {
           ResponseEntity<CommonResponse<Object>>  response = subscriptionService.getSubscriptions(filterFields);
          
            long endTime = System.currentTimeMillis();
            log.info("API response time: {} ms", endTime - startTime);

            return response;

        } catch (Exception e) {
            log.error("Error getting subscriptions", e);
            // Customize the response or handle the exception as needed
            return ResponseEntity.badRequest().body(new CommonResponse<>(null, "Error getting subscriptions: " + e.getMessage()));
        }
    }

    @PatchMapping
    public ResponseEntity<CommonResponse<Object>> updateItems(@RequestBody  CommonRequest<Subscription> commonRequest) {
        long startTime = System.currentTimeMillis();
        try {
           ResponseEntity<CommonResponse<Object>> response = subscriptionService.updateItems(commonRequest.getData());

           long endTime = System.currentTimeMillis();
           log.info("API response time: {} ms", endTime - startTime);

           return response;

        } catch (Exception e) {
            log.error("Error updating subscriptions", e);
            // Customize the response or handle the exception as needed
            return ResponseEntity.badRequest().body(new CommonResponse<>(null, "Error updating subscriptions: " + e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse<List<Subscription>>> deleteItems(@RequestBody CommonRequest<Long> commonRequest) {
        long startTime = System.currentTimeMillis();
        try {
          ResponseEntity<CommonResponse<List<Subscription>>>   response = subscriptionService.deleteItems(commonRequest.getData());
          
          long endTime = System.currentTimeMillis();
          log.info("API response time: {} ms", endTime - startTime);

          return response;

        } catch (Exception e) {
            log.error("Error deleting subscriptions", e);
            // Customize the response or handle the exception as needed
            return ResponseEntity.badRequest().body(new CommonResponse<>(null, "Error deleting subscriptions: " + e.getMessage()));
        }
    }

}
