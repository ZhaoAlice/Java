package javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FieldDeclarationParser {

    public static class FieldInfo {
        private String fieldName;
        private String simplifiedType; // 格式：包名.类名
        private boolean isPrimitive;
        private String fullDeclaration;

        // getters and setters
        public String getFieldName() {
            return fieldName;
        }

        public String getSimplifiedType() {
            return simplifiedType;
        }

        public boolean isPrimitive() {
            return isPrimitive;
        }

        public String getFullDeclaration() {
            return fullDeclaration;
        }

        @Override
        public String toString() {
            return String.format("%s: %s (%s)", fieldName, simplifiedType, isPrimitive ? "primitive" : "reference");
        }
    }

    public static List<FieldInfo> parseFields(File javaFile) throws Exception {
        // 配置类型解析器
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        JavaParser parser = new JavaParser(new ParserConfiguration().setSymbolResolver(symbolSolver));

        CompilationUnit cu = parser.parse(javaFile).getResult().orElseThrow();
        List<FieldInfo> fieldInfos = new ArrayList<>();

        cu.findAll(FieldDeclaration.class).forEach(field -> {
            Type type = field.getElementType();
            String simplifiedTypeName = getSimplifiedTypeName(type);
            boolean isPrimitive = isPrimitiveType(type);

            for (VariableDeclarator var : field.getVariables()) {
                FieldInfo info = new FieldInfo();
                info.fieldName = var.getNameAsString();
                info.simplifiedType = simplifiedTypeName;
                info.isPrimitive = isPrimitive;
                info.fullDeclaration = field.toString().trim();
                fieldInfos.add(info);
            }
        });

        return fieldInfos;
    }

    private static String getSimplifiedTypeName(Type type) {
        try {
            // 尝试解析为引用类型
            Optional<ResolvedReferenceTypeDeclaration> resolved = type.resolve().asReferenceType().getTypeDeclaration();
            if (resolved.isPresent()) {
                return resolved.get().getPackageName() + "." + resolved.get().getClassName();
            }
            return type.toString();
        } catch (UnsolvedSymbolException | UnsupportedOperationException e) {
            // 如果解析失败，返回原始类型名称
            return extractBasicTypeName(type.toString());
        }
    }

    private static String extractBasicTypeName(String rawType) {
        // 去除泛型部分 <...>
        int genericStart = rawType.indexOf('<');
        if (genericStart > 0) {
            rawType = rawType.substring(0, genericStart);
        }

        // 去除数组部分 []
        int arrayStart = rawType.indexOf('[');
        if (arrayStart > 0) {
            rawType = rawType.substring(0, arrayStart);
        }

        //// 去除包名（最后一个点之后的部分）
        //int lastDot = rawType.lastIndexOf('.');
        //if (lastDot > 0) {
        //    rawType = rawType.substring(lastDot + 1);
        //}

        return rawType.trim();
    }

    private static boolean isPrimitiveType(Type type) {
        String typeName = type.toString();
        return typeName.equals("byte") || typeName.equals("short") || typeName.equals("int") ||
               typeName.equals("long") || typeName.equals("float") || typeName.equals("double") ||
               typeName.equals("char") || typeName.equals("boolean") || typeName.equals("void");
    }

    public static void main(String[] args) throws Exception {
        File javaFile = new File("D:\\work\\myself\\code\\Java\\src\\main\\java\\javaparser\\MyClass.java");
        List<FieldInfo> fields = parseFields(javaFile);
        
        System.out.println("Fields in " + javaFile.getName() + ":");
        fields.forEach(System.out::println);
    }

}