package com.epam.addressbook.history;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class HistoryApplicationTest {

    @Test
    public void getHistoriesByHousingId_returnsEmptyArray() {
        HistoryApplication.main(new String[]{});

        String response = new RestTemplate().getForObject("http://localhost:8181/histories?housingId=0", String.class);

        assertThat(response).isEqualTo("[]");
    }
}