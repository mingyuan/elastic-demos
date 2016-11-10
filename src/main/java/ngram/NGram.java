package ngram;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;

public class NGram {
    public static void bigram() throws IOException {

        CJKAnalyzer analyzer = new CJKAnalyzer();
        String text = "中华人民共和国";
        TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));
        tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            CharTermAttribute charTermAttribute = (CharTermAttribute) tokenStream.getAttribute(CharTermAttribute.class);
            System.out.println(charTermAttribute.toString());
        }

        analyzer.close();
    }

    public static void main(String[] args) throws IOException {
        bigram();
    }
}
