package com.yomimashou.creator.text;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class HukumusumeScraper extends Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HukumusumeScraper.class);

    private static final String HUKUMUSUME_ROOT = "http://hukumusume.com/douwa/";
    private static final String HUKUMUSUME_STORY_URL = HUKUMUSUME_ROOT + "pc/%s/%s/%s.htm"; // http://hukumusume.com/douwa/pc/jap/01/01.htm
    private static final String ORIGIN_TAG_HUKUMUSUME = "Hukumusume";

    public List<ScrapedText> getContent() {
        // as a starter, get jap stories for the month of march
        return IntStream.rangeClosed(1, 31).boxed().map(day -> {
                    try {
                        return getScrapedText(buildStoryUrl(StoryType.JAP, 3, day));
                    } catch (IOException error) {
                        LOGGER.error("Unable to scrape story type {} for month {} and day {}", StoryType.JAP, 3, day, error);
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public ScrapedText getScrapedText(String url) throws IOException {
        Document document = Jsoup.connect(url).get();

        String title = document.title().split("＜")[0].trim();

        Elements elements = document.select("p");

        Optional<Element> image = elements.stream()
                .filter(element -> "center".equals(element.attributes().get("align")))
                .map(Element::children)
                .flatMap(Collection::stream)
                .filter(element -> "img".equals(element.tag().getName()) && element.attributes().get("src").contains("gazou"))
                .findFirst();

        String text = elements.stream()
                .filter(element -> "".equals(element.attributes().get("align")) && element.text().startsWith("　"))
                .map(Element::text).collect(Collectors.joining("\n"))
                .replace(" 　", "\n").replace("　", "");

        ScrapedText scrapedText = new ScrapedText();
        scrapedText.setTitle(title);
        scrapedText.setContent(text);
        scrapedText.setOriginalURL(url);
        scrapedText.setOriginTag(ORIGIN_TAG_HUKUMUSUME);
        image.ifPresent(element -> scrapedText.setImgUrl(element.attr("src").replace("../../../", HUKUMUSUME_ROOT)));

        return scrapedText;
    }

    private String addLeadingZero(int number) {
        return number < 10 ? "0" + number : "" + number;
    }

    public enum StoryType {
        MONOGATARI("100monogatari"), // scary stories
        JAP("jap"), // japanese old stories
        WORLD("world"), // world tales
        MINWA("minwa"), // japanese folk tales
        AESOP("aesop"), // aesop fairy tales
        KOBANASHI("kobanashi"); // Edo stories

        StoryType(String value) {
            this.value = value;
        }

        private final String value;

        public String getValue() {
            return value;
        }
    }

    private String buildStoryUrl(StoryType storyType, int month, int day) {
        return String.format(HUKUMUSUME_STORY_URL, storyType.getValue(), addLeadingZero(month), addLeadingZero(day));
    }
}
