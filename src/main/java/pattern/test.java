package pattern;

import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("localhost.*");
        System.out.println(pattern.matcher("localhost:8010").matches());
    }
}
