package javaparser.mapper;

import java.util.ArrayList;
import java.util.List;

public class EntityInfo {
    private String className;
    private String tableName;
    private List<PropertyInfo> properties = new ArrayList<>();
    private List<RelationInfo> relations = new ArrayList<>();
    private List<String> mapperInterfaces = new ArrayList<>();
    private List<String> sourceFiles = new ArrayList<>();
    
    // constructors, getters and setters
    
    public void addProperty(PropertyInfo property) {
        this.properties.add(property);
    }
    
    public void addRelation(RelationInfo relation) {
        this.relations.add(relation);
    }
    
    public void addMapperInterface(String mapperInterface) {
        this.mapperInterfaces.add(mapperInterface);
    }
    
    public void addSourceFile(String sourceFile) {
        this.sourceFiles.add(sourceFile);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<PropertyInfo> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyInfo> properties) {
        this.properties = properties;
    }

    public List<RelationInfo> getRelations() {
        return relations;
    }

    public void setRelations(List<RelationInfo> relations) {
        this.relations = relations;
    }

    public List<String> getMapperInterfaces() {
        return mapperInterfaces;
    }

    public void setMapperInterfaces(List<String> mapperInterfaces) {
        this.mapperInterfaces = mapperInterfaces;
    }

    public List<String> getSourceFiles() {
        return sourceFiles;
    }

    public void setSourceFiles(List<String> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }
}