package javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 生产级Java方法解析工具
 * 功能：
 * 1. 解析方法参数（类型、名称）
 * 2. 解析局部变量（类型、名称、初始化表达式）
 * 3. 支持链式方法调用分析
 * 4. 支持泛型类型解析
 */
public class MethodParser {

    private final TypeSolver typeSolver;

    public MethodParser() {
        // 初始化类型解析器（仅使用JRE内置类型解析）
        this.typeSolver = new ReflectionTypeSolver();
    }

    /**
     * 解析Java文件中的所有方法
     */
    public List<MethodInfo> parseMethods(Path javaFilePath) throws Exception {
        // 配置符号解析器
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        ParserConfiguration parserConfiguration = new ParserConfiguration().setSymbolResolver(symbolSolver);

        // 解析Java文件
        ParseResult<CompilationUnit> parseResult = new JavaParser(parserConfiguration).parse(javaFilePath);
        if (!parseResult.isSuccessful()) {
            throw new IllegalArgumentException("解析Java文件失败: " + javaFilePath);
        }

        CompilationUnit cu = parseResult.getResult().orElseThrow();
        MethodVisitor visitor = new MethodVisitor();
        cu.accept(visitor, null);
        return visitor.getMethods();
    }

    /**
     * 方法信息容器
     */
    public static class MethodInfo {
        private String methodName;
        private List<VariableInfo> parameters = new ArrayList<>();
        private List<VariableInfo> localVariables = new ArrayList<>();
        private List<MethodCallInfo> methodCalls = new ArrayList<>();

        // getters and setters
        public String getMethodName() { return methodName; }
        public void setMethodName(String methodName) { this.methodName = methodName; }
        public List<VariableInfo> getParameters() { return parameters; }
        public List<VariableInfo> getLocalVariables() { return localVariables; }
        public List<MethodCallInfo> getMethodCalls() { return methodCalls; }
    }

    /**
     * 变量信息（参数/局部变量）
     */
    public static class VariableInfo {
        private String name;
        private String type;
        private String initializer;
        private String scope;

        // getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getInitializer() { return initializer; }
        public void setInitializer(String initializer) { this.initializer = initializer; }
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
    }

    /**
     * 方法调用信息
     */
    public static class MethodCallInfo {
        private String methodName;
        private String callerType;
        private boolean isChainCall;
        private List<String> argumentTypes = new ArrayList<>();

        // getters and setters
        public String getMethodName() { return methodName; }
        public void setMethodName(String methodName) { this.methodName = methodName; }
        public String getCallerType() { return callerType; }
        public void setCallerType(String callerType) { this.callerType = callerType; }
        public boolean isChainCall() { return isChainCall; }
        public void setChainCall(boolean chainCall) { isChainCall = chainCall; }
        public List<String> getArgumentTypes() { return argumentTypes; }
    }

    private String safeResolveType(Type type) {
        try {
            // 尝试完整解析（带泛型信息）
            return type.resolve().describe();
        } catch (Exception e) {
            try {
                // 退避策略1：尝试不解析泛型参数的基础类型
                if (type.isClassOrInterfaceType()) {
                    return type.asClassOrInterfaceType().getNameAsString();
                }
                // 退避策略2：使用toString()但移除包名（简化显示）
                return type.toString().replaceAll("^[a-z0-9_.]+\\.", "");
            } catch (Exception e2) {
                // 最终退避策略：返回原始类型字符串
                return type.toString();
            }
        }
    }

    /**
     * 安全解析表达式类型
     */
    private String safeResolveExpressionType(Expression expr) {
        try {
            return expr.calculateResolvedType().describe();
        } catch (Exception e) {
            // 表达式类型解析退避策略
            if (expr instanceof NameExpr) {
                return ((NameExpr) expr).getNameAsString();
            }
            if (expr instanceof FieldAccessExpr) {
                return ((FieldAccessExpr) expr).getNameAsString();
            }
            return expr.toString();
        }
    }

    /**
     * 方法访问器
     */
    private class MethodVisitor extends VoidVisitorAdapter<Void> {
        private final List<MethodInfo> methods = new ArrayList<>();

        public List<MethodInfo> getMethods() {
            return methods;
        }

        @Override
        public void visit(MethodDeclaration methodDecl, Void arg) {
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setMethodName(methodDecl.getNameAsString());

            // 解析方法参数
            parseParameters(methodDecl, methodInfo);

            // 解析方法体中的局部变量和方法调用
            methodDecl.getBody().ifPresent(body -> {
                parseLocalVariables(body, methodInfo);
                parseMethodCalls(body, methodInfo);
            });

            methods.add(methodInfo);
            super.visit(methodDecl, arg);
        }

        private void parseParameters(MethodDeclaration methodDecl, MethodInfo methodInfo) {
            for (Parameter param : methodDecl.getParameters()) {
                VariableInfo paramInfo = new VariableInfo();
                paramInfo.setName(param.getNameAsString());
                paramInfo.setType(safeResolveType(param.getType()));
                paramInfo.setScope("parameter");
                methodInfo.getParameters().add(paramInfo);
            }
        }

        private void parseLocalVariables(BlockStmt body, MethodInfo methodInfo) {
            body.findAll(VariableDeclarationExpr.class).forEach(expr -> {
                for (VariableDeclarator var : expr.getVariables()) {
                    VariableInfo varInfo = new VariableInfo();
                    varInfo.setName(var.getNameAsString());
                    varInfo.setType(safeResolveType(var.getType()));
                    varInfo.setInitializer(var.getInitializer()
                            .map(Object::toString)
                            .orElse(""));
                    varInfo.setScope("local");
                    methodInfo.getLocalVariables().add(varInfo);
                }
            });

            //// 处理foreach循环变量
            //body.findAll(ForEachStmt.class).forEach(foreach -> {
            //    VariableInfo varInfo = new VariableInfo();
            //    varInfo.setName(foreach.getVariable().getNameAsString());
            //    varInfo.setType(foreach.getVariable().getType().resolve().describe());
            //    varInfo.setScope("foreach");
            //    methodInfo.getLocalVariables().add(varInfo);
            //});

            // 处理for循环变量
            body.findAll(ForStmt.class).forEach(forStmt -> {
                forStmt.getInitialization().forEach(init -> {
                    if (init instanceof VariableDeclarationExpr) {
                        VariableDeclarationExpr initExpr = (VariableDeclarationExpr) init;
                        for (VariableDeclarator var : initExpr.getVariables()) {
                            VariableInfo varInfo = new VariableInfo();
                            varInfo.setName(var.getNameAsString());
                            varInfo.setType(safeResolveType(var.getType()));
                            varInfo.setInitializer(var.getInitializer()
                                    .map(Object::toString)
                                    .orElse(""));
                            varInfo.setScope("for-loop");
                            methodInfo.getLocalVariables().add(varInfo);
                        }
                    }
                });
            });
        }

        private void parseMethodCalls(BlockStmt body, MethodInfo methodInfo) {
            body.findAll(MethodCallExpr.class).forEach(call -> {
                MethodCallInfo callInfo = new MethodCallInfo();
                
                try {
                    // 解析方法名
                    callInfo.setMethodName(call.getNameAsString());
                    
                    // 解析调用者类型
                    Optional<Expression> scope = call.getScope();
                    if (scope.isPresent()) {
                        callInfo.setCallerType(safeResolveExpressionType(scope.get()));
                        
                        // 检测链式调用
                        if (scope.get() instanceof MethodCallExpr) {
                            callInfo.setChainCall(true);
                        }
                    }
                    
                    // 解析参数类型
                    call.getArguments().forEach(arg -> {
                        callInfo.getArgumentTypes().add(safeResolveExpressionType(arg));
                    });
                    
                    methodInfo.getMethodCalls().add(callInfo);
                } catch (Exception e) {
                    // 生产环境应记录解析失败的日志
                    System.err.println("方法调用解析失败: " + call + ", 原因: " + e.getMessage());
                }
            });
        }
    }


    public static void main(String[] args) throws Exception {
        // 1. 初始化解析器
        MethodParser parser = new MethodParser();

        // 2. 指定要解析的Java文件
        Path javaFile = Paths.get("D:\\work\\myself\\code\\ConsumeMessageOrderlyService.java");

        // 3. 执行解析
        List<MethodParser.MethodInfo> methods = parser.parseMethods(javaFile);

        // 4. 打印解析结果
        for (MethodParser.MethodInfo method : methods) {
            System.out.println("\n方法: " + method.getMethodName());

            // 打印参数
            System.out.println("参数:");
            method.getParameters().forEach(param -> {
                System.out.printf("  %s: %s%n", param.getName(), param.getType());
            });

            // 打印局部变量
            System.out.println("局部变量:");
            method.getLocalVariables().forEach(var -> {
                System.out.printf("  %s: %s (scope: %s, init: %s)%n",
                    var.getName(), var.getType(), var.getScope(),
                    var.getInitializer().length() > 50 ?
                        var.getInitializer().substring(0, 47) + "..." :
                        var.getInitializer());
            });

            // 打印方法调用
            System.out.println("方法调用:");
            method.getMethodCalls().forEach(call -> {
                System.out.printf("  %s.%s(%s)%s%n",
                    call.getCallerType(), call.getMethodName(),
                    String.join(", ", call.getArgumentTypes()),
                    call.isChainCall() ? " [链式调用]" : "");
            });
        }
    }
}