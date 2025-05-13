//package javaparser;
//
//import com.github.javaparser.JavaParser;
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.body.MethodDeclaration;
//import com.github.javaparser.ast.body.VariableDeclarator;
//import com.github.javaparser.ast.expr.VariableDeclarationExpr;
//import com.github.javaparser.ast.stmt.ForStmt;
//import com.github.javaparser.ast.stmt.ForEachStmt;
//import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
//
//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//public class LocalVariableAnalyzer {
//
//    public static void main(String[] args) throws Exception {
//        // 解析Java源文件
//        FileInputStream in = new FileInputStream("src/main/java/com/example/TestClass.java");
//        CompilationUnit cu = JavaParser.parse(in);
//
//        // 创建访问者并开始分析
//        LocalVariableVisitor visitor = new LocalVariableVisitor();
//        cu.accept(visitor, null);
//
//        // 打印结果
//        visitor.printResults();
//    }
//
//    private static class LocalVariableVisitor extends VoidVisitorAdapter<Void> {
//        private final List<VariableInfo> variables = new ArrayList<>();
//
//        @Override
//        public void visit(MethodDeclaration n, Void arg) {
//            // 获取方法名
//            String methodName = n.getNameAsString();
//            System.out.println("\n方法: " + methodName);
//
//            // 访问方法体内容
//            super.visit(n, arg);
//        }
//
//        @Override
//        public void visit(VariableDeclarationExpr n, Void arg) {
//            // 处理普通变量声明
//            for (VariableDeclarator var : n.getVariables()) {
//                String varName = var.getNameAsString();
//                String varType = var.getType().resolve().describe();
//                String initExpr = var.getInitializer().map(Object::toString).orElse("无初始化");
//
//                variables.add(new VariableInfo(varName, varType, initExpr, "方法级"));
//            }
//            super.visit(n, arg);
//        }
//
//        @Override
//        public void visit(ForEachStmt n, Void arg) {
//            // 处理foreach循环变量
//            String varName = n.getVariable().getNameAsString();
//            String varType = n.getVariable().getType().resolve().describe();
//
//            variables.add(new VariableInfo(varName, varType, "foreach循环", "循环级"));
//            super.visit(n, arg);
//        }
//
//        @Override
//        public void visit(ForStmt n, Void arg) {
//            // 处理for循环初始化变量
//            n.getInitialization().forEach(expr -> {
//                if (expr instanceof VariableDeclarationExpr) {
//                    ((VariableDeclarationExpr) expr).getVariables().forEach(var -> {
//                        String varName = var.getNameAsString();
//                        String varType = var.getType().resolve().describe();
//                        String initExpr = var.getInitializer().map(Object::toString).orElse("无初始化");
//
//                        variables.add(new VariableInfo(varName, varType, initExpr, "循环级"));
//                    });
//                }
//            });
//            super.visit(n, arg);
//        }
//
//        public void printResults() {
//            System.out.println("\n局部变量分析结果:");
//            System.out.println("+----------------------+--------------------------------+---------------------+------------+");
//            System.out.println("| 变量名              | 类型全限定名                  | 初始化表达式        | 作用域     |");
//            System.out.println("+----------------------+--------------------------------+---------------------+------------+");
//
//            for (VariableInfo info : variables) {
//                System.out.printf("| %-20s | %-30s | %-19s | %-10s |\n",
//                        info.name,
//                        info.type,
//                        info.initializer.length() > 18 ? info.initializer.substring(0, 15) + "..." : info.initializer,
//                        info.scope);
//            }
//            System.out.println("+----------------------+--------------------------------+---------------------+------------+");
//        }
//    }
//
//    private static class VariableInfo {
//        String name;
//        String type;
//        String initializer;
//        String scope;
//
//        public VariableInfo(String name, String type, String initializer, String scope) {
//            this.name = name;
//            this.type = type;
//            this.initializer = initializer;
//            this.scope = scope;
//        }
//    }
//}