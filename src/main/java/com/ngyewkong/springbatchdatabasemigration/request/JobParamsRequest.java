package com.ngyewkong.springbatchdatabasemigration.request;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class JobParamsRequest {
    // set up the parameter key:value pair
    private String paramKey;

    private String paramValue;
}
