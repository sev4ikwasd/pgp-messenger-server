package com.sev4ikwasd.pgpmessengerserver.config.settings;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "web")
@Getter
@Setter
public class WebSettings {
    private String authenticationHeaderName;

    private String authenticationUrl;

    private String refreshTokenUrl;

    private String registerUrl;

    private String apiRootUrl;
}
