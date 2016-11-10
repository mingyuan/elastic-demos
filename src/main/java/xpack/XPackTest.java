package xpack;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

public class XPackTest {

    private static void addShutdownHook(TransportClient transportClient) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                transportClient.close();
            }
        });

    }

    public static void main(String[] args) throws UnknownHostException {
        System.setProperty("log4j.configurationFile", "conf/log4j.properties");
        System.setProperty("log4j2.disable.jmx", Boolean.TRUE.toString());
        Settings settings = Settings.builder().put("cluster.name", "es5").put("xpack.security.user", "").put("client.transport.sniff", true).build();
        TransportClient client = new PreBuiltXPackTransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("vm1"), 9300));
        addShutdownHook(client);
        Map<String, Object> sourceMap = new LinkedHashMap<>();
        sourceMap.put("title", "习近平会见洪秀柱 谈了这几点很重要2");
        IndexResponse indexResponse = client.prepareIndex("tag_v1", "tags", "1").setSource(sourceMap).get();
        System.out.println(indexResponse.toString());
    }
}
