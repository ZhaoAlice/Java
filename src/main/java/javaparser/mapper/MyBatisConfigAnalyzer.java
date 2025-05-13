// MyBatisConfigAnalyzer.java
package javaparser.mapper;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class MyBatisConfigAnalyzer {
    public void analyzeFromConfiguration(SqlSessionFactory sqlSessionFactory, List<EntityInfo> entities) {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        
        // 关联Mapper接口和实体
        configuration.getMapperRegistry().getMappers().forEach(mapperClass -> {
            String mapperInterface = mapperClass.getName();
            String entityClassName = guessEntityClassFromMapper(mapperInterface);
            
            entities.stream()
                   .filter(e -> e.getClassName().equals(entityClassName))
                   .findFirst()
                   .ifPresent(entity -> entity.addMapperInterface(mapperInterface));
        });
        
        // 补充resultMap信息
        configuration.getResultMaps().forEach(resultMap -> {
            String entityClassName = resultMap.getType().getName();
            entities.stream()
                   .filter(e -> e.getClassName().equals(entityClassName))
                   .findFirst()
                   .ifPresent(entity -> {
                       if (entity.getTableName() == null) {
                           entity.setTableName(resultMap.getId().replace("BaseResultMap", ""));
                       }
                   });
        });
    }
    
    private String guessEntityClassFromMapper(String mapperInterface) {
        // 简单逻辑：假设Mapper接口名为XxxMapper，实体类名为Xxx
        String simpleName = mapperInterface.substring(mapperInterface.lastIndexOf('.') + 1);
        if (simpleName.endsWith("Mapper")) {
            String entitySimpleName = simpleName.substring(0, simpleName.length() - "Mapper".length());
            return mapperInterface.substring(0, mapperInterface.lastIndexOf('.') + 1) + entitySimpleName;
        }
        return null;
    }
}