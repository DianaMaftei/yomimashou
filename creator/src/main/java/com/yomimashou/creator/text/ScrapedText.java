package com.yomimashou.creator.text;

public class ScrapedText {
    private String title;
    private String content;
    private String imgUrl;
    private String originalURL;
    private String originTag;

    public ScrapedText() {
    }

    public ScrapedText(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public String getOriginTag() {
        return originTag;
    }

    public void setOriginTag(String originTag) {
        this.originTag = originTag;
    }

}
