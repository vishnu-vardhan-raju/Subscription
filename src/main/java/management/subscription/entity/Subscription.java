package management.subscription.entity;

import lombok.*;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import management.subscription.enums.Plan;
import management.subscription.enums.Product;
import management.subscription.enums.Recurrence;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sort_test")
public class Subscription extends AuditEntity   {

    @Transient
    public static final String Sequence = "sequence";

    @Id
    private Long id;

    @NotEmpty(message = "Customer Name should not be empty")
    private String customerName;

    @NotNull( message = "Please provide a valid invoice address")
    private String invoiceAddress;

    @NotNull( message = "Please provide a valid Delivery Address")
    private String deliveryAddress;

    @NotNull( message = "Please Select a valid Subscription Plan")
    private Plan subscriptionPlan;

    @NotNull( message = "Expiration can not be null")
    private Date expiration;

    @NotNull( message = "Please select a valid Subscription Type")
    private String subscriptionType;

    @NotNull
    private Recurrence recurrence;

    @NotNull
    private String currency;

    @NotNull
    private Product product;

}
