package suggest;

import org.junit.Test;

/**
 * Created by jiangmingyuan@myhaowai.com on 2016/11/8.
 */
public class PinyinUtilsTest {
    @org.junit.After
    public void tearDown() throws Exception {
        System.out.println("tearDown");
    }

    @Test
    public void test() {
        String s = "ga公1司的1号机器人很牛的";
        PinyinEntity pinyin2 = PinyinUtils.getPinyin(s);
        System.out.println(pinyin2);
    }

}