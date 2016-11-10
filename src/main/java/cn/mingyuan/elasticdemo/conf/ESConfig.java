package cn.mingyuan.elasticdemo.conf;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangmingyuan@myhaowai.com
 * @version 2016/11/10 13:32
 * @since jdk1.8
 */
@Data
@NoArgsConstructor
public class ESConfig {
    private String clusterName;
    private boolean sniff;
    private String clusterNodes;
    private String xpackUser;
    private String xpackPassword;
}
