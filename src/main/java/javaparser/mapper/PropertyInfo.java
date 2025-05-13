package javaparser.mapper;

public class PropertyInfo {
    private String name;
    private String column;
    private String jdbcType;
    
    // constructors, getters and setters
    @Override
    public String toString() {
        return String.format("%s(%s:%s)", name, column, jdbcType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }
}