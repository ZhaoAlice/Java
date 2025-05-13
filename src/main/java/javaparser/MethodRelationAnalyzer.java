package javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodRelationAnalyzer {

    public static class MethodInfo {
        private String methodName;
        private List<String> parameters = new ArrayList<>();
        private List<String> localVariables = new ArrayList<>();
        private List<String> methodCalls = new ArrayList<>();
        private Map<String, List<String>> chainCalls = new HashMap<>();

        // getters and setters
        public String getMethodName() {
            return methodName;
        }

        public List<String> getParameters() {
            return parameters;
        }

        public List<String> getLocalVariables() {
            return localVariables;
        }

        public List<String> getMethodCalls() {
            return methodCalls;
        }

        public Map<String, List<String>> getChainCalls() {
            return chainCalls;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("方法: ").append(methodName).append("\n");
            sb.append("参数: ").append(String.join(", ", parameters)).append("\n");
            sb.append("局部变量: ").append(String.join(", ", localVariables)).append("\n");
            sb.append("方法调用: ").append(String.join(", ", methodCalls)).append("\n");
            sb.append("链式调用:\n");
            chainCalls.forEach((caller, calls) -> 
                sb.append("  ").append(caller).append(" -> ")
                 .append(String.join(" -> ", calls)).append("\n"));
            return sb.toString();
        }
    }

    public static Map<String, MethodInfo> analyzeClass(File javaFile) throws Exception {
        JavaParser parser = new JavaParser();
        CompilationUnit cu = parser.parse(javaFile).getResult().orElseThrow();
        
        Map<String, MethodInfo> methodMap = new HashMap<>();
        
        // 分析方法声明
        cu.accept(new MethodVisitor(), methodMap);
        
        // 分析方法调用关系
        cu.accept(new MethodCallVisitor(), methodMap);
        
        return methodMap;
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Map<String, MethodInfo>> {
        @Override
        public void visit(MethodDeclaration method, Map<String, MethodInfo> methodMap) {
            MethodInfo info = new MethodInfo();
            info.methodName = method.getNameAsString();
            
            // 处理参数
            for (Parameter param : method.getParameters()) {
                info.parameters.add(param.getNameAsString() + ": " + param.getType());
            }
            
            // 处理局部变量
            method.findAll(VariableDeclarator.class).forEach(var -> {
                info.localVariables.add(var.getNameAsString() + ": " + var.getType());
            });
            
            methodMap.put(info.methodName, info);
            super.visit(method, methodMap);
        }
    }

    private static class MethodCallVisitor extends VoidVisitorAdapter<Map<String, MethodInfo>> {
        @Override
        public void visit(MethodCallExpr call, Map<String, MethodInfo> methodMap) {
            // 获取当前方法上下文
            MethodDeclaration enclosingMethod = call.findAncestor(MethodDeclaration.class)
                .orElseThrow(() -> new IllegalStateException("方法调用不在方法内部"));
            
            MethodInfo currentMethod = methodMap.get(enclosingMethod.getNameAsString());
            
            // 记录简单方法调用
            String calledMethod = call.getNameAsString();
            currentMethod.methodCalls.add(calledMethod);
            
            // 处理链式调用
            if (call.getScope().isPresent() && call.getScope().get() instanceof MethodCallExpr) {
                MethodCallExpr previousCall = (MethodCallExpr) call.getScope().get();
                String previousMethod = previousCall.getNameAsString();
                
                List<String> chain = currentMethod.chainCalls
                    .computeIfAbsent(previousMethod, k -> new ArrayList<>());
                chain.add(calledMethod);
            }
            
            super.visit(call, methodMap);
        }
    }

    public static void main(String[] args) throws Exception {
        File javaFile = new File("src/main/java/com/example/ExampleClass.java");
        Map<String, MethodInfo> methods = analyzeClass(javaFile);
        
        System.out.println("方法分析结果:");
        methods.values().forEach(System.out::println);
    }
}