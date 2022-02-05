package com.yomimashou.creator.text;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AozoraBunkoScraper extends Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AozoraBunkoScraper.class);

    @Value("${path.aozoraBunko}")
    private String aozoraBunkoCSVFilePath;

    private static final String AOZORA_BUNKO_AUTHOR_ROOT = "https://www.aozora.gr.jp/cards/%s/";
    private static final String AOZORA_BUNKO_BOOK_INFO_URL = AOZORA_BUNKO_AUTHOR_ROOT + "card%s.html";
    private static final String ORIGIN_TAG_AOZORA_BUNKO = "AozoraBunko";

    public List<ScrapedText> getContent() {
        List<ScrapedText> texts = new ArrayList<>();
        try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(aozoraBunkoCSVFilePath))) {
            bufferedReader.lines().collect(Collectors.toList()).stream().limit(10)
                    .forEach(line -> {
                        final String[] splitLine = line.split(",");
                        String authorId = splitLine[0];
                        String bookId = splitLine[2];
                        try {
                            texts.add(getScrapedText(getBookTextUrl(buildBookInfoUrl(authorId, bookId), authorId)));
                        } catch (IOException error) {
                            LOGGER.error("Unable to scrape book with id {} and author {}", bookId, authorId, error);
                        }
                    });
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return texts.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public ScrapedText getScrapedText(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        Element title = document.select("h1.title").stream().findFirst().get();
        Element author = document.select("h2.author").stream().findFirst().get();
        Element body = document.select("div.main_text").stream().findFirst().get();

        ScrapedText scrapedText = new ScrapedText();
        scrapedText.setTitle(title.text() + " - " + author.text());
        scrapedText.setContent(body.text().replace("。 ", "\n"));
        scrapedText.setOriginalURL(url);
        scrapedText.setOriginTag(ORIGIN_TAG_AOZORA_BUNKO);

        return scrapedText;
    }

    private String getBookTextUrl(String url, String authorId) throws IOException {
        Document bookInfoPage = Jsoup.connect(url).get();

        Optional<Element> nashi = bookInfoPage.select("td:contains(なし)").stream().findFirst();
        if (!nashi.isPresent()) {
            return null;
        }
        Optional<Element> documentLink = nashi.get().parent().select("a:contains(.html)").stream().findFirst();
        String rootUrl = String.format(AOZORA_BUNKO_AUTHOR_ROOT, addLeadingZeros(authorId));
        return documentLink.map(element -> element.attr("href").replace("./", rootUrl)).orElse(null);

    }

    private String addLeadingZeros(String id) {
        if (id.length() < 6) {
            return StringUtils.repeat("0", 6 - id.length()) + id;
        }
        return id;
    }

    private String buildBookInfoUrl(String authorId, String bookId) {
        return String.format(AOZORA_BUNKO_BOOK_INFO_URL, addLeadingZeros(authorId), bookId);
    }
}
