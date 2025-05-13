package test;

import java.util.List;
import java.util.ArrayList;
import static javaparser.CodeAnalyzer.TEST;
import static javaparser.CodeAnalyzer.test;


// 父类
class ParentClass {
    protected int parentField = 10;

    public ParentClass parentMethod() {
        System.out.println("ParentClass: parentMethod");
        return new ParentClass();
    }
}

// 接口
interface MyInterface {
    void interfaceMethod();
}

// 另一个接口
interface AnotherInterface {
    void anotherMethod();
}

// 自定义类型
class CustomType extends ArrayList {
    private String name;

    private int i;

    public CustomType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

// 主类
public class ComprehensiveClass<T> extends ParentClass implements MyInterface {

    // 字段声明
    private int primitiveField; // 基本类型
    private Integer wrapperField; // 包装类型
    private CustomType customField; // 自定义类型
    private List<T> genericList; // 泛型字段
    private static final String aa = "";
    public static CustomType customType = new CustomType("");
    // 构造方法
    public ComprehensiveClass(int primitiveField, Integer wrapperField, CustomType customField) {
        this.primitiveField = primitiveField;
        this.wrapperField = wrapperField;
        this.customField = customField;
        this.genericList = new ArrayList<>();
    }

    // 覆盖父类方法
    @Override
    public ParentClass parentMethod() {
        System.out.println("ComprehensiveClass: Overridden parentMethod");
        return new ParentClass();
    }

    // 实现接口方法
    @Override
    public void interfaceMethod() {
        System.out.println("ComprehensiveClass: Implemented interfaceMethod");
    }

    // 泛型方法
    public <E> void genericMethod(E element) {
        System.out.println("Generic Method: " + element);
    }

    // 普通方法
    public void methodWithParameters(int primitiveParam, CustomType customParam) {
        int localVar = primitiveParam + 10; // 方法内部变量
        System.out.println("Local Variable: " + localVar);
        customParam.setName("UpdatedName");
    }

    // 调用其他类的方法
    public void callOtherClassMethod(CustomType customParam) {
        String name = customParam.getName(); // 调用 CustomType 的方法
        System.out.println("CustomType Name: " + name);
    }

    // 调用自身的方法
    public void callOwnMethod() {
        this.parentMethod().parentMethod(); // 调用覆盖的父类方法
        this.interfaceMethod(); // 调用实现的接口方法
    }

    // 静态方法
    public static void staticMethod() {
        System.out.println("Static Method");
    }

    // 内部类
    static class InnerClass extends ParentClass implements AnotherInterface {
        @Override
        public void anotherMethod() {
            System.out.println("InnerClass: Implemented anotherMethod");
        }
    }

    // 内部接口
    interface InnerInterface extends MyInterface {
        void innerInterfaceMethod();
    }

    // 内部类实现内部接口
    public static class InnerClassImplementingInterface extends spring.FreeMarkerExample implements InnerInterface {
        @Override
        public void interfaceMethod() {
            ComprehensiveClass.customType.getName();
            final String aa1 = ComprehensiveClass.aa;
            System.out.println("InnerClassImplementingInterface: Implemented interfaceMethod");
        }

        @Override
        public void innerInterfaceMethod() {
            System.out.println("InnerClassImplementingInterface: Implemented innerInterfaceMethod");
        }
    }

    enum TestEnum {
        T1,
        T2
    }

    // 主方法（测试）
    public static void main(String[] args) {
        ComprehensiveClass<String> instance = new ComprehensiveClass<>(5, 10, new CustomType("Test"));
        instance.methodWithParameters(20, new CustomType("CustomParam"));
        instance.callOtherClassMethod(new CustomType("CustomType"));
        instance.callOwnMethod();
        instance.genericMethod("Generic Parameter");

        // 内部类实例化
        InnerClass innerClass = new InnerClass();
        innerClass.anotherMethod();

        // 内部类实现接口实例化
        InnerClassImplementingInterface innerInterfaceImpl = new InnerClassImplementingInterface();
        innerInterfaceImpl.interfaceMethod();
        innerInterfaceImpl.innerInterfaceMethod();
    }
}