package suggest;

import base.BaseTransportClient;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.Strings;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jiangmingyuan@myhaowai.com
 * @version 2016/11/8 13:56
 * @since jdk1.8
 */
public class SuggestIndexBuilder extends BaseTransportClient {

    @Data
    @AllArgsConstructor
    private static class SuggestInput {// suggest 字段要填充的内容
        private String input;
        private int weight;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class SuggestBean {
        private List<SuggestInput> name;// suggest 字段
        private String fullname;// 其余字段
    }

    private static void doIndex() throws IOException {
        List<String> readLines = FileUtils.readLines(new File("data/username_weight.txt"), "utf-8");

        for (String line : readLines) {
            if (Strings.isNullOrEmpty(line)) {
                continue;
            }

            String[] split = line.split(",");
            String name = split[0];
            if (Strings.isNullOrEmpty(name.trim())) {
                continue;
            }
            int weight = Integer.parseInt(split[1]);

            PinyinEntity pinyinEntity = PinyinUtils.getPinyin(name.toLowerCase());

            List<SuggestInput> inputs = new LinkedList<>();
            inputs.add(new SuggestInput(pinyinEntity.getFirstLetters(), weight));
            inputs.add(new SuggestInput(pinyinEntity.getFullPinyin(), weight));
            inputs.add(new SuggestInput(pinyinEntity.getOriginalTextLowerCase(), weight));
            String jsonString = JSON.toJSONString(new SuggestBean(inputs, name));
            System.out.println(jsonString);
            IndexResponse res = client.prepareIndex("username", "weixin").setSource(jsonString).get();
        }

    }

    public static void main(String[] args) throws IOException {
        doIndex();
    }

}
