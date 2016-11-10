package cn.mingyuan.elasticdemo.suggest;

import cn.mingyuan.elasticdemo.base.BaseTransportClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.completion.FuzzyOptions;

/**
 * @author jiangmingyuan@myhaowai.com
 * @version 2016年11月7日 下午2:00:46
 * @since jdk1.8
 */
public class SuggestDemo extends BaseTransportClient {

    private static void getSuggest(String input) {
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        CompletionSuggestionBuilder completionSuggestion = SuggestBuilders.completionSuggestion("name");
        completionSuggestion.prefix(input, FuzzyOptions.builder().setFuzziness(Fuzziness.ZERO).setUnicodeAware(true).build());
        completionSuggestion.size(10);
        suggestBuilder.addSuggestion("mysuggestname", completionSuggestion);
        SearchResponse response = client.prepareSearch("username").suggest(suggestBuilder).get();
        Suggest suggest = response.getSuggest();
        if (suggest == null) {
            return;
        }
        System.out.println(suggest);
        JSONObject root = JSON.parseObject(suggest.toString());
        JSONArray jsonArray = root.getJSONObject("suggest").getJSONArray("mysuggestname");
        JSONArray optionsArray = jsonArray.getJSONObject(0).getJSONArray("options");

        optionsArray.forEach(option -> {
            String string = ((JSONObject) option).getJSONObject("_source").getString("fullname");
            System.out.println(string);
        });
    }

    public static void main(String[] args) {
        String input = "GV";
        getSuggest(input.toLowerCase());
    }
}
