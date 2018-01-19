package no.nav.pam.feed.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;


@Ignore
public class FeedConnectorTest {

    @Test
    public void fetchFeedAdTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        FeedConnector feedConnector = new FeedConnector(objectMapper, restTemplate);
        //TODO finish the test later
    }

}
