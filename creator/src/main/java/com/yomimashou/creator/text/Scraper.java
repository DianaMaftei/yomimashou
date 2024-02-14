package com.yomimashou.creator.text;

import com.yomimashou.creator.config.CreatorProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public abstract class Scraper {

    private CreatorProperties creatorProperties;
    private RestTemplate restTemplate;

    public void createContent() {
        try {
            for (ScrapedText scrapedText : getContent()) {
                addTextToApplication(scrapedText);
            }
        } catch (IOException e) {
            log.error("could not create content", e);
        }
    }

    protected abstract List<ScrapedText> getContent();

    private void addTextToApplication(ScrapedText scrapedText) throws MalformedURLException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBasicAuth(creatorProperties.getUsername(), creatorProperties.getPassword());

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
