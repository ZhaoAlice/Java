package net;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * 忘记是从哪里copy过来的了
 */
public class TestJavaNet {
    public static void main(String[] args) throws IOException{
        InetAddress inetAddress;//声明InetAddress对象
        try {
            inetAddress=InetAddress.getLocalHost();//实例化InetAddress对象，返回本地主机
            String hostName=inetAddress.getHostName();//获取本地主机名
            String canonicalHostName=inetAddress.getCanonicalHostName();//获取此 IP地址的完全限定域名
            byte[] address=inetAddress.getAddress();//获取原始IP地址
            int a=0;
            if(address[3]<0){
                a=address[3]+256;
            }
            String hostAddress=inetAddress.getHostAddress();//获取本地主机的IP地址
            boolean reachable=inetAddress.isReachable(2000);//获取布尔类型，看是否能到达此IP地址
            System.out.println(inetAddress.toString());
            System.out.println("主机名为："+hostName);//输出本地主机名
            System.out.println("此IP地址的完全限定域名："+canonicalHostName);//输出此IP地址的完全限定域名
            System.out.println("原始IP地址为："+address[0]+"."+address[1]+"."+address[2]+"."+a);//输出本地主机的原始IP地址
            System.out.println("IP地址为："+hostAddress);//输出本地主机的IP地址
            System.out.println("是否能到达此IP地址："+reachable);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        getLocalIP();
    }
    /**
     * 本地IP <br>
     *
     * @return <br>
     * @author henry<br>
     * @taskId <br>
     */
    public static String getLocalIP() {
        try {
            // Traversal Network interface to get the first non-loopback and non-private address
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            ArrayList<String> ipv4Result = new ArrayList<String>();
            ArrayList<String> ipv6Result = new ArrayList<String>();
            while (enumeration.hasMoreElements()) {
                final NetworkInterface networkInterface = enumeration.nextElement();
                final Enumeration<InetAddress> en = networkInterface.getInetAddresses();
                while (en.hasMoreElements()) {
                    final InetAddress address = en.nextElement();
                    System.out.println(address.getHostAddress());
                    System.out.println(networkInterface.getName());
                    System.out.println(address.isLoopbackAddress());
                    if (!address.isLoopbackAddress()) {
                        if (address instanceof Inet6Address) {
                            ipv6Result.add(normalizeHostAddress(address));
                        }
                        else {
                            ipv4Result.add(normalizeHostAddress(address));
                        }
                    }
                }
            }

            System.out.println("ipv4: " + ipv4Result);
            System.out.println("ipv6: " + ipv6Result);

            // prefer ipv4
            if (!ipv4Result.isEmpty()) {
                for (String ip : ipv4Result) {
                    if (ip.startsWith("127.0") || ip.startsWith("192.168")) {
                        continue;
                    }

                    return ip;
                }

                return ipv4Result.get(ipv4Result.size() - 1);
            }
            else if (!ipv6Result.isEmpty()) {
                return ipv6Result.get(0);
            }
            //If failed to find,fall back to localhost
            final InetAddress localHost = InetAddress.getLocalHost();
            return normalizeHostAddress(localHost);
        }
        catch (Exception e) {
        }

        return "";
    }
    private static String normalizeHostAddress(final InetAddress localHost) {
        if (localHost instanceof Inet6Address) {
            return "[" + localHost.getHostAddress() + "]";
        }
        else {
            return localHost.getHostAddress();
        }
    }
}