package javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.nio.file.Path;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CodeAnalyzer {
    public static final String TEST = "test";
    private final Connection connection;
    private final Map<String, Integer> packageCache = new HashMap<>();
    private final Map<String, Integer> classCache = new HashMap<>();

    public CodeAnalyzer(Connection connection) {
        this.connection = connection;
    }

    public void analyzeAndStore(String javaCode) throws Exception {
        JavaParser parser = new JavaParser();
        ParseResult<CompilationUnit> parseResult = parser.parse(javaCode);

        if (!parseResult.isSuccessful() || parseResult.getResult().isEmpty()) {
            throw new IllegalArgumentException("解析失败: " + parseResult.getProblems());
        }

        CompilationUnit cu = parseResult.getResult().get();

        // 处理包信息
        String packageName = cu.getPackageDeclaration()
            .map(pd -> pd.getNameAsString())
            .orElse("");
        int packageId = storePackage(packageName);

        // 处理所有类型声明
        for (TypeDeclaration<?> type : cu.getTypes()) {
            processType(type, packageId);
        }
    }

    public static void test() {

    }
    private int storePackage(String packageName) throws SQLException {
        if (packageCache.containsKey(packageName)) {
            return packageCache.get(packageName);
        }

        String sql = "INSERT INTO packages (name) VALUES (?) ON DUPLICATE KEY UPDATE id=id";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, packageName);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    packageCache.put(packageName, id);
                    return id;
                }
            }

            // 查询现有ID
            try (PreparedStatement query = connection.prepareStatement(
                "SELECT id FROM packages WHERE name = ?")) {
                query.setString(1, packageName);
                try (ResultSet rs = query.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        packageCache.put(packageName, id);
                        return id;
                    }
                }
            }
        }
        throw new SQLException("存储包失败: " + packageName);
    }

    private void processType(TypeDeclaration<?> type, int packageId) throws Exception {
        int classId = storeClass(type, packageId);

        // 处理类/接口声明
        if (type instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) type;

            // 处理继承
            for (ClassOrInterfaceType extendedType : coid.getExtendedTypes()) {
                processInheritance(classId, extendedType);
            }

            // 处理接口实现
            for (ClassOrInterfaceType implementedType : coid.getImplementedTypes()) {
                processImplementation(classId, implementedType);
            }
        }

        // 处理字段
        for (FieldDeclaration field : type.getFields()) {
            processField(classId, field);
        }

        // 处理方法
        for (MethodDeclaration method : type.getMethods()) {
            processMethod(classId, method);
        }

        // 处理内部类型
        for (BodyDeclaration<?> member : type.getMembers()) {
            if (member instanceof TypeDeclaration) {
                processType((TypeDeclaration<?>) member, packageId);
            }
        }
    }

    private int storeClass(TypeDeclaration<?> type, int packageId) throws SQLException {
        String className = type.getNameAsString();
        boolean isInterface = false;
        AccessSpecifier accessSpecifier = null;
        // 新增类型检查
        if (type instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) type;
            isInterface = coid.isInterface();
            accessSpecifier = coid.getAccessSpecifier();
        }

        String fullName = packageCache.entrySet().stream()
            .filter(e -> e.getValue() == packageId)
            .findFirst()
            .map(e -> e.getKey() + "." + className)
            .orElse(className);

        if (classCache.containsKey(fullName)) {
            return classCache.get(fullName);
        }

        String sql = "INSERT INTO classes (package_id, name, is_interface, access_modifier) " +
            "VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, packageId);
            stmt.setString(2, className);
            stmt.setBoolean(3, isInterface);
            stmt.setString(4, getAccessModifier(Optional.ofNullable(accessSpecifier)));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    classCache.put(fullName, id);
                    return id;
                }
            }
        }
        throw new SQLException("存储类失败: " + fullName);
    }

    private void processInheritance(int childId, ClassOrInterfaceType parentType) throws Exception {
        String parentName = resolveTypeName(parentType);
        int parentId = ensureClassExists(parentName);

        String sql = "INSERT INTO inheritance (child_class_id, parent_class_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, childId);
            stmt.setInt(2, parentId);
            stmt.executeUpdate();
        }
    }

    private void processImplementation(int classId, ClassOrInterfaceType interfaceType) throws Exception {
        String interfaceName = resolveTypeName(interfaceType);
        int interfaceId = ensureClassExists(interfaceName);

        String sql = "INSERT INTO interface_implementations (class_id, interface_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            stmt.setInt(2, interfaceId);
            stmt.executeUpdate();
        }
    }

    private int ensureClassExists(String fullName) throws Exception {
        if (classCache.containsKey(fullName)) {
            return classCache.get(fullName);
        }

        int lastDot = fullName.lastIndexOf('.');
        String packageName = lastDot > 0 ? fullName.substring(0, lastDot) : "";
        String className = lastDot > 0 ? fullName.substring(lastDot + 1) : fullName;

        int packageId = storePackage(packageName);

        String sql = "INSERT INTO classes (package_id, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE id=id";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, packageId);
            stmt.setString(2, className);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    classCache.put(fullName, id);
                    return id;
                }
            }

            // 查询现有ID
            try (PreparedStatement query = connection.prepareStatement(
                "SELECT id FROM classes WHERE package_id = ? AND name = ?")) {
                query.setInt(1, packageId);
                query.setString(2, className);
                try (ResultSet rs = query.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        classCache.put(fullName, id);
                        return id;
                    }
                }
            }
        }
        throw new SQLException("确保类存在失败: " + fullName);
    }

    private void processField(int classId, FieldDeclaration field) throws SQLException {
        String sql = "INSERT INTO fields (class_id, name, type, access_modifier) VALUES (?, ?, ?, ?)";

        for (VariableDeclarator var : field.getVariables()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, classId);
                stmt.setString(2, var.getNameAsString());
                stmt.setString(3, var.getTypeAsString());
                stmt.setString(4, getAccessModifier(Optional.ofNullable(field.getAccessSpecifier())));
                stmt.executeUpdate();
            }
        }
    }

    private void processMethod(int classId, MethodDeclaration method) throws SQLException {
        String sql = "INSERT INTO methods (class_id, name, return_type, is_static, is_abstract, is_final, access_modifier) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, classId);
            stmt.setString(2, method.getNameAsString());
            stmt.setString(3, method.getType().asString());
            stmt.setBoolean(4, method.isStatic());
            stmt.setBoolean(5, method.isAbstract());
            stmt.setBoolean(6, method.isFinal());
            stmt.setString(7, getAccessModifier(Optional.ofNullable(method.getAccessSpecifier())));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int methodId = rs.getInt(1);

                    // 处理泛型参数
                    method.getTypeParameters().forEach(tp -> {
                        try {
                            storeGenericTypeParameter(tp, methodId, "METHOD");
                        } catch (Exception e) {
                            throw new RuntimeException("Error storing method generic parameter", e);
                        }
                    });

                    // 处理参数
                    method.getParameters().forEach(param -> {
                        try {
                            storeMethodParameter(param, methodId);
                        } catch (Exception e) {
                            throw new RuntimeException("Error storing method parameter", e);
                        }
                    });
                }
            }
        }
    }

    private void storeMethodParameter(Parameter param, int methodId) throws SQLException {
        String sql = "INSERT INTO method_parameters (method_id, name, type) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, methodId);
            stmt.setString(2, param.getNameAsString());
            stmt.setString(3, param.getType().asString());
            stmt.executeUpdate();
        }

    }

    private void storeGenericTypeParameter(TypeParameter tp, int ownerId, String ownerType) throws SQLException {
        String sql = "INSERT INTO generic_type_parameters (owner_id, owner_type, name) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ownerId);
            stmt.setString(2, ownerType);
            stmt.setString(3, tp.getNameAsString());
            stmt.executeUpdate();
        }
    }

    private String resolveTypeName(ClassOrInterfaceType type) {
        return type.getNameWithScope();
    }

    private String getAccessModifier(Optional<AccessSpecifier> accessSpecifier) {
        return accessSpecifier.map(as -> {
            switch (as) {
                case PUBLIC: return "PUBLIC";
                case PROTECTED: return "PROTECTED";
                case PRIVATE: return "PRIVATE";
                default: return "DEFAULT";
            }
        }).orElse("DEFAULT");
    }

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/code_analysis";
        String username = "root";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            CodeAnalyzer analyzer = new CodeAnalyzer(conn);

            // 示例代码
            String code = "public class Test { private int field; public void method() {} }";
            analyzer.analyzeAndStore(code);
            System.out.println("解析存储成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}