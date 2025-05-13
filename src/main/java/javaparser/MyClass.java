package javaparser;

import java.util.List;

import javaparser.mapper.EntityAnalyzer;

public class MyClass {
    private int count;
    protected String name;
    public List<String> items;
    final double PI = 3.14;
    private EntityAnalyzer entityAnalyzer;
    private ComprehensiveClass<String> comprehensiveClass;
    private ComprehensiveClass[] comprehensiveClass1;
    private int[] a;
    private Integer[] b;
    private test.ComprehensiveClass.InnerClassImplementingInterface[] comprehensiveClas3s1;
    private InnerClass innerClass;

    // 内部类
    static class InnerClass {
        public void anotherMethod() {
            System.out.println("InnerClass: Implemented anotherMethod");
        }
    }
}