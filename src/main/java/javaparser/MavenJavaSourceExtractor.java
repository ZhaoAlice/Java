package javaparser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MavenJavaSourceExtractor {

    public static void main(String[] args) {
        //if (args.length != 1) {
        //    System.err.println("Usage: java MavenJavaSourceExtractor <root_directory>");
        //    System.exit(1);
        //}

        String rootDir = "D:\\work\\myself\\code\\Java";
        List<String> javaSourcePaths = findMavenJavaSourcePaths(rootDir);

        System.out.println("Found " + javaSourcePaths.size() + " Maven modules with Java sources:");
        javaSourcePaths.forEach(System.out::println);

        Path basePath = Paths.get("/home/user");
        Path fullPath = basePath.resolve("project").resolve("src").resolve("main").resolve("java");
        System.out.println(fullPath);

    }

    /**
     * 查找所有 Maven 模块的 Java 源代码路径
     * @param rootDir 根目录路径
     * @return Java 源代码路径列表
     */
    public static List<String> findMavenJavaSourcePaths(String rootDir) {
        List<String> javaSourcePaths = new ArrayList<>();
        File root = new File(rootDir);

        if (!root.exists() || !root.isDirectory()) {
            System.err.println("Invalid directory: " + rootDir);
            return javaSourcePaths;
        }

        // 查找所有包含 pom.xml 的目录（Maven 模块）
        List<File> mavenModules = findMavenModules(root);
        
        // 为每个模块查找 Java 源代码路径
        for (File module : mavenModules) {
            Path javaSourcePath = Paths.get(module.getAbsolutePath(), "src", "main", "java");
            if (Files.exists(javaSourcePath) && Files.isDirectory(javaSourcePath)) {
                javaSourcePaths.add(javaSourcePath.toString());
            }
        }

        return javaSourcePaths;
    }

    /**
     * 递归查找所有包含 pom.xml 的目录（Maven 模块）
     * @param directory 起始目录
     * @return Maven 模块目录列表
     */
    private static List<File> findMavenModules(File directory) {
        List<File> mavenModules = new ArrayList<>();
        
        // 检查当前目录是否是 Maven 模块
        File pomFile = new File(directory, "pom.xml");
        if (pomFile.exists() && pomFile.isFile()) {
            mavenModules.add(directory);
            // 不检查子目录，因为子模块通常由父 pom 管理
            return mavenModules;
        }
        
        // 递归检查子目录
        File[] subDirs = directory.listFiles(File::isDirectory);
        if (subDirs != null) {
            for (File subDir : subDirs) {
                mavenModules.addAll(findMavenModules(subDir));
            }
        }
        
        return mavenModules;
    }

    /**
     * 替代方法：使用 Java 8 Stream API 实现
     * @param rootDir 根目录路径
     * @return Java 源代码路径列表
     */
    public static List<String> findMavenJavaSourcePathsWithStream(String rootDir) {
        try {
            return Files.walk(Paths.get(rootDir))
                    .filter(path -> path.endsWith("pom.xml"))
                    .map(Path::getParent)
                    .map(modulePath -> modulePath.resolve("src/main/java"))
                    .filter(path -> Files.exists(path) && Files.isDirectory(path))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error scanning directory: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}