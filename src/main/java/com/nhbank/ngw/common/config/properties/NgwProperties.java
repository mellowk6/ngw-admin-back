package com.nhbank.ngw.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "ngw")
public class NgwProperties {
    // getters/setters
    private String baseUrl;
    private String serviceKey;  //클라이언트 식별용 키
    private Timeout timeout = new Timeout();

    @Setter
    @Getter
    public static class Timeout {
        // getters/setters
        private int connectMillis;
        private int readMillis;

    }

}