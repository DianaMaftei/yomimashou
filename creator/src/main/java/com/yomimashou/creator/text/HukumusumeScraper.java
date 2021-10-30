package com.yomimashou.creator.text;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
    private static final String HUKUMUSUME_URL_PREFIX = "http://hukumusume.com/douwa/pc/";
    private static final String HUKUMUSUME_URL_SUFFIX = ".htm";
    public static final String ORIGIN_TAG_HUKUMUSUME = "Hukumusume";

    public List<ScrapedText> getContent() throws IOException {
        // as a starter, get jap stories for the month of august
        // in the future, get all texts by story type and month and day : http://hukumusume.com/douwa/pc/jap/01/01.htm
        return IntStream.rangeClosed(0, 31).boxed().map(day -> {
            try {
                return getScrapedText(HUKUMUSUME_URL_PREFIX + StoryType.JAP.getValue() + "/08/" + addLeadingZero(day) + HUKUMUSUME_URL_SUFFIX);
            } catch (IOException e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public ScrapedText getScrapedText(String url) throws IOException {
        ScrapedText scrapedText = new ScrapedText();

        Document document = Jsoup.connect(url).get();

        String title = document.title().split("＜")[0].replace("　", "");

        // SET TITLE
        scrapedText.setTitle(title);

        Elements elements = document.select("p");

        // SET IMAGE
        Optional<Element> image = elements.stream()
                .filter(element -> "center".equals(element.attributes().get("align")))
                .map(Element::children)
                .flatMap(Collection::stream)
                .filter(element -> "img".equals(element.tag().getName()) && element.attributes().get("src").contains("gazou"))
                .findFirst();
        if (image.isPresent()) {
            String imageSrc = image.get().attributes().get("src").replace("../../../", "http://hukumusume.com/douwa/");
            scrapedText.setImgUrl(imageSrc);
        }

        // SET CONTENT
        String text = elements.stream()
                .filter(element -> "".equals(element.attributes().get("align")) && element.text().startsWith("　"))
                .map(Element::text).collect(Collectors.joining("\n"))
                .replace(" 　", "\n").replace("　", "");
        scrapedText.setContent(text);

        // SET ORIGINAL SOURCE URL AND TAG
        scrapedText.setOriginalURL(url);
        scrapedText.setOriginTag(ORIGIN_TAG_HUKUMUSUME);

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
}
