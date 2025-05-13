package javaparser.mapper;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.resolution.*;
import com.github.javaparser.resolution.declarations.*;
import com.github.javaparser.resolution.types.*;
import com.github.javaparser.symbolsolver.*;
import com.github.javaparser.symbolsolver.resolution.typesolvers.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class EnhancedMethodAnalyzer {

    // 配置符号解析器（组合多种类型解析器）
    private static CombinedTypeSolver createTypeSolver() throws IOException {
        CombinedTypeSolver solver = new CombinedTypeSolver();
        solver.add(new ReflectionTypeSolver());
        //solver.add(new JavaParserTypeSolver(new File("src/main/java"))); // 项目源码目录
        //solver.add(new JarTypeSolver(new File("lib/some-dependency.jar"))); // 依赖库
        return solver;
    }

    public static class MethodAnalysisResult {
        public String methodSignature;
        public String returnType;
        public List<String> parameters = new ArrayList<>();
        public List<String> typeParameters = new ArrayList<>();
        public List<String> localVariables = new ArrayList<>();
        public List<String> methodCalls = new ArrayList<>();
        public List<String> exceptions = new ArrayList<>();
        public String javadoc = "";
        public List<String> controlStructures = new ArrayList<>();

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Method: ").append(methodSignature).append("\n");
            if (!javadoc.isEmpty()) sb.append("  Javadoc: ").append(javadoc).append("\n");
            sb.append("  Returns: ").append(returnType).append("\n");
            if (!typeParameters.isEmpty()) sb.append("  TypeParams: ").append(typeParameters).append("\n");
            sb.append("  Params: ").append(parameters).append("\n");
            if (!exceptions.isEmpty()) sb.append("  Throws: ").append(exceptions).append("\n");
            if (!localVariables.isEmpty()) sb.append("  Variables: ").append(localVariables).append("\n");
            if (!methodCalls.isEmpty()) sb.append("  Calls: ").append(methodCalls).append("\n");
            if (!controlStructures.isEmpty()) sb.append("  Control: ").append(controlStructures).append("\n");
            return sb.toString();
        }
    }

    // 带缓存的解析器实例（提升性能）
    private static class ParserHolder {
        private static final JavaParser INSTANCE;
        static {
            ParserConfiguration config = null;
            try {
                config = new ParserConfiguration()
                    .setSymbolResolver(new JavaSymbolSolver(createTypeSolver()))
                    .setLanguageLevel(ParserConfiguration.LanguageLevel.BLEEDING_EDGE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            INSTANCE = new JavaParser(config);
        }
    }

    public static Map<String, MethodAnalysisResult> analyzeClassMethods(String filePath, String className) {
        Map<String, MethodAnalysisResult> results = new ConcurrentHashMap<>();
        try {
            // 使用带缓存的解析器
            ParseResult<CompilationUnit> parseResult = ParserHolder.INSTANCE.parse(new File(filePath));
            
            if (!parseResult.isSuccessful()) {
                System.err.println("解析失败: " + parseResult.getProblems());
                return results;
            }

            CompilationUnit cu = parseResult.getResult().orElseThrow();
            
            // 并行处理类声明（提升大文件处理性能）
            cu.findAll(ClassOrInterfaceDeclaration.class).parallelStream()
                .filter(c -> c.getNameAsString().equals(className))
                .forEach(c -> analyzeClass(c, results));
                
        } catch (FileNotFoundException e) {
            System.err.println("文件未找到: " + filePath);
        } catch (Exception e) {
            System.err.println("解析错误: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }

    private static void analyzeClass(ClassOrInterfaceDeclaration classDecl, 
                                   Map<String, MethodAnalysisResult> results) {
        // 并行处理方法（提升性能）
        classDecl.getMethods().parallelStream().forEach(method -> {
            MethodAnalysisResult result = new MethodAnalysisResult();
            try {
                // 1. 处理方法签名
                analyzeMethodSignature(method, result);
                
                // 2. 处理泛型类型参数
                //analyzeTypeParameters(method, result);
                
                // 3. 处理参数和返回类型（包括复杂类型）
                analyzeParametersAndReturnType(method, result);
                
                // 4. 处理异常声明
                analyzeThrownExceptions(method, result);
                
                // 5. 处理Javadoc
                analyzeJavadoc(method, result);
                
                // 6. 分析方法体内容
                if (method.getBody().isPresent()) {
                    analyzeMethodBody(method.getBody().get(), result);
                }
                
                // 将结果存入Map
                results.put(result.methodSignature, result);
                
            } catch (UnsolvedSymbolException e) {
                System.err.println("无法解析符号: " + e.getName() + " 在方法: " + method.getNameAsString());
                result.methodSignature = method.getDeclarationAsString();
                results.put(result.methodSignature, result);
            } catch (Exception e) {
                System.err.println("分析方法出错: " + method.getNameAsString() + " - " + e.getMessage());
            }
        });
    }

    private static void analyzeMethodSignature(MethodDeclaration method, 
                                             MethodAnalysisResult result) {
        result.methodSignature = method.getDeclarationAsString();
    }

    //private static void analyzeTypeParameters(MethodDeclaration method,
    //                                        MethodAnalysisResult result) {
    //    method.getTypeParameters().forEach(tp -> {
    //        String typeParam = tp.getNameAsString();
    //        if (tp.getTypeBound().isNonEmpty()) {
    //            typeParam += " extends " + tp.getTypeBound().stream()
    //                .map(TypeParameter::getTypeBound)
    //                .map(Object::toString)
    //                .collect(Collectors.joining(" & "));
    //        }
    //        result.typeParameters.add(typeParam);
    //    });
    //}

    private static void analyzeParametersAndReturnType(MethodDeclaration method, 
                                                      MethodAnalysisResult result) {
        // 处理参数（包括泛型参数）
        for (Parameter param : method.getParameters()) {
            try {
                ResolvedType resolvedType = param.getType().resolve();
                result.parameters.add(resolvedType.describe() + " " + param.getNameAsString());
            } catch (Exception e) {
                // 回退到简单类型表示
                result.parameters.add(param.getType().asString() + " " + param.getNameAsString());
            }
        }
        
        // 处理返回类型（包括泛型返回类型）
        try {
            ResolvedType resolvedReturnType = method.getType().resolve();
            result.returnType = resolvedReturnType.describe();
        } catch (Exception e) {
            result.returnType = method.getType().asString();
        }
    }

    private static void analyzeThrownExceptions(MethodDeclaration method, 
                                              MethodAnalysisResult result) {
        method.getThrownExceptions().forEach(exception -> {
            try {
                ResolvedType resolvedType = exception.resolve();
                result.exceptions.add(resolvedType.describe());
            } catch (Exception e) {
                result.exceptions.add(exception.asString());
            }
        });
    }

    private static void analyzeJavadoc(MethodDeclaration method, 
                                      MethodAnalysisResult result) {
        method.getJavadoc().ifPresent(javadoc -> {
            result.javadoc = javadoc.getDescription().toText()
                .replace("\n", " ")
                .trim();
        });
    }

    private static void analyzeMethodBody(BlockStmt body,
                                          MethodAnalysisResult result) {
        // 1. 局部变量分析
        body.findAll(VariableDeclarator.class).forEach(v -> {
            try {
                ResolvedType resolvedType = v.getType().resolve();
                result.localVariables.add(resolvedType.describe() + " " + v.getNameAsString());
            } catch (Exception e) {
                result.localVariables.add(v.getType().asString() + " " + v.getNameAsString());
            }
        });
        
        // 2. 方法调用分析（处理静态方法、实例方法、泛型方法等）
        body.findAll(MethodCallExpr.class).forEach(m -> {
            try {
                ResolvedMethodDeclaration resolved = m.resolve();
                String callInfo = resolved.getQualifiedSignature();
                result.methodCalls.add(callInfo);
            } catch (Exception e) {
                // 无法解析的方法调用
                String callInfo = "UNRESOLVED: " + m.getNameAsString();
                if (m.getScope().isPresent()) {
                    callInfo += " on " + m.getScope().get().toString();
                }
                result.methodCalls.add(callInfo);
            }
        });
        
        // 3. 控制流分析
        body.findAll(IfStmt.class).forEach(i -> result.controlStructures.add("if"));
        body.findAll(ForStmt.class).forEach(f -> result.controlStructures.add("for"));
        body.findAll(WhileStmt.class).forEach(w -> result.controlStructures.add("while"));
        body.findAll(SwitchStmt.class).forEach(s -> result.controlStructures.add("switch"));
        body.findAll(TryStmt.class).forEach(t -> result.controlStructures.add("try"));
    }

    public static void main(String[] args) {
        String filePath = "D:\\work\\myself\\code\\Java\\src\\main\\java\\test\\ComprehensiveClass.java";
        String className = "ComprehensiveClass";
        
        long startTime = System.currentTimeMillis();
        Map<String, MethodAnalysisResult> analysis = analyzeClassMethods(filePath, className);
        long duration = System.currentTimeMillis() - startTime;
        
        System.out.println("分析完成，耗时: " + duration + "ms");
        System.out.println("找到 " + analysis.size() + " 个方法:");
        
        analysis.values().forEach(System.out::println);
    }
}