package com.epam.addressbook.accommodation;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class AccommodationApplicationTest {

    @Test
    public void getAccommodationByHousingId_returnsEmptyArray() {
        AccommodationApplication.main(new String[]{});

        String response = new RestTemplate().getForObject("http://localhost:8181/accommodations?housingId=0", String.class);

        assertThat(response).isEqualTo("[]");
    }
}