package com.yomimashou.creator.text;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScrapedText {
    private String title;
    private String content;
    private String imgUrl;
    private String originalURL;
    private String originTag;
}
