package javaparser.mapper;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.resolution.*;
import com.github.javaparser.resolution.declarations.*;
import com.github.javaparser.resolution.types.*;
import com.github.javaparser.symbolsolver.*;
import com.github.javaparser.symbolsolver.resolution.typesolvers.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class ChainedMethodCallAnalyzer {

    public static class MethodCallDetail {
        String callExpression;
        String declaringClass;
        String methodName;
        String returnType;
        List<String> parameterTypes = new ArrayList<>();
        boolean isStatic;
        String chainPosition; // 表示在链式调用中的位置
        
        @Override
        public String toString() {
            return String.format("%s.%s(%s): %s [%s] at %s",
                    declaringClass,
                    methodName,
                    String.join(", ", parameterTypes),
                    returnType,
                    isStatic ? "static" : "instance",
                    chainPosition);
        }
    }

    public static List<MethodCallDetail> analyzeChainedCalls(String filePath, 
                                                           String className, 
                                                           String methodName,
                                                           List<String> classpath) {
        try {
            // 1. 配置类型解析器
            CombinedTypeSolver solver = new CombinedTypeSolver();
            solver.add(new ReflectionTypeSolver());
            solver.add(new JavaParserTypeSolver(new File("src/main/java")));
            
            // 添加额外的类路径
            for (String path : classpath) {
                if (path.endsWith(".jar")) {
                    solver.add(new JarTypeSolver(path));
                } else {
                    solver.add(new JavaParserTypeSolver(new File(path)));
                }
            }
            
            JavaSymbolSolver symbolSolver = new JavaSymbolSolver(solver);
            JavaParser parser = new JavaParser(new ParserConfiguration()
                    .setSymbolResolver(symbolSolver)
                    .setLanguageLevel(ParserConfiguration.LanguageLevel.BLEEDING_EDGE));
            
            // 2. 解析文件
            ParseResult<CompilationUnit> parseResult = parser.parse(new File(filePath));
            if (!parseResult.isSuccessful()) {
                System.err.println("解析失败: " + parseResult.getProblems());
                return Collections.emptyList();
            }

            CompilationUnit cu = parseResult.getResult().orElseThrow();
            
            // 3. 查找目标方法
            Optional<MethodDeclaration> targetMethod = cu.findAll(MethodDeclaration.class).stream()
                .filter(m -> m.getNameAsString().equals(methodName))
                //.filter(m -> {
                //    Optional<ClassOrInterfaceDeclaration> parent = m.getAncestorOfType(ClassOrInterfaceDeclaration.class);
                //    return parent.isPresent() && parent.get().getNameAsString().equals(className);
                //})
                .findFirst();
            
            if (!targetMethod.isPresent()) {
                System.err.println("未找到方法: " + className + "." + methodName);
                return Collections.emptyList();
            }
            
            // 4. 分析方法体中的链式调用
            return targetMethod.get().getBody()
                    .map(body -> analyzeMethodBody(body, symbolSolver))
                    .orElse(Collections.emptyList());
            
        } catch (Exception e) {
            System.err.println("分析失败: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static List<MethodCallDetail> analyzeMethodBody(BlockStmt body, JavaSymbolSolver solver) {
        List<MethodCallDetail> results = new ArrayList<>();
        
        // 查找所有方法调用表达式
        body.findAll(MethodCallExpr.class).forEach(methodCall -> {
            // 处理链式调用
            if (methodCall.getScope().isPresent() && 
                methodCall.getScope().get() instanceof MethodCallExpr) {
                
                // 递归解析链式调用
                List<MethodCallExpr> chain = flattenMethodChain(methodCall);
                
                // 解析链中的每个方法调用
                for (int i = 0; i < chain.size(); i++) {
                    MethodCallExpr callInChain = chain.get(i);
                    try {
                        MethodCallDetail detail = resolveMethodCall(callInChain, solver);
                        detail.chainPosition = String.format("%d of %d", i+1, chain.size());
                        results.add(detail);
                    } catch (Exception e) {
                        System.err.println("无法解析链式调用中的方法: " + callInChain + " - " + e.getMessage());
                    }
                }
            } else {
                // 普通方法调用
                try {
                    MethodCallDetail detail = resolveMethodCall(methodCall, solver);
                    detail.chainPosition = "single";
                    results.add(detail);
                } catch (Exception e) {
                    System.err.println("无法解析方法调用: " + methodCall + " - " + e.getMessage());
                }
            }
        });
        
        return results;
    }

    // 将链式调用展平为一个列表（从最外层到最内层）
    private static List<MethodCallExpr> flattenMethodChain(MethodCallExpr call) {
        List<MethodCallExpr> chain = new ArrayList<>();
        chain.add(call);
        
        while (call.getScope().isPresent() && 
               call.getScope().get() instanceof MethodCallExpr) {
            call = (MethodCallExpr) call.getScope().get();
            chain.add(0, call); // 添加到列表开头
        }
        
        return chain;
    }

    // 解析单个方法调用的详细信息
    private static MethodCallDetail resolveMethodCall(MethodCallExpr call, JavaSymbolSolver solver) 
            throws UnsolvedSymbolException, UnsolvedSymbolException {
        MethodCallDetail detail = new MethodCallDetail();
        detail.callExpression = call.toString();
        
        ResolvedMethodDeclaration resolved = call.resolve();
        
        // 方法声明类
        ResolvedReferenceTypeDeclaration declaringClass = 
            resolved.declaringType().asReferenceType();
        detail.declaringClass = declaringClass.getQualifiedName();
        
        // 方法基本信息
        detail.methodName = resolved.getName();
        detail.isStatic = resolved.isStatic();
        
        // 返回类型
        ResolvedType returnType = resolved.getReturnType();
        detail.returnType = returnType.describe();
        
        // 参数类型
        for (int i = 0; i < resolved.getNumberOfParams(); i++) {
            ResolvedType paramType = resolved.getParam(i).getType();
            detail.parameterTypes.add(paramType.describe());
        }
        
        return detail;
    }

    public static void main(String[] args) {
        // 示例用法
        String filePath = "src/main/java/com/example/ServiceClass.java";
        String className = "ServiceClass";
        String methodName = "processData";
        
        // 配置类路径
        List<String> classpath = Arrays.asList(
            "src/main/java",
            "lib/dependency1.jar"
        );
        
        List<MethodCallDetail> calls = analyzeChainedCalls(filePath, className, methodName, classpath);
        
        System.out.println("方法 " + className + "." + methodName + " 中的调用关系:");
        calls.forEach(call -> {
            System.out.println("  " + call.callExpression);
            System.out.println("    -> " + call);
        });
    }
}