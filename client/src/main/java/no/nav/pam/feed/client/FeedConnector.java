package no.nav.pam.feed.client;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedConnector {

    private static final Logger LOG = LoggerFactory.getLogger(FeedConnector.class);
    private static final int RESULT_LIMIT = 1000;
    private static final int PAGE_SIZE = 200;

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public FeedConnector(ObjectMapper mapper , RestTemplate resttemplate) {
        this.mapper = mapper;
        this.restTemplate = resttemplate;
    }

    public <T> List<T> fetchContentList(String url, long minutes,Class<T> type) throws IOException {
        List<T> items = new ArrayList<>();
        int pageNumber = 0;
        boolean lastPage = false;

        while (!lastPage && (pageNumber < RESULT_LIMIT/PAGE_SIZE)) {
            URI uri = buildURI(url, minutes, pageNumber);
            LOG.debug("fetching from uri {}", uri.toString());
            String json = restTemplate.getForObject(uri, String.class);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(FeedTransport.class, type);
            FeedTransport<T> feedPage = mapper.readValue(json, javaType);
            items.addAll(feedPage.content);
            LOG.debug("Fetched {} items page number {} from feed", feedPage.content.size(), pageNumber);
            pageNumber++;
            lastPage = feedPage.last;
        }

        return items;
    }

    private URI buildURI(String url, long minutes, int pageNumber) {
        return UriComponentsBuilder.fromUriString(url)
                .queryParam("minutes", minutes)
                .queryParam("page", pageNumber)
                .queryParam("size", PAGE_SIZE)
                .queryParam("sort", "updated,asc")
                .build()
                .toUri();
    }

}
