package lang.java11;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2022/9/8
 * @since 1.0.0
 */
public class StringFeature {
    public static void main(String[] args) {
        // string isBlank
        String blankStr = "   ";
        String blankWhStr = "aa  ";
        System.out.println(blankStr.isBlank());
        System.out.println(blankWhStr.isBlank());
        String lineStr = "hello java11 \n string split \n string end";
        // stream
        lineStr.lines().forEach(s -> System.out.println(s));

        // strip 去除全角与半角空白字符
        String str = "strip ";
        str.stripLeading();
        str.stripTrailing();
        str.stripIndent();
        System.out.println(str.repeat(3));
        System.out.println(str.strip());
    }
}