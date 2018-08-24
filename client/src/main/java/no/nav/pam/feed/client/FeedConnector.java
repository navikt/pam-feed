package no.nav.pam.feed.client;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedConnector {

    private static final Logger LOG = LoggerFactory.getLogger(FeedConnector.class);
    private final int pagesize;

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public FeedConnector(ObjectMapper mapper, RestTemplate resttemplate,
                         @Value("${feed.pagesize:20}") int pagesize) {
        this.mapper = mapper;
        this.restTemplate = resttemplate;
        this.pagesize = pagesize;
    }

    @Deprecated
    public <T> List<T> fetchContentList(String url, long millis, Class<T> type) throws IOException {
        URI uri = buildURI(url, millis);

        return fetchWithURI(uri, type);
    }

    /**
     * Will fetch feed with elements updated since the given date.
     *
     * This method should only be used when the feed producers support timestamp parameters
     * @param url Feed producer URL
     * @param updatedSince ISO DateTime stamp to
     * @param type element type
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T> List<T> fetchContentList(String url, LocalDateTime updatedSince, Class<T> type) throws IOException {
        URI uri = buildURI(url, updatedSince);

        return fetchWithURI(uri, type);
    }

    public <T> List<T> fetchContentList(String url, LocalDateTime updatedSince, Class<T> type, HttpHeaders headers) throws IOException {
        URI uri = buildURI(url, updatedSince);

        return fetchWithURIHeader(uri, type, headers);
    }

    private <T> List<T> fetchWithURIHeader(URI uri, Class<T> type, HttpHeaders headers) throws IOException {
        List<T> items = new ArrayList<>();
        LOG.info("fetching from uri {}", uri.toString());
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> exchange = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        String json = exchange.getBody();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(FeedTransport.class, type);
        FeedTransport<T> feedPage = mapper.readValue(json, javaType);
        items.addAll(feedPage.content);
        return items;
    }


    private <T> List<T> fetchWithURI(URI uri, Class<T> type) throws IOException {
        List<T> items = new ArrayList<>();

        LOG.info("fetching from uri {}", uri.toString());
        String json = restTemplate.getForObject(uri, String.class);
        JavaType javaType = mapper.getTypeFactory().constructParametricType(FeedTransport.class, type);
        FeedTransport<T> feedPage = mapper.readValue(json, javaType);
        items.addAll(feedPage.content);
        LOG.debug("Fetched {} items page number {} from feed", feedPage.content.size());
        return items;
    }

    @Deprecated
    private URI buildURI(String url, long millis) {
        return UriComponentsBuilder.fromUriString(url)
                .queryParam("millis", millis)
                .queryParam("size", pagesize)
                .queryParam("sort", "updated,asc")
                .build()
                .toUri();
    }

    private URI buildURI(String url, LocalDateTime updatedSince) {
        return UriComponentsBuilder.fromUriString(url)
                .queryParam("updatedSince", updatedSince)
                .queryParam("size", pagesize)
                .queryParam("sort", "updated,asc")
                .build()
                .toUri();
    }

}
