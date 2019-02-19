package com.epam.addressbook;

import com.epam.addressbook.support.ApplicationServer;
import com.epam.addressbook.support.HttpClient;
import com.epam.addressbook.support.MapBuilder;
import com.epam.addressbook.testsupport.TestScenarioSupport;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

public class FlowTest {

    private final HttpClient httpClient = new HttpClient();
    private final String workingDir = System.getProperty("user.dir");

    private ApplicationServer registrationServer = new ApplicationServer(workingDir + "/../applications/registration-server/build/libs/registration-server.jar", "8883");
    private ApplicationServer accommodationServer = new ApplicationServer(workingDir + "/../applications/accommodation-server/build/libs/accommodation-server.jar", "8881");
    private ApplicationServer historyServer = new ApplicationServer(workingDir + "/../applications/history-server/build/libs/history-server.jar", "8882");
    private ApplicationServer leasingServer = new ApplicationServer(workingDir + "/../applications/leasing-server/build/libs/leasing-server.jar", "8884");

    private String registrationServerUrl(String path) {
        return "http://localhost:8883" + path;
    }

    private String accommodationServerUrl(String path) {
        return "http://localhost:8881" + path;
    }

    private String historyServerUrl(String path) {
        return "http://localhost:8882" + path;
    }

    private String leasingServerUrl(String path) {
        return "http://localhost:8884" + path;
    }

    private long findResponseId(HttpClient.Response response) {
        try {
            return JsonPath.parse(response.body).read("$.id", Long.class);
        } catch (PathNotFoundException e) {
            try {
                return JsonPath.parse(response.body).read("$[0].id", Long.class);
            } catch (PathNotFoundException e1) {
                fail("Could not find id in response body. Response was: \n" + response);
                return -1;
            }
        }
    }

    @Before
    public void setup() throws Exception {
        registrationServer.startWithDatabaseName("address_book_registration_test");
        accommodationServer.startWithDatabaseName("address_book_accommodation_test");
        historyServer.startWithDatabaseName("address_book_history_test");
        leasingServer.startWithDatabaseName("address_book_leasing_test");
        ApplicationServer.waitOnPorts("8881", "8882", "8883", "8884");
        TestScenarioSupport.clearAllDatabases();
    }

    @After
    public void tearDown() {
        registrationServer.stop();
        accommodationServer.stop();
        historyServer.stop();
        leasingServer.stop();
    }

    @Test
    public void testBasicFlow() {
        HttpClient.Response response;

        response = httpClient.get(registrationServerUrl("/"));
        assertThat(response.body).isEqualTo("Hello!!!");

        response = httpClient.post(registrationServerUrl("/registration"), MapBuilder.jsonMapBuilder()
                .put("loginName", "login")
                .put("loginPassword", "password")
                .put("firstName", "Billy")
                .put("lastName", "Milligan")
                .put("email", "bm@example.com")
                .put("phone", "+12345678900")
                .build()
        );
        long createdPersonId = findResponseId(response);
        assertThat(createdPersonId).isGreaterThan(0);

        response = httpClient.get(registrationServerUrl("/persons/" + createdPersonId));
        assertThat(response.body).isNotNull().isNotEmpty();

        response = httpClient.get(registrationServerUrl("/accounts?personId=" + createdPersonId));
        long createdAccountId = findResponseId(response);
        assertThat(createdAccountId).isGreaterThan(0);

        response = httpClient.post(registrationServerUrl("/housings"), MapBuilder.jsonMapBuilder()
                .put("accountId", createdAccountId)
                .put("name", "House #1")
                .put("addressLine1", "Address Line 1")
                .put("addressLine2", "#2")
                .put("city", "City")
                .put("state", "ST")
                .put("zip5", "12345")
                .put("active", true)
                .build()
        );
        long createdHousingId = findResponseId(response);
        assertThat(createdHousingId).isGreaterThan(0);

        response = httpClient.get(registrationServerUrl("/housings?accountId=" + createdAccountId));
        assertThat(response.body).isNotNull().isNotEmpty();


        response = httpClient.get(accommodationServerUrl("/"));
        assertThat(response.body).isEqualTo("Hello!!!");

        response = httpClient.post(
                accommodationServerUrl("/accommodations"), MapBuilder.jsonMapBuilder()
                        .put("housingId", createdHousingId)
                        .put("personId", createdPersonId)
                        .put("singleOwned", true)
                        .put("startDate", "2018-01-01")
                        .put("endDate", "2015-10-01")
                        .build()
        );

        long createdAccommodationId = findResponseId(response);
        assertThat(createdAccommodationId).isGreaterThan(0);

        response = httpClient.get(accommodationServerUrl("/accommodations?housingId=" + createdHousingId));
        assertThat(response.body).isNotNull().isNotEmpty();


        response = httpClient.get(historyServerUrl("/"));
        assertThat(response.body).isEqualTo("Hello!!!");

        response = httpClient.post(historyServerUrl("/histories"), MapBuilder.jsonMapBuilder()
                .put("housingId", createdHousingId)
                .put("name", "History Name #1")
                .put("description", "History Description #1")
                .build()
        );
        long createdHistoryId = findResponseId(response);
        assertThat(createdHistoryId).isGreaterThan(0);

        response = httpClient.get(historyServerUrl("/histories?housingId" + createdHousingId));
        assertThat(response.body).isNotNull().isNotEmpty();


        response = httpClient.get(leasingServerUrl("/"));
        assertThat(response.body).isEqualTo("Hello!!!");

        response = httpClient.post(leasingServerUrl("/leasings"), MapBuilder.jsonMapBuilder()
                .put("housingId", createdHousingId)
                .put("personId", createdPersonId)
                .put("assignDate", "2018-01-01")
                .put("months", 10)
                .build()
        );
        long createdLeasingId = findResponseId(response);
        assertThat(createdLeasingId).isGreaterThan(0);

        response = httpClient.get(leasingServerUrl("/leasings?housingId" + createdHousingId));
        assertThat(response.body).isNotNull().isNotEmpty();
    }
}