package markdown;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TestHeader3 {

    private static final Map<String, Integer> cache = new HashMap<>();
    private static final Pattern uppercasePattern = Pattern.compile("[A-Z]+");
    private static final Pattern htmlTagPattern = Pattern.compile("<[^>]+>");
    private static final Pattern whitespacePattern = Pattern.compile("\\s");
    private static final Pattern multipleDashPattern = Pattern.compile("-+");
    private static final Pattern leadingDigitPattern = Pattern.compile("^\\d");
    private static final Pattern re = Pattern.compile("[\\u2000-\\u206F\\u2E00-\\u2E7F\\\\'!\"#$%&()*+,./:;<=>?@\\[\\]^`{|}~]");

    public static String slugify(String str) {
        if (str == null) {
            return "";
        }

        String slug = str.trim();
        slug = uppercasePattern.matcher(slug).replaceAll(match -> match.group().toLowerCase());
        slug = htmlTagPattern.matcher(slug).replaceAll("");
        slug = re.matcher(slug).replaceAll("");
        slug = whitespacePattern.matcher(slug).replaceAll("-");
        slug = multipleDashPattern.matcher(slug).replaceAll("-");
        slug = leadingDigitPattern.matcher(slug).replaceAll("_$0");

        int count = cache.getOrDefault(slug, 0);
        count = cache.containsKey(slug) ? count + 1 : 0;
        cache.put(slug, count);

        if (count > 0) {
            slug = slug + "-" + count;
        }

        return slug;
    }

    public static void clearCache() {
        cache.clear();
    }

    public static void main(String[] args) {
        // Test the slugify function
        // 标题加粗文本-和-斜体文本-列表项链接文本图片描述代码
        // 标题加粗文本-和-斜体文本-列表项链接文本图片描述代码
        // _1标题加粗文本-和-斜体文本-列表项链接文本图片描述代码 前后端保持一致
        String testString = "# 1标题**加粗文本**\"\"\\\" ,和 *斜体文本*- 列表项,[链接文本](http://example.com),[图片描述](http://example.com/image.jpg)`代码`";
        String slug = slugify(testString);
        System.out.println("Slug: " + slug);

        // Clear the cache
        clearCache();
    }
}
