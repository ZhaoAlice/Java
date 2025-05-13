package javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.Type;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MethodParameterParser {

    public static class MethodParameterInfo {
        private String methodName;
        private List<String> parameterTypes = new ArrayList<>();
        private int parameterCount;
        private String returnType;

        // getters
        public String getMethodName() {
            return methodName;
        }

        public List<String> getParameterTypes() {
            return parameterTypes;
        }

        public int getParameterCount() {
            return parameterCount;
        }

        public String getReturnType() {
            return returnType;
        }

        @Override
        public String toString() {
            return String.format("%s %s(%s)", 
                    returnType, 
                    methodName, 
                    String.join(", ", parameterTypes));
        }
    }

    public static List<MethodParameterInfo> parseMethods(File javaFile) throws Exception {
        JavaParser parser = new JavaParser();
        CompilationUnit cu = parser.parse(javaFile).getResult().orElseThrow();
        List<MethodParameterInfo> methodInfos = new ArrayList<>();

        cu.findAll(MethodDeclaration.class).forEach(method -> {
            MethodParameterInfo info = new MethodParameterInfo();
            info.methodName = method.getNameAsString();
            info.returnType = method.getType().toString();
            
            for (Parameter param : method.getParameters()) {
                String typeName = simplifyTypeName(param.getType().toString());
                info.parameterTypes.add(typeName);
            }
            
            info.parameterCount = method.getParameters().size();
            methodInfos.add(info);
        });

        return methodInfos;
    }

    private static String simplifyTypeName(String fullTypeName) {
        // 去除泛型部分
        int genericStart = fullTypeName.indexOf('<');
        if (genericStart > 0) {
            fullTypeName = fullTypeName.substring(0, genericStart);
        }
        
        // 去除数组部分
        int arrayStart = fullTypeName.indexOf('[');
        if (arrayStart > 0) {
            fullTypeName = fullTypeName.substring(0, arrayStart);
        }
        
        // 去除包名（取最后一部分）
        int lastDot = fullTypeName.lastIndexOf('.');
        if (lastDot > 0) {
            fullTypeName = fullTypeName.substring(lastDot + 1);
        }
        
        return fullTypeName.trim();
    }

    public static void main(String[] args) throws Exception {
        File javaFile = new File("D:\\work\\myself\\code\\Java\\src\\main\\java\\javaparser\\ConsumeMessageOrderlyService.java");
        List<MethodParameterInfo> methods = parseMethods(javaFile);
        
        System.out.println("Methods in " + javaFile.getName() + ":");
        methods.forEach(System.out::println);
    }



}