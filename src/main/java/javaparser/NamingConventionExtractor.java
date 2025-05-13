package javaparser;

import java.util.Optional;

public class NamingConventionExtractor {

    public static class ClassComponents {
        private final String packagePath; // 全小写
        private final String className;   // 大驼峰格式
        private final boolean isValid;

        public ClassComponents(String packagePath, String className, boolean isValid) {
            this.packagePath = packagePath;
            this.className = className;
            this.isValid = isValid;
        }

        public Optional<String> getPackagePath() {
            return Optional.ofNullable(packagePath);
        }

        public String getClassName() {
            return className;
        }

        public boolean isValid() {
            return isValid;
        }

        @Override
        public String toString() {
            return (packagePath != null ? packagePath + "." : "") + className;
        }
    }

    /**
        * 基于命名规范提取包路径和类名
     * @param fullQualifiedName 全限定类名
     * @return 包含包路径和类名的对象
     */
    public static ClassComponents extract(String fullQualifiedName) {
        if (fullQualifiedName == null || fullQualifiedName.trim().isEmpty()) {
            return new ClassComponents(null, "", false);
        }

        String cleaned = fullQualifiedName.trim()
            .replaceAll("\\$\\$", "")  // 去除数组符号
            .replaceAll("<.*>", "");   // 去除泛型

        // 处理内部类（取最外层类名）
        int innerClassSeparator = cleaned.indexOf('$');
        if (innerClassSeparator > 0) {
            cleaned = cleaned.substring(0, innerClassSeparator);
        }

        // 找到最后一个从大写字母开始的类名部分
        int lastUpperIndex = findLastUpperCaseTransition(cleaned);

        if (lastUpperIndex <= 0) {
            // 没有符合大驼峰格式的类名
            return new ClassComponents(null, cleaned, false);
        }

        String packagePath = cleaned.substring(0, lastUpperIndex).toLowerCase();
        String className = cleaned.substring(lastUpperIndex + 1);

        // 验证包路径是否全小写
        boolean isPackageValid = packagePath.toLowerCase().equals(packagePath);
        // 验证类名是否大驼峰
        boolean isClassValid = Character.isUpperCase(className.charAt(0));
        return new ClassComponents(
            isPackageValid ? packagePath : null,
            isClassValid ? className : cleaned.substring(lastUpperIndex),
            isPackageValid && isClassValid
        );
    }

    /**
        * 找到最后一个从小写到大写的过渡点
     */
    private static int findLastUpperCaseTransition(String fullClassName) {
        if (fullClassName == null || fullClassName.isEmpty()) {
            return -1;
        }

        // 反向查找，找到最后一个点后跟着大写字母的位置
        for (int i = 0; i < fullClassName.length(); i++) {
            if (fullClassName.charAt(i) == '.' &&
                Character.isUpperCase(fullClassName.charAt(i + 1))) {
                return i; // 返回大写字母的位置
            }
        }

        return -1; // 没有找到符合条件的位置
    }

    public static void main(String[] a) {
        //testExtraction("com.example.MyClass");
        //testExtraction("java.util.ArrayList");
        testExtraction("org.springframework.web.servlet.TT.HandlerInterceptor");
        //testExtraction("MyClass");
        //testExtraction("com.example.InvalidClass");
        //testExtraction("com.Example.MyClass");
        //testExtraction("test.comprehensive.InnerClass$Nested");
        //testExtraction("int[]");
        //testExtraction("java.util.List<String>");
    }

    private static void testExtraction(String fullName) {
        ClassComponents components = extract(fullName);
        System.out.printf("原始: %-45s → 包: %-30s 类名: %-20s 有效: %b%n",
            fullName,
            components.getPackagePath().orElse("(无)"),
            components.getClassName(),
            components.isValid());
    }
}