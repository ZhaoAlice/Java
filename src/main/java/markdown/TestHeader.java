package markdown;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestHeader {

    public static class ConfigResult {
        private String str;
        private Map<String, Object> config;

        public ConfigResult(String str, Map<String, Object> config) {
            this.str = str;
            this.config = config;
        }

        public String getStr() {
            return str;
        }

        public Map<String, Object> getConfig() {
            return config;
        }
    }

    public static ConfigResult getAndRemoveConfig(String str) {
        if (str == null) {
            str = "";
        }

        Map<String, Object> config = new HashMap<>();

        if (!str.isEmpty()) {
            // Remove leading and trailing quotes
            str = str.replaceAll("^([\"'])", "").replaceAll("([\"'])$", "");

            // Regex pattern to match :key=value or :key
            Pattern pattern = Pattern.compile("(?:^|\\s):([\\w-]+:?)=?([\\w-%]+)?");
            Matcher matcher = pattern.matcher(str);

            StringBuilder sb = new StringBuilder();
            while (matcher.find()) {
                String key = matcher.group(1);
                String value = matcher.group(2);

                if (!key.contains(":")) {
                    config.put(key, value != null ? value.replace("&quot;", "") : true);
                    matcher.appendReplacement(sb, "");  // Remove matched part
                } else {
                    matcher.appendReplacement(sb, matcher.group());  // Keep matched part
                }
            }
            matcher.appendTail(sb);
            str = sb.toString().trim();
        }

        return new ConfigResult(str, config);
    }

    public static void main(String[] args) {
        String input = "<h1>标题<strong>加粗文本</strong>&quot;&quot;&quot; ,和 <em>斜体文本</em>- 列表项,链接文本,图片描述<code>代码</code></h1>";
        ConfigResult result = getAndRemoveConfig(input);
        System.out.println("Remaining String: " + result.getStr());
        System.out.println("Config: " + result.getConfig());
    }
}
