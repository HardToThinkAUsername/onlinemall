package com.onlinemall.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParamConfiguration {
    @Value("${local-save-address}")
    private String localSaveAddress;

    @Value("${return-base-url}")
    private String baseUrl;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setLocalSaveAddress(String localSaveAddress) {
        this.localSaveAddress = localSaveAddress;
    }

    public String getLocalSaveAddress() {
        return localSaveAddress;
    }
}
