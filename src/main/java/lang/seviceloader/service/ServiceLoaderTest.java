package lang.seviceloader.service;

import java.util.ServiceLoader;

public class ServiceLoaderTest {

    public static void main(String[] args) {
        //加载META-INF目录下的配置文件中所配置的实现类
        //java SPI机制
        ServiceLoader<MessageIntf> sLoader = ServiceLoader.load(MessageIntf.class);
        for (MessageIntf impl : sLoader) {
            System.out.println(impl.getMessage());
        }
    }
}
