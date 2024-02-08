package com.yomimashou.analyzer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    private String surface;
    private String baseForm;
    private String reading;
    private String partOfSpeechLevel1;
    private String partOfSpeechLevel2;

    @JsonIgnore
    private boolean merged;
}
