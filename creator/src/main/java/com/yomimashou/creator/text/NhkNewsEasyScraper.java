package com.yomimashou.creator.text;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class NhkNewsEasyScraper extends Scraper {
    private static final String NHK_EASY_RECENT_ARTICLES_URL = "https://www3.nhk.or.jp/news/easy/news-list.json";
    private static final String NHK_EASY_ARTICLE_URL = "https://www3.nhk.or.jp/news/easy/%s/%s.html";
    public static final String ORIGIN_TAG_NHK_EASY = "NHK Easy";

    public List<ScrapedText> getContent() throws IOException {
        return getRecentNewsIds().stream().map(id -> {
            try {
                return getScrapedText(id);
            } catch (IOException e) {
                return null;
            }
        }).collect(Collectors.toList());
    }

    private List<String> getRecentNewsIds() throws IOException {
        String jsonString = null;
        try {
            jsonString = Jsoup.connect(NHK_EASY_RECENT_ARTICLES_URL).ignoreContentType(true).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        List<Object> articlesJSON = mapper.readValue(jsonString.substring(1), new TypeReference<List<Object>>() {
        });

        Map<String, List> recentArticlesByDate = (Map<String, List>) articlesJSON.get(0);
        List<Map> newsArticles = recentArticlesByDate.values().stream().flatMap(List::stream).map(article -> (Map) article).collect(Collectors.toList());

        return newsArticles.stream().map(article -> ((String) article.get("news_id"))).collect(Collectors.toList());
    }

    private ScrapedText getScrapedText(String newsIs) throws IOException {
        ScrapedText scrapedText = new ScrapedText();

        String url = String.format(NHK_EASY_ARTICLE_URL, newsIs, newsIs);
        Document document = Jsoup.connect(url).get();

        // SET TITLE
        scrapedText.setTitle(document.title().split("\\|")[1]);

        Elements articleElements = document.select("div#js-article-body");

        // SET IMAGE
        Optional<Element> image = articleElements.first().children().stream()
                .filter(element -> element != null && "img".equals(element.tag().getName())).findFirst();
        image.ifPresent(element -> scrapedText.setImgUrl(element.attr("src")));

        List<String> articleParagraphs = articleElements.first().children().stream()
                .filter(element -> element != null && "p".equals(element.tag().getName()))
                .map(element -> {
                    element.select("rt").remove();
                    return element.text();
                }).collect(Collectors.toList());

        // SET CONTENT
        scrapedText.setContent(String.join("\n", articleParagraphs));

        // SET ORIGINAL SOURCE URL AND TAG
        scrapedText.setOriginalURL(url);
        scrapedText.setOriginTag(ORIGIN_TAG_NHK_EASY);

        return scrapedText;
    }
}
