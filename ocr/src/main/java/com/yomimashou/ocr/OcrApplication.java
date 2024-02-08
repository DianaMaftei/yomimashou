package com.yomimashou.ocr;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static net.sourceforge.tess4j.ITessAPI.TessPageSegMode.PSM_SINGLE_BLOCK_VERT_TEXT;

@SpringBootApplication
public class OcrApplication {

    @Value("${tessdata.path}")
    public String tessdataPath;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(OcrApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

    @Bean
    public Tesseract tesseract() {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessdataPath);
        tesseract.setLanguage("jpn");
        tesseract.setPageSegMode(PSM_SINGLE_BLOCK_VERT_TEXT);

        tesseract.setTessVariable("user_defined_dpi", "300");
        tesseract.setTessVariable("preserve_interword_spaces", "1");
        tesseract.setTessVariable("tessedit_enable_dict_correction", "1");
        tesseract.setTessVariable("textord_really_old_xheight", "1");
        tesseract.setTessVariable("tosp_threshold_bias2", "1");
        tesseract.setTessVariable("classify_norm_adj_midpoint", "96");
        tesseract.setTessVariable("tessedit_class_miss_scale", "0.002");
        tesseract.setTessVariable("textord_initialx_ile", "1.0");
        tesseract.setTessVariable("textord_min_linesize", "2.0");
        tesseract.setTessVariable("tessedit_do_invert", "0");

        return tesseract;
    }
}
