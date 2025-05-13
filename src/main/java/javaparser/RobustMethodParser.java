//package javaparser;
//
//import com.github.javaparser.JavaParser;
//import com.github.javaparser.ParseResult;
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.body.MethodDeclaration;
//import com.github.javaparser.ast.body.Parameter;
//import com.github.javaparser.ast.body.VariableDeclarator;
//import com.github.javaparser.ast.expr.*;
//import com.github.javaparser.ast.stmt.BlockStmt;
//import com.github.javaparser.ast.stmt.ForStmt;
//import com.github.javaparser.ast.stmt.ForeachStmt;
//import com.github.javaparser.ast.type.Type;
//import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
//import com.github.javaparser.resolution.TypeSolver;
//import com.github.javaparser.symbolsolver.JavaSymbolSolver;
//import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
///**
// * 增强型Java方法解析工具
// * 改进点：
// * 1. 增加类型解析的退避策略
// * 2. 更健壮的类型解析处理
// * 3. 详细的错误日志记录
// */
//public class RobustMethodParser {
//    private final TypeSolver typeSolver;
//
//    public RobustMethodParser() {
//        this.typeSolver = new ReflectionTypeSolver();
//        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
//        JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
//    }
//
//    // ... 保留之前定义的MethodInfo, VariableInfo, MethodCallInfo类 ...
//
//    /**
//     * 安全解析类型名称（带退避策略）
//     */
//    private String safeResolveType(Type type) {
//        try {
//            // 尝试完整解析（带泛型信息）
//            return type.resolve().describe();
//        } catch (Exception e) {
//            try {
//                // 退避策略1：尝试不解析泛型参数的基础类型
//                if (type.isClassOrInterfaceType()) {
//                    return type.asClassOrInterfaceType().getNameAsString();
//                }
//                // 退避策略2：使用toString()但移除包名（简化显示）
//                return type.toString().replaceAll("^[a-z0-9_.]+\\.", "");
//            } catch (Exception e2) {
//                // 最终退避策略：返回原始类型字符串
//                return type.toString();
//            }
//        }
//    }
//
//    /**
//     * 安全解析表达式类型
//     */
//    private String safeResolveExpressionType(Expression expr) {
//        try {
//            return expr.calculateResolvedType().describe();
//        } catch (Exception e) {
//            // 表达式类型解析退避策略
//            if (expr instanceof NameExpr) {
//                return ((NameExpr) expr).getNameAsString();
//            }
//            if (expr instanceof FieldAccessExpr) {
//                return ((FieldAccessExpr) expr).getNameAsString();
//            }
//            return expr.toString();
//        }
//    }
//
//    private class MethodVisitor extends VoidVisitorAdapter<Void> {
//        private final List<MethodInfo> methods = new ArrayList<>();
//
//        @Override
//        public void visit(MethodDeclaration methodDecl, Void arg) {
//            MethodInfo methodInfo = new MethodInfo();
//            methodInfo.setMethodName(methodDecl.getNameAsString());
//
//            // 解析方法参数（带退避策略）
//            parseParameters(methodDecl, methodInfo);
//
//            // 解析方法体
//            methodDecl.getBody().ifPresent(body -> {
//                parseLocalVariables(body, methodInfo);
//                parseMethodCalls(body, methodInfo);
//            });
//
//            methods.add(methodInfo);
//            super.visit(methodDecl, arg);
//        }
//
//        private void parseParameters(MethodDeclaration methodDecl, MethodInfo methodInfo) {
//            for (Parameter param : methodDecl.getParameters()) {
//                VariableInfo paramInfo = new VariableInfo();
//                paramInfo.setName(param.getNameAsString());
//
//                // 使用安全类型解析
//                paramInfo.setType(safeResolveType(param.getType()));
//                paramInfo.setScope("parameter");
//                methodInfo.getParameters().add(paramInfo);
//            }
//        }
//
//        private void parseLocalVariables(BlockStmt body, MethodInfo methodInfo) {
//            // 处理普通变量声明
//            body.findAll(VariableDeclarationExpr.class).forEach(expr -> {
//                for (VariableDeclarator var : expr.getVariables()) {
//                    VariableInfo varInfo = new VariableInfo();
//                    varInfo.setName(var.getNameAsString());
//                    varInfo.setType(safeResolveType(var.getType()));
//                    varInfo.setInitializer(var.getInitializer()
//                            .map(init -> safeResolveInitializer(init))
//                            .orElse(""));
//                    varInfo.setScope("local");
//                    methodInfo.getLocalVariables().add(varInfo);
//                }
//            });
//
//            // 处理foreach循环变量
//            body.findAll(ForeachStmt.class).forEach(foreach -> {
//                VariableInfo varInfo = new VariableInfo();
//                varInfo.setName(foreach.getVariable().getNameAsString());
//                varInfo.setType(safeResolveType(foreach.getVariable().getType()));
//                varInfo.setScope("foreach");
//                methodInfo.getLocalVariables().add(varInfo);
//            });
//
//            // 处理for循环变量
//            body.findAll(ForStmt.class).forEach(forStmt -> {
//                forStmt.getInitialization().forEach(init -> {
//                    if (init instanceof VariableDeclarationExpr) {
//                        VariableDeclarationExpr initExpr = (VariableDeclarationExpr) init;
//                        for (VariableDeclarator var : initExpr.getVariables()) {
//                            VariableInfo varInfo = new VariableInfo();
//                            varInfo.setName(var.getNameAsString());
//                            varInfo.setType(safeResolveType(var.getType()));
//                            varInfo.setInitializer(var.getInitializer()
//                                    .map(i -> safeResolveInitializer(i))
//                                    .orElse(""));
//                            varInfo.setScope("for-loop");
//                            methodInfo.getLocalVariables().add(varInfo);
//                        }
//                    }
//                });
//            });
//        }
//
//        /**
//         * 安全解析初始化表达式
//         */
//        private String safeResolveInitializer(Expression init) {
//            try {
//                // 对于简单字面量直接返回
//                if (init instanceof LiteralExpr || init instanceof NameExpr) {
//                    return init.toString();
//                }
//
//                // 对于方法调用保留关键信息
//                if (init instanceof MethodCallExpr) {
//                    MethodCallExpr call = (MethodCallExpr) init;
//                    return call.getScope()
//                            .map(s -> safeResolveExpressionType(s) + ".")
//                            .orElse("") + call.getNameAsString() + "()";
//                }
//
//                // 默认情况截断长表达式
//                String str = init.toString();
//                return str.length() > 50 ? str.substring(0, 47) + "..." : str;
//            } catch (Exception e) {
//                return "<无法解析的表达式>";
//            }
//        }
//
//        private void parseMethodCalls(BlockStmt body, MethodInfo methodInfo) {
//            body.findAll(MethodCallExpr.class).forEach(call -> {
//                MethodCallInfo callInfo = new MethodCallInfo();
//                callInfo.setMethodName(call.getNameAsString());
//
//                try {
//                    // 解析调用者类型（带退避策略）
//                    call.getScope().ifPresent(scope -> {
//                        callInfo.setCallerType(safeResolveExpressionType(scope));
//                        if (scope instanceof MethodCallExpr) {
//                            callInfo.setChainCall(true);
//                        }
//                    });
//
//                    // 解析参数类型
//                    call.getArguments().forEach(arg -> {
//                        callInfo.getArgumentTypes().add(safeResolveExpressionType(arg));
//                    });
//                } catch (Exception e) {
//                    // 记录解析失败但仍保留基本信息
//                    callInfo.setCallerType("<无法解析的类型>");
//                }
//
//                methodInfo.getMethodCalls().add(callInfo);
//            });
//        }
//    }
//
//    /**
//     * 解析Java文件中的所有方法（带异常处理）
//     */
//    public List<MethodInfo> parseMethods(Path javaFilePath) throws Exception {
//        try {
//            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(javaFilePath);
//            if (!parseResult.isSuccessful() || !parseResult.getResult().isPresent()) {
//                throw new IllegalArgumentException("解析Java文件失败: " +
//                    parseResult.getProblems().toString());
//            }
//
//            CompilationUnit cu = parseResult.getResult().get();
//            MethodVisitor visitor = new MethodVisitor();
//            cu.accept(visitor, null);
//            return visitor.getMethods();
//        } catch (Exception e) {
//            // 生产环境应记录详细日志
//            System.err.println("解析文件失败: " + javaFilePath + ", 原因: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    public static void main(String[] args) {
//        RobustMethodParser parser = new RobustMethodParser();
//
//        try {
//            List<MethodInfo> methods = parser.parseMethods(Paths.get("MyClass.java"));
//
//            methods.forEach(method -> {
//                System.out.println("\n方法: " + method.getMethodName());
//
//                System.out.println("参数:");
//                method.getParameters().forEach(p ->
//                    System.out.printf("  %s: %s%n", p.getName(), p.getType()));
//
//                System.out.println("局部变量:");
//                method.getLocalVariables().forEach(v ->
//                    System.out.printf("  %s: %s (init: %s)%n",
//                        v.getName(), v.getType(), v.getInitializer()));
//
//                System.out.println("方法调用:");
//                method.getMethodCalls().forEach(c ->
//                    System.out.printf("  %s.%s(%s)%s%n",
//                        c.getCallerType(), c.getMethodName(),
//                        String.join(", ", c.getArgumentTypes()),
//                        c.isChainCall() ? " [链式]" : ""));
//            });
//        } catch (Exception e) {
//            System.err.println("解析失败: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}