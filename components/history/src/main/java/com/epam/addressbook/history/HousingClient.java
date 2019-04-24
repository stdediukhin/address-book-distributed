package com.epam.addressbook.history;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class HousingClient {

    private final ConcurrentMap<Long, HousingInfo> cache = new ConcurrentHashMap<>();
    private final RestTemplate restTemplate;
    private final String endpoint;

    public HousingClient(RestTemplate restTemplate, String registrationServerEndpoint) {
        this.restTemplate = restTemplate;
        this.endpoint = registrationServerEndpoint;
    }

    @HystrixCommand(fallbackMethod = "getHousingFromCache")
    public HousingInfo getHousing(long housingId) {
        final HousingInfo housing = restTemplate.getForObject(endpoint + "/housings/" + housingId, HousingInfo.class);

        cache.put(housingId, housing);

        return housing;
    }

    public HousingInfo getHousingFromCache(long housingId) {
        log.info("Getting project with id {} from cache", housingId);

        return cache.getOrDefault(housingId, null);
    }
}