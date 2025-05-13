package javaparser.mapper;

public class RelationInfo {
    private String property;
    private String targetEntity;
    private String relationType; // "one-to-one", "one-to-many", "many-to-many"

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(String targetEntity) {
        this.targetEntity = targetEntity;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    // constructors, getters and setters
}