package com.neoderm.feign.config;

import feign.Logger;
import org.springframework.stereotype.Component;

/**
 * feign配置
 */
@Component
public class FeignConfig {

    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

}
