package com.epam.addressbook.registration;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationApplicationTest {

    private static RestTemplate restTemplate = new RestTemplate();

    @BeforeClass
    public static void setUp() {
        RegistrationApplication.main(new String[]{});
    }

    @Test
    public void getAccountsByPersonId_returnsEmptyArray() {
        assertThat(restTemplate.getForObject("http://localhost:8181/accounts?personId=0", String.class)).isEqualTo("[]");
    }

    @Test
    public void getHousingsByAccountId_returnsEmptyArray() {
        assertThat(restTemplate.getForObject("http://localhost:8181/housings?accountId=0", String.class)).isEqualTo("[]");
    }

    @Test
    public void getHousingById_returnsNull() {
        assertThat(restTemplate.getForObject("http://localhost:8181/housings/0", String.class)).isEqualTo(null);
    }

    @Test
    public void getPersonById_returnsNull() {
        assertThat(restTemplate.getForObject("http://localhost:8181/persons/0", String.class)).isEqualTo(null);
    }
}