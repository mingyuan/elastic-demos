package cn.mingyuan.elasticdemo.suggest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PinyinEntity {
    private String fullPinyin;
    private String firstLetters;
    private String originalText;
    private String originalTextLowerCase;
}
