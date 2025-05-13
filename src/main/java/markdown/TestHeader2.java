package markdown;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestHeader2 {

    public static class ConfigResult {
        private String content;
        private boolean ignoreAllSubs;
        private boolean ignoreSubHeading;

        public ConfigResult(String content, boolean ignoreAllSubs, boolean ignoreSubHeading) {
            this.content = content;
            this.ignoreAllSubs = ignoreAllSubs;
            this.ignoreSubHeading = ignoreSubHeading;
        }

        public String getContent() {
            return content;
        }

        public boolean isIgnoreAllSubs() {
            return ignoreAllSubs;
        }

        public boolean isIgnoreSubHeading() {
            return ignoreSubHeading;
        }
    }

    public static ConfigResult getAndRemoveDocsifyIgnoreConfig(String content) {
        boolean ignoreAllSubs = false;
        boolean ignoreSubHeading = false;

        if (content == null) {
            content = "";
        }

        Pattern ignorePattern = Pattern.compile("<!-- \\{docsify-ignore\\} -->");
        Matcher ignoreMatcher = ignorePattern.matcher(content);
        if (ignoreMatcher.find()) {
            content = ignoreMatcher.replaceAll("");
            ignoreSubHeading = true;
        }

        ignorePattern = Pattern.compile("\\{docsify-ignore\\}");
        ignoreMatcher = ignorePattern.matcher(content);
        if (ignoreMatcher.find()) {
            content = ignoreMatcher.replaceAll("");
            ignoreSubHeading = true;
        }

        Pattern ignoreAllPattern = Pattern.compile("<!-- \\{docsify-ignore-all\\} -->");
        Matcher ignoreAllMatcher = ignoreAllPattern.matcher(content);
        if (ignoreAllMatcher.find()) {
            content = ignoreAllMatcher.replaceAll("");
            ignoreAllSubs = true;
        }

        ignoreAllPattern = Pattern.compile("\\{docsify-ignore-all\\}");
        ignoreAllMatcher = ignoreAllPattern.matcher(content);
        if (ignoreAllMatcher.find()) {
            content = ignoreAllMatcher.replaceAll("");
            ignoreAllSubs = true;
        }

        return new ConfigResult(content, ignoreAllSubs, ignoreSubHeading);
    }

    public static void main(String[] args) {
        String content = "<!-- {docsify-ignore} -->This is a test content.<!-- {docsify-ignore-all} -->";
        ConfigResult result = getAndRemoveDocsifyIgnoreConfig(content);
        System.out.println("Content: " + result.getContent());
        System.out.println("Ignore All Subs: " + result.isIgnoreAllSubs());
        System.out.println("Ignore Sub Heading: " + result.isIgnoreSubHeading());
    }
}
