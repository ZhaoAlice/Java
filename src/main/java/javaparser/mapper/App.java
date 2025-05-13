// App.java
package javaparser.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        // 1. 配置参数
        String basePackage = "com.example.entity";
        String mapperXmlPath = "src/main/resources/mapper";
        String mybatisConfig = "mybatis-config.xml";
        
        // 2. 解析Mapper XML文件
        MapperXmlParser xmlParser = new MapperXmlParser();
        List<File> mapperXmlFiles = findMapperXmlFiles(mapperXmlPath);
        List<EntityInfo> entities = xmlParser.parseMapperXmls(mapperXmlFiles);
        
        // 3. 分析实体类
        EntityAnalyzer entityAnalyzer = new EntityAnalyzer();
        List<EntityInfo> javaEntities = entityAnalyzer.analyzeEntities(basePackage, "");
        mergeEntities(entities, javaEntities);
        
        // 4. 分析MyBatis配置
        if (!mybatisConfig.isEmpty()) {
            try (InputStream inputStream = Resources.getResourceAsStream(mybatisConfig)) {
                SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
                MyBatisConfigAnalyzer configAnalyzer = new MyBatisConfigAnalyzer();
                configAnalyzer.analyzeFromConfiguration(sqlSessionFactory, entities);
            }
        }
        
        // 5. 输出结果
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter()
                                     .writeValueAsString(entities));
    }
    
    private static List<File> findMapperXmlFiles(String mapperXmlPath) {
        List<File> xmlFiles = new ArrayList<>();
        File dir = new File(mapperXmlPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".xml"));
            if (files != null) {
                for (File file : files) {
                    xmlFiles.add(file);
                }
            }
        }
        return xmlFiles;
    }
    
    private static void mergeEntities(List<EntityInfo> mainList, List<EntityInfo> toMerge) {
        for (EntityInfo entity : toMerge) {
            boolean exists = mainList.stream()
                                   .anyMatch(e -> e.getClassName().equals(entity.getClassName()));
            if (!exists) {
                mainList.add(entity);
            }
        }
    }
}