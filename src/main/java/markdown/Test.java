package markdown;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.renderer.HeaderIdGenerator;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.core.HeadingParser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.NodeVisitor;
import com.vladsch.flexmark.util.ast.TextCollectingVisitor;
import com.vladsch.flexmark.util.ast.VisitHandler;
import com.vladsch.flexmark.util.ast.Visitor;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class Test {
    public static void main(String[] args) {
        String markdown = "# **Title** 1 Set business domain parameters by referring to the following table, with<font color=\"red\">*</font>Is a required parameter.\n## Subtitle 1.1\nSome text\n## Subtitle 1.2\nMore text\n# Title 2\nEven more text";

        HeaderIdGenerator generator = new HeaderIdGenerator();

        // "标题加粗文本""" , 和 斜体文本- 列表项,链接文本图片描述代码"
        String aa = """
            # 1标题**加粗文本**""\\" ,和 *斜体文本*- 列表项,[链接文本](http://example.com),[图片描述](http://example.com/image.jpg)`代码`\n
            sssfffccccccccc\n
            # 标题**加粗文本** 和 *斜体文本*- 列表项[链接文本](http://example.com)[图片描述](http://example.com/image.jpg)`代码`""";
        // 解析 Markdown 文本
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        Document document = parser.parse(aa);
        HtmlRenderer htmlRenderer = new HtmlRenderer.Builder().build();
        // 创建一个NodeVisitor来遍历解析树
        NodeVisitor visitor1 = new NodeVisitor(
            new VisitHandler<>(Heading.class, heading -> {
                // 使用 TextCollectingVisitor 提取标题的纯文本内容
                TextCollectingVisitor textCollectingVisitor = new TextCollectingVisitor();
                String headingText = textCollectingVisitor.collectAndGetText(heading);
                System.out.println("Heading level " + heading.getLevel() + ": " + headingText);
                System.out.println("Heading id is " + generator.getId(headingText));

                String renderStr = htmlRenderer.render(heading);
                System.out.println("Render Heading is " + renderStr);
                System.out.println("Render Heading id is " + heading.getAnchorRefId());

                // 获取标题下的内容
                StringBuilder content = new StringBuilder();
                Node nextNode = heading.getNext();
                while (nextNode != null && !(nextNode instanceof Heading)) {
                    content.append(textCollectingVisitor.collectAndGetText(nextNode)).append("\n");
                    nextNode = nextNode.getNext();
                }
                System.out.println("Content under heading: " + content.toString().trim());
            })
        );

        // 开始遍历
        visitor1.visit(document);
    }
}
