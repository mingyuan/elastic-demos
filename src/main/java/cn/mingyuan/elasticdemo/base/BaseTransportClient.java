package cn.mingyuan.elasticdemo.base;

import cn.mingyuan.elasticdemo.conf.ESConfig;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 *
 * 连接elastic集群，获取TransportClient
 *@author  jiangmingyuan@myhaowai.com
 *@version 2016/11/10 14:35
 *@since   jdk1.8
 */
public class BaseTransportClient {
    protected static final TransportClient client;

    static {
        System.setProperty("log4j.configurationFile", "conf/log4j.properties");
        System.setProperty("log4j2.disable.jmx", Boolean.TRUE.toString());
        ESConfig config = SpringHolder.getBean(ESConfig.class);
        Settings settings = Settings.builder().put("cluster.name", config.getClusterName()).put("xpack.security.user", config.getXpackUser() + ":" + config.getXpackPassword()).put("client.transport.sniff", config.isSniff()).build();

        client = new PreBuiltXPackTransportClient(settings);
        String clusterNodes = config.getClusterNodes();
        String[] addresses = clusterNodes.split(",");
        for (String address : addresses) {
            String[] hostport = address.split(":");
            try {
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostport[0]), Integer.parseInt(hostport[1])));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        addShutdownHook(client);
    }

    private static void addShutdownHook(TransportClient client) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (client != null) {
                System.out.println("... es client closing ...");
                client.close();
                System.out.println("... es client closed ...");
            }
        }));
    }
}
