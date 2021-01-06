package com.yomimashou.creator.scraper;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;

@Component
public abstract class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);

    @Value("${api.creator.username}")
    public String username;

    @Value("${api.creator.password}")
    public String password;

    // TODO consider adding Aozora Bunko scraper, as well as others

    @Autowired
    public RestTemplate restTemplate;

    public void createContent() {
        try {
            for (ScrapedText scrapedText : getContent()) {
                addTextToApplication(scrapedText);
            }
        } catch (IOException e) {
            LOGGER.error("could not create content", e);
        }
    }

    protected abstract List<ScrapedText> getContent() throws IOException;

    private void addTextToApplication(ScrapedText scrapedText) throws MalformedURLException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBasicAuth(username, password);

        JSONObject textJsonObject = new JSONObject();
        textJsonObject.put("title", scrapedText.getTitle());
        textJsonObject.put("content", scrapedText.getContent());
        textJsonObject.put("originalSource", scrapedText.getOriginalURL());
        textJsonObject.put("tags", Collections.singletonList(scrapedText.getOriginTag()));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("text", textJsonObject.toMap());
        if (scrapedText.getImgUrl() != null) {
            UrlResource resource = new UrlResource(scrapedText.getImgUrl());
            body.add("file", resource);
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.postForEntity("http://localhost:8080/api/text", requestEntity, Object.class);
    }
}
