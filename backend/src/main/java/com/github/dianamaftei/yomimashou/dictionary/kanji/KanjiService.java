package com.github.dianamaftei.yomimashou.dictionary.kanji;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;

import static com.github.dianamaftei.yomimashou.model.QKanjiEntry.kanjiEntry;

@Service
public class KanjiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KanjiService.class);

    private final JPAQueryFactory jpaQueryFactory;
    @Value("${kanji.path}")
    private String kanjiPath;

    @Autowired
    public KanjiService(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public Kanji get(String searchItem) {
        return (Kanji) jpaQueryFactory.query().from(kanjiEntry).where(kanjiEntry.kanji.eq(searchItem)).fetchOne();
    }

    @ResponseBody
    public byte[] getKanjiSVG(String kanji) {
        try (InputStream in = new FileInputStream(new File(kanjiPath + File.separator + kanji));
             ByteArrayOutputStream baos = new ByteArrayOutputStream();) {

            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            LOGGER.error("could not read kanji svg", e);
        }
        return new byte[0];
    }
}
