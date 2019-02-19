package com.epam.addressbook.leasing;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class LeasingApplicationTest {

    @Test
    public void getLeasingsByPersonId_returnsEmptyArray() {
        LeasingApplication.main(new String[]{});

        String response = new RestTemplate().getForObject("http://localhost:8181/leasings?personId=0", String.class);

        assertThat(response).isEqualTo("[]");
    }
}