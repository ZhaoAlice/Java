package spring;

import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerExample {
    public static void main(String[] args) throws Exception {
        // 配置 FreeMarker
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassForTemplateLoading(FreeMarkerExample.class, "/templates"); // 模板文件路径
        cfg.setDefaultEncoding("UTF-8");

        // 加载模板
        Template template = cfg.getTemplate("user_card.ftl");

        // 数据模型
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("userName", "张三");
        dataModel.put("age", 16);
        dataModel.put("gender", "male");
        dataModel.put("isPremium", true);
        dataModel.put("aa", "true");

        // 输出模板结果
        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);
        System.out.println(writer.toString());
    }
}
