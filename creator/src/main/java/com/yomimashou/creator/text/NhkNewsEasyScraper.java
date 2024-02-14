package com.yomimashou.creator.text;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NhkNewsEasyScraper extends Scraper {

    private static final String NHK_EASY_RECENT_ARTICLES_URL = "https://www3.nhk.or.jp/news/easy/news-list.json";
    private static final String NHK_EASY_ARTICLE_URL = "https://www3.nhk.or.jp/news/easy/%s/%s.html";
    private static final String ORIGIN_TAG_NHK_EASY = "NHK Easy";

    // TODO add scheduler
    public List<ScrapedText> getContent() {
        return getRecentNewsIds().stream().map(newsId -> {
                    try {
                        return getScrapedText(buildArticleUrl(newsId));
                    } catch (IOException error) {
                        log.error("Unable to scrape news with id {}", newsId, error);
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<String> getRecentNewsIds() {
        try {
            String jsonString = Jsoup.connect(NHK_EASY_RECENT_ARTICLES_URL).ignoreContentType(true).get().text();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, List<Map>> recentArticlesByDate = (Map<String, List<Map>>) mapper.readValue(jsonString, Object[].class)[0];
            return recentArticlesByDate.values().stream()
                    .flatMap(List::stream)
                    .map(article -> (String) article.get("news_id"))
                    .collect(Collectors.toList());
        } catch (IOException error) {
            log.error("Unable to fetch recent news ids", error);
            return Collections.emptyList();
        }
    }

    private ScrapedText getScrapedText(String url) throws IOException {
        Document document = Jsoup.connect(url).get();

        String title = document.title().split("\\|")[1];

        Elements body = document.select("div#js-article-body");

        Optional<Element> image = body.first().children().stream()
                .filter(element -> "img".equals(element.tag().getName()))
                .findFirst();

        String content = body.first().children().stream()
                .filter(element -> "p".equals(element.tag().getName()))
                .map(element -> {
                    element.select("rt").remove();
                    return element.text();
                }).collect(Collectors.joining("\n"));

        ScrapedText scrapedText = new ScrapedText();
        scrapedText.setTitle(title);
        scrapedText.setContent(content);
        scrapedText.setOriginalURL(url);
        scrapedText.setOriginTag(ORIGIN_TAG_NHK_EASY);
        image.ifPresent(element -> scrapedText.setImgUrl(element.attr("src")));

        return scrapedText;
    }

    private String buildArticleUrl(String newsId) {
        return String.format(NHK_EASY_ARTICLE_URL, newsId, newsId);
    }
}
