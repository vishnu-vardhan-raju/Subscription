package management.subscription.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public  class AuditEntity  {


    @Field("createddate")
    @CreatedDate
    private Date createDate;

    @Field("updateddate")
    @LastModifiedDate
    private Date updateDate;

    @Field("createdby")
    @CreatedBy
    private String createdBy;

    @Field("updatedby")
    @LastModifiedBy
    private String updatedBy;
    
}
