package base;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class BaseTransportClient {
    protected static final TransportClient client;

    static {
        System.setProperty("log4j.configurationFile", "conf/log4j.properties");
        System.setProperty("log4j2.disable.jmx", Boolean.TRUE.toString());
        Settings settings = Settings.builder().put("cluster.name", "es5").put("xpack.security.user", "mingyuan:haowai*#2016").put("client.transport.sniff", true).build();

        client = new PreBuiltXPackTransportClient(settings);
        try {
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("vm1"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        addShutdownHook(client);
    }

    private static void addShutdownHook(TransportClient client) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (client != null) {
                    client.close();
                    System.out.println("... es client closed ...");
                }
            }
        });
    }

}
