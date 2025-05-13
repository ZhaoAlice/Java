package javaparser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class JavaParserExample {

    public static void main(String[] args) {
        try {
            CombinedTypeSolver typeSolver = new CombinedTypeSolver();
            typeSolver.add(new ReflectionTypeSolver(true)); // 解析 JDK 类
            //typeSolver.add(new JavaParserTypeSolver("D:\\zlg\\code\\gitea\\zmq-manager\\src\\main\\java")); // 解析项目源码
            //
            //// 配置全局 SymbolSolver
            JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
            StaticJavaParser.getParserConfiguration().setSymbolResolver(symbolSolver);
            // 加载 Java 文件
            File file = new File("D:\\work\\myself\\code\\Java\\src\\main\\java\\test\\ComprehensiveClass.java");
            CompilationUnit cu = StaticJavaParser.parse(file);
            List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = cu.findAll(ClassOrInterfaceDeclaration.class);
            List<FieldDeclaration> fields = cu.findAll(FieldDeclaration.class);
            List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);
            //methods.get(0).fi
            NodeList<Parameter> parameters = methods.get(8).getParameters();
            List<ImportDeclaration> importDeclarations = cu.findAll(ImportDeclaration.class);
            String name = fields.get(5).resolve().getType().describe();
            List<TypeDeclaration<?>> typeDeclarations = cu.getTypes();
            ClassOrInterfaceDeclaration classOrInterfaceDeclaration = classOrInterfaceDeclarations.get(0);
            NodeList<BodyDeclaration<?>> bodyDeclarations = classOrInterfaceDeclaration.getMembers();
            List<FieldDeclaration> fieldDeclarations = classOrInterfaceDeclaration.getFields();
            // 创建访问者并开始解析
            ClassVisitor classVisitor = new ClassVisitor();
            classVisitor.visit(cu, null);
            String aa = "ssh://git@git-nj.iwhalecloud.com:52422/ptdev01/aiops-copilot-v2.git";
            System.out.println(aa.substring(aa.lastIndexOf("/") + 1).replace(".git", ""));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 自定义访问者类
    private static class ClassVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(ClassOrInterfaceDeclaration cid, Void arg) {
            // 打印类名
            System.out.println("Class Name: " + cid.getName());

            // 打印泛型参数
            List<TypeParameter> typeParameters = cid.getTypeParameters();
            if (!typeParameters.isEmpty()) {
                System.out.println("Generic Parameters:");
                typeParameters.forEach(tp -> System.out.println(" - " + tp.getName()));
            }

            // 打印继承的父类
            cid.getExtendedTypes().forEach(et -> System.out.println("Extends: " + et.getName()));

            // 打印实现的接口
            cid.getImplementedTypes().forEach(it -> System.out.println("Implements: " + it.getName()));

            // 打印字段
            System.out.println("Fields:");
            List<FieldDeclaration> fields = cid.getFields();
            for (FieldDeclaration field : fields) {
                System.out.println(" - " + field.getVariables().get(0).getName() + ": " + field.getElementType());
            }

            // 打印方法
            System.out.println("Methods:");
            List<MethodDeclaration> methods = cid.getMethods();
            for (MethodDeclaration method : methods) {
                System.out.println(" - " + method.getName() + ": " + method.getType());
                if (method.getParameters().size() > 0) {
                    System.out.println("   Parameters:");
                    method.getParameters().forEach(p -> System.out.println("    * " + p.getName() + ": " + p.getType()));
                }
            }

            // 打印内部类和内部接口
            System.out.println("Inner Classes and Interfaces:");
            List<BodyDeclaration<?>> members = cid.getMembers();
            for (BodyDeclaration<?> member : members) {
                if (member instanceof ClassOrInterfaceDeclaration) {
                    ClassOrInterfaceDeclaration inner = (ClassOrInterfaceDeclaration) member;
                    System.out.println(" - " + inner.getName() + " (isInterface: " + inner.isInterface() + ")");
                }
            }

            super.visit(cid, arg);
        }
    }
}