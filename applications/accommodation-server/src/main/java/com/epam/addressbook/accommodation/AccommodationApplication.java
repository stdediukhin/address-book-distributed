package com.epam.addressbook.accommodation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import java.util.TimeZone;

@SpringBootApplication
@ComponentScan({"com.epam.addressbook.accommodation", "com.epam.addressbook.restsupport"})
public class AccommodationApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(AccommodationApplication.class, args);
    }

    @Bean
    HousingClient housingClient(RestTemplate restTemplate, @Value("${registration.server.endpoint}") String registrationEndpoint) {
        return new HousingClient(restTemplate, registrationEndpoint);
    }
}