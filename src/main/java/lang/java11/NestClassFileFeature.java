package lang.java11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 〈文件读取 写入 字符串更方便〉<br>
 * 但是如果你通过反射API实现内部类访问外部类的私有属性和方法就会抛出IllegalStateException异常
 * Java 11 修复了反射不能访问的问题.
 * JVM 访问规则不允许嵌套类之间进行私有访问。我们能通过常规方式可以访问是因为 JVM 在编译时为我们隐式地创建了桥接方法。Java 11 中引入了两个新的属性：一个叫做 NestMembers 的属性，用于标识其它已知的静态 nest 成员；另外一个是每个 nest 成员都包含的 NestHost 属性，用于标识出它的 nest 宿主类。在编译期就映射了双方的寄宿关系，不再需要桥接了。
 *
 * @author Carrie
 * @create 2022/9/8
 * @since 1.0.0
 */
public class NestClassFileFeature {
    public static void main(String[] args) throws IOException {
        String filePath = "E:\\work\\新建文本文档.txt";
        Path path = Files.writeString(Path.of(filePath), "write string to file");
        String fileContent = Files.readString(path);
        System.out.println(fileContent);
    }
}