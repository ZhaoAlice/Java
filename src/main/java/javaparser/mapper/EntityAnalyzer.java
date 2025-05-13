// EntityAnalyzer.java
package javaparser.mapper;


import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class EntityAnalyzer {
    public List<EntityInfo> analyzeEntities(String packageName, String classpath) throws Exception {
        List<EntityInfo> entities = new ArrayList<>();
        
        // 使用Reflections扫描类路径
        Reflections reflections = new Reflections(
            new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forClassLoader())
                .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
                .filterInputsBy(input -> input.startsWith(packageName.replace(".", "/")))
        );
        
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
        
        for (Class<?> clazz : classes) {
            // 简单判断是否是实体类：有属性且有getter/setter
            if (isPotentialEntity(clazz)) {
                EntityInfo entity = new EntityInfo();
                entity.setClassName(clazz.getName());
                
                // 分析属性
                for (Field field : clazz.getDeclaredFields()) {
                    PropertyInfo prop = new PropertyInfo();
                    prop.setName(field.getName());
                    prop.setJdbcType(mapJavaTypeToJdbcType(field.getType().getSimpleName()));
                    entity.addProperty(prop);
                }
                
                entities.add(entity);
            }
        }
        
        return entities;
    }
    
    private boolean isPotentialEntity(Class<?> clazz) {
        // 简单的实体类判断逻辑
        return clazz.getDeclaredFields().length > 0 && 
               Stream.of(clazz.getDeclaredMethods())
                    .anyMatch(m -> m.getName().startsWith("get"));
    }
    
    private String mapJavaTypeToJdbcType(String javaType) {
        switch (javaType.toLowerCase()) {
            case "string": return "VARCHAR";
            case "int": case "integer": return "INTEGER";
            case "long": return "BIGINT";
            case "double": return "DOUBLE";
            case "float": return "FLOAT";
            case "boolean": return "BOOLEAN";
            case "date": return "TIMESTAMP";
            case "bigdecimal": return "DECIMAL";
            default: return "OTHER";
        }
    }
}