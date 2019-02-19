package com.epam.addressbook.accommodation;

import org.springframework.web.client.RestTemplate;

public class HousingClient {

    private final RestTemplate restTemplate;
    private final String registrationServerEndpoint;

    public HousingClient(RestTemplate restTemplate, String registrationServerEndpoint) {
        this.restTemplate = restTemplate;
        this.registrationServerEndpoint = registrationServerEndpoint;
    }

    public HousingInfo getHousing(long housingId) {
        return restTemplate.getForObject(registrationServerEndpoint + "/housings/" + housingId, HousingInfo.class);
    }
}