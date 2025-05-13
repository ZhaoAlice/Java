// MapperXmlParser.java
package javaparser.mapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MapperXmlParser {
    public List<EntityInfo> parseMapperXmls(List<File> mapperXmlFiles) throws Exception {
        List<EntityInfo> entities = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        for (File xmlFile : mapperXmlFiles) {
            Document doc = builder.parse(xmlFile);
            parseResultMaps(doc, entities, xmlFile.getAbsolutePath());
            parseAssociations(doc, entities);
        }
        
        return entities;
    }
    
    private void parseResultMaps(Document doc, List<EntityInfo> entities, String sourceFile) {
        NodeList resultMaps = doc.getElementsByTagName("resultMap");
        
        for (int i = 0; i < resultMaps.getLength(); i++) {
            Node node = resultMaps.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String type = element.getAttribute("type");
                String id = element.getAttribute("id");
                
                if (!type.isEmpty()) {
                    EntityInfo entity = findOrCreateEntity(entities, type);
                    entity.addSourceFile(sourceFile);
                    
                    // 尝试从resultMap id推断表名
                    if (id.contains("BaseResultMap") || id.contains("ResultMap")) {
                        String tableName = id.replace("BaseResultMap", "")
                                           .replace("ResultMap", "");
                        if (!tableName.isEmpty()) {
                            entity.setTableName(tableName.toLowerCase());
                        }
                    }
                    
                    NodeList mappings = element.getElementsByTagName("result");
                    for (int j = 0; j < mappings.getLength(); j++) {
                        Node mapping = mappings.item(j);
                        if (mapping.getNodeType() == Node.ELEMENT_NODE) {
                            Element mappingElement = (Element) mapping;
                            PropertyInfo prop = new PropertyInfo();
                            prop.setName(mappingElement.getAttribute("property"));
                            prop.setColumn(mappingElement.getAttribute("column"));
                            prop.setJdbcType(mappingElement.getAttribute("jdbcType"));
                            entity.addProperty(prop);
                        }
                    }
                    
                    // 处理id元素
                    NodeList idMappings = element.getElementsByTagName("id");
                    for (int j = 0; j < idMappings.getLength(); j++) {
                        Node idMapping = idMappings.item(j);
                        if (idMapping.getNodeType() == Node.ELEMENT_NODE) {
                            Element idElement = (Element) idMapping;
                            PropertyInfo prop = new PropertyInfo();
                            prop.setName(idElement.getAttribute("property"));
                            prop.setColumn(idElement.getAttribute("column"));
                            prop.setJdbcType(idElement.getAttribute("jdbcType"));
                            entity.addProperty(prop);
                        }
                    }
                }
            }
        }
    }
    
    private void parseAssociations(Document doc, List<EntityInfo> entities) {
        NodeList associations = doc.getElementsByTagName("association");
        parseRelations(associations, entities, "one-to-one");
        
        NodeList collections = doc.getElementsByTagName("collection");
        parseRelations(collections, entities, "one-to-many");
    }
    
    private void parseRelations(NodeList relationNodes, List<EntityInfo> entities, String relationType) {
        for (int i = 0; i < relationNodes.getLength(); i++) {
            Node node = relationNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String property = element.getAttribute("property");
                String javaType = element.getAttribute("javaType");
                String ofType = element.getAttribute("ofType");
                
                String targetEntity = !javaType.isEmpty() ? javaType : ofType;
                if (!property.isEmpty() && !targetEntity.isEmpty()) {
                    // 找到拥有此关系的实体
                    String namespace = element.getOwnerDocument()
                                             .getDocumentElement()
                                             .getAttribute("namespace");
                    EntityInfo entity = findEntityByMapperInterface(entities, namespace);
                    if (entity != null) {
                        RelationInfo relation = new RelationInfo();
                        relation.setProperty(property);
                        relation.setTargetEntity(targetEntity);
                        relation.setRelationType(relationType);
                        entity.addRelation(relation);
                    }
                }
            }
        }
    }
    
    private EntityInfo findOrCreateEntity(List<EntityInfo> entities, String className) {
        return entities.stream()
                     .filter(e -> e.getClassName().equals(className))
                     .findFirst()
                     .orElseGet(() -> {
                         EntityInfo newEntity = new EntityInfo();
                         newEntity.setClassName(className);
                         entities.add(newEntity);
                         return newEntity;
                     });
    }
    
    private EntityInfo findEntityByMapperInterface(List<EntityInfo> entities, String mapperInterface) {
        return entities.stream()
                     .filter(e -> e.getMapperInterfaces().contains(mapperInterface))
                     .findFirst()
                     .orElse(null);
    }
}