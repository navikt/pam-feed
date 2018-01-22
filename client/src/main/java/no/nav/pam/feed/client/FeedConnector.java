package no.nav.pam.feed.client;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    private final int resultlimit;
    private final int pagesize;

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public FeedConnector(ObjectMapper mapper , RestTemplate resttemplate,
                         @Value("${feed.resultlimit:100}") int resultlimit,
                         @Value("${feed.pagesize:20}") int pagesize) {
        this.mapper = mapper;
        this.restTemplate = resttemplate;
        this.resultlimit = resultlimit;
        this.pagesize = pagesize;
    }

    public <T> List<T> fetchContentList(String url, long millis,Class<T> type) throws IOException {
        List<T> items = new ArrayList<>();
        int pageNumber = 0;
        boolean lastPage = false;

        while (!lastPage && (pageNumber < resultlimit/pagesize)) {
            URI uri = buildURI(url, millis, pageNumber);
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

    private URI buildURI(String url, long millis, int pageNumber) {
        return UriComponentsBuilder.fromUriString(url)
                .queryParam("millis", millis)
                .queryParam("page", pageNumber)
                .queryParam("size", pagesize)
                .queryParam("sort", "updated,asc")
                .build()
                .toUri();
    }

}
