package cn.mingyuan.elasticdemo.base;

import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author jiangmingyuan@myhaowai.com
 * @version 2016/11/10 14:00
 * @since jdk1.8
 */
public class SpringHolder {
    private static final FileSystemXmlApplicationContext applicationContext;

    static {
        applicationContext = new FileSystemXmlApplicationContext("conf/applicationContext.xml");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (applicationContext != null) {
                applicationContext.close();
            }
        }));
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }
}
