package net;

import java.lang.reflect.Method;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReflectionExample {
    public static void main(String[] args) {
        try {
            // 创建 User1 的实例
            Object user1 = new User1();

            // 获取 User1 的 Class 对象
            Class<?> clazz = user1.getClass();

            // 动态调用 method1（无参数）
            Method method1 = clazz.getMethod("method1");
            method1.invoke(user1); // 输出：执行方法1

            // 动态调用 method2（带一个参数）
            Method method2 = clazz.getMethod("method2", String.class);
            System.out.println(method2.getName());
            method2.invoke(user1, "Hello"); // 输出：执行方法2，参数：Hello

            // 动态调用 method3（带两个参数，并返回结果）
            Method method3 = clazz.getMethod("method3", int[].class);
            int[] arr = {5, 10};
            Object result = method3.invoke(user1, arr);
            System.out.println("method3 返回结果：" + result); // 输出：method3 返回结果：15


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // 示例 JSON 数据
            String json = "{\n" +
                "  \"user\": {\n" +
                "    \"id1\": null,\n" +
                "    \"id2\": false,\n" +
                "    \"id\": 123,\n" +
                "    \"name\": \"John Doe\",\n" +
                "    \"address\": {\n" +
                "      \"city\": \"New York\",\n" +
                "      \"zipcode\": \"10001\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

            // 创建 ObjectMapper 实例
            ObjectMapper objectMapper = new ObjectMapper();

            System.out.println("test空字符串: "+ objectMapper.writeValueAsString(""));
            // 将 JSON 字符串解析为 JsonNode
            JsonNode rootNode = objectMapper.readTree(json);

            // 动态获取路径值
            String name = getValueByPath(rootNode, "user.name");
            System.out.println("User Name: " + name); // 输出: User Name: John Doe

            // 动态值显示为空的节点
            String name1 = getValueByPath(rootNode, "user.id1");
            System.out.println("User Name1: " + name1); // 输出: User Name: John Doe

            // 动态值显示为空的节点
            String name2 = getValueByPath(rootNode, "user.id2");
            System.out.println("User Name2: " + name2); // 输出: User Name: John Doe

            String city = getValueByPath(rootNode, "user.address.city");
            System.out.println("City: " + city); // 输出: City: New York

            String zipcode = getValueByPath(rootNode, "user.address.zipcode");
            System.out.println("Zipcode: " + zipcode); // 输出: Zipcode: 10001

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据路径获取 JSON 节点的值
     * @param rootNode 根节点
     * @param path 路径，使用 "." 分隔
     * @return 节点值（如果节点不存在，返回 null）
     */
    public static String getValueByPath(JsonNode rootNode, String path) {
        String[] fields = path.split("\\.");
        JsonNode currentNode = rootNode;

        for (String field : fields) {
            if (currentNode == null) {
                return null;
            }
            JsonNode currentNode1 = currentNode.get("aa");
            currentNode = currentNode.path(field);
        }
        if (currentNode.isMissingNode()) {
            return null;
        }
        else {
            System.out.println(currentNode.asBoolean(true));
            return currentNode.asText();
        }
    }

    public static interface User {
        // 接口中没有方法
    }

    public static class User1 implements User {

        public void method1() {
            System.out.println("执行方法1");
        }

        public void method2(String param) {
            System.out.println("执行方法2，参数：" + param);
        }

        public int method3(int... a) {
            System.out.println("执行方法3，参数：" + a[0] + " 和 " + a[1]);
            return a[1] + a[0];
        }
    }

}
