package com.yomimashou.it;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.JsonPath;
import com.yomimashou.appscommon.model.Text;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("it")
@EnableAutoConfiguration
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-it.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TextIT {

    @Autowired
    private MockMvc mvc;

    @Test
    public void addTokenizesText() throws Exception {
        // Arrange
        Text text = Text.builder().title("test").content("むかしむかし、貧しいけれど、心の優しいおじいさんとおばあさんがいました。").build();
        String json = getJsonString(text);

        Map<String, String> smth = new LinkedHashMap<>();
        smth.put("むかし", "むかし");
        smth.put("貧しいけれど", "貧しい");
        smth.put("心", "心");
        smth.put("の", "の");
        smth.put("優しい", "優しい");
        smth.put("おじいさん", "おじいさん");
        smth.put("と", "と");
        smth.put("おばあさん", "おばあさん");
        smth.put("が", "が");
        smth.put("いました", "いる");

        // Act
        mvc.perform(MockMvcRequestBuilders.multipart("/api/text")
                        .file(new MockMultipartFile("text", "", "application/json", json.getBytes())).characterEncoding("UTF-8"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parsedKanji").value("貧|心|優"))
                .andExpect(jsonPath("$.parsedWords").value(equalTo(asParsedJson(smth))));
    }

    private String getJsonString(Text text) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(text);
    }

    private <T> T asParsedJson(Object obj) throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(obj);
        return JsonPath.read(json, "$");
    }
}
