package com.epam.addressbook.history;

import org.springframework.web.client.RestTemplate;

public class HousingClient {

    private final RestTemplate restTemplate;
    private final String endpoint;

    public HousingClient(RestTemplate restTemplate, String registrationServerEndpoint) {
        this.restTemplate = restTemplate;
        this.endpoint = registrationServerEndpoint;
    }

    public HousingInfo getHousing(long housingId) {
        return restTemplate.getForObject(endpoint + "/housings/" + housingId, HousingInfo.class);
    }
}