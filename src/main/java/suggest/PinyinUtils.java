package suggest;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.elasticsearch.analysis.PinyinConfig;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.analysis.PinyinTokenFilter;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class PinyinUtils {
    private static PinyinConfig config = new PinyinConfig();

    static {
        config.keepFirstLetter = true;
        config.keepNoneChinese = true;
        config.keepNoneChineseInFirstLetter = true;
        config.keepOriginal = false;
        config.keepFullPinyin = true;
        config.LimitFirstLetterLength = 5;
        config.lowercase = true;
    }

    /**
     * 获取拼音转换后的结果，保留英文与数字
     *
     * @param string 输入
     * @return PinyinEntity
     * @see PinyinEntity
     */
    public static PinyinEntity getPinyin(String string) {
        if (Strings.isNullOrEmpty(string)) return null;
        StringReader sr = new StringReader(string);
        Analyzer analyzer = new KeywordAnalyzer();
        PinyinTokenFilter filter = new PinyinTokenFilter(analyzer.tokenStream("f", sr), config);
        List<String> pinyin = new ArrayList<>();
        try {
            filter.reset();
            while (filter.incrementToken()) {
                CharTermAttribute ta = filter.getAttribute(CharTermAttribute.class);
                pinyin.add(ta.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (pinyin.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pinyin.size() - 1; i++) {
                sb.append(pinyin.get(i));
            }

            PinyinEntity entity = new PinyinEntity();
            entity.setFullPinyin(sb.toString());
            entity.setFirstLetters(pinyin.get(pinyin.size() - 1));
            entity.setOriginalText(string);
            entity.setOriginalTextLowerCase(string.toLowerCase());
            return entity;
        }

        return new PinyinEntity(null, null, string, string.toLowerCase());
    }
}



