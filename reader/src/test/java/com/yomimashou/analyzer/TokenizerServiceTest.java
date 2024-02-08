package com.yomimashou.analyzer;

import com.atilika.kuromoji.ipadic.Tokenizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenizerServiceTest {

    @InjectMocks
    private TokenizerService tokenizerService;

    @Mock
    private Tokenizer tokenizer;

    @Test
    void tokenizeShouldReturnAListOfTokensFromTheGivenText() {
        // Arrange
        String text = "むかしむかし、貧しいけれど、心の優しいおじいさんとおばあさんがいました。";
        List<com.atilika.kuromoji.ipadic.Token> mockTokens = new ArrayList<>();
        mockTokens.add(buildMockKuromojiToken("むかし", "むかし", "名詞", "副詞可能"));
        mockTokens.add(buildMockKuromojiToken("むかし", "むかし", "名詞", "副詞可能"));
        mockTokens.add(buildMockKuromojiToken("、", "、", "記号", "読点"));
        mockTokens.add(buildMockKuromojiToken("貧しい", "貧しい", "形容詞", "自立"));
        mockTokens.add(buildMockKuromojiToken("けれど", "けれど", "助詞", "接続助詞"));
        mockTokens.add(buildMockKuromojiToken("、", "、", "記号", "読点"));
        mockTokens.add(buildMockKuromojiToken("心", "心", "名詞", "一般"));
        mockTokens.add(buildMockKuromojiToken("の", "の", "助詞", "連体化"));
        mockTokens.add(buildMockKuromojiToken("優しい", "優しい", "形容詞", "自立"));
        mockTokens.add(buildMockKuromojiToken("おじいさん", "おじいさん", "名詞", "一般"));
        mockTokens.add(buildMockKuromojiToken("と", "と", "助詞", "並立助詞"));
        mockTokens.add(buildMockKuromojiToken("おばあさん", "おばあさん", "名詞", "一般"));
        mockTokens.add(buildMockKuromojiToken("が", "が", "助詞", "格助詞"));
        mockTokens.add(buildMockKuromojiToken("い", "いる", "動詞", "自立"));
        mockTokens.add(buildMockKuromojiToken("まし", "ます", "助動詞", "*"));
        mockTokens.add(buildMockKuromojiToken("た", "た", "助動詞", "*"));
        mockTokens.add(buildMockKuromojiToken("。", "。", "記号", "句点"));
        when(tokenizer.tokenize(text)).thenReturn(mockTokens);

        List<Token> expectedTokens = new ArrayList<>();
        expectedTokens.add(buildToken("むかし", "むかし", "名詞", "副詞可能"));
        expectedTokens.add(buildToken("むかし", "むかし", "名詞", "副詞可能"));
        expectedTokens.add(buildToken("貧しいけれど", "貧しい", "形容詞", "自立"));
        expectedTokens.add(buildToken("心", "心", "名詞", "一般"));
        expectedTokens.add(buildToken("の", "の", "助詞", "連体化"));
        expectedTokens.add(buildToken("優しい", "優しい", "形容詞", "自立"));
        expectedTokens.add(buildToken("おじいさん", "おじいさん", "名詞", "一般"));
        expectedTokens.add(buildToken("と", "と", "助詞", "並立助詞"));
        expectedTokens.add(buildToken("おばあさん", "おばあさん", "名詞", "一般"));
        expectedTokens.add(buildToken("が", "が", "助詞", "格助詞"));
        expectedTokens.add(buildToken("いました", "いる", "動詞", "自立"));

        // Act
        List<Token> actualTokens = tokenizerService.tokenize(text);

        // Assert
        // symbols were not included
        // inflected forms were merged
        assertThat(actualTokens).isEqualTo(expectedTokens);
    }

    private com.atilika.kuromoji.ipadic.Token buildMockKuromojiToken(String surface, String base, String pos1, String pos2) {
        com.atilika.kuromoji.ipadic.Token mockToken = mock(com.atilika.kuromoji.ipadic.Token.class);
        when(mockToken.getSurface()).thenReturn(surface);
        when(mockToken.getBaseForm()).thenReturn(base);
        when(mockToken.getReading()).thenReturn(surface);
        when(mockToken.getPartOfSpeechLevel1()).thenReturn(pos1);
        when(mockToken.getPartOfSpeechLevel2()).thenReturn(pos2);
        return mockToken;
    }

    private Token buildToken(String surface, String base, String pos1, String pos2) {
        return Token.builder().surface(surface).baseForm(base).reading(surface).partOfSpeechLevel1(pos1).partOfSpeechLevel2(pos2).build();
    }
}
