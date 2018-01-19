package no.nav.pam.feed.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedTransport<T> {
    public Boolean last;
    public Integer totalPages;
    public Integer totalElements;
    public Integer size;
    public Integer number;
    public Boolean first;
    public Integer numberOfElements;
    public List<T> content;
}