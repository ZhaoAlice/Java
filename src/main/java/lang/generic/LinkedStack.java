package lang.generic;

import com.sun.org.apache.bcel.internal.generic.POP;

/**
 * 〈练习泛型: 一般用在容器中, 一个类想要承载什么类型的对象 就是模板〉<br>
 *
 * @author Carrie
 * @create 2020/4/6
 * @since 1.0.0
 */
public class LinkedStack<T> {
    private static class Node<U> {
        private U item;
        private Node<U> next;

        Node() {
            this.item = null;
            this.next = null;
        }
        Node(U item, Node<U> next) {
            this.item = item;
            this.next = next;
        }

        boolean end() {
            return item == null && next == null;
        }
    }
    private Node<T> top = new Node<T>();

    public void push(T item) {
        top = new Node<T>(item, top);
    }

    public T pop() {
        T item = top.item;
        if (!top.end()) {
            top= top.next;
        }
        return item;
    }

    public static void main(String[] args) {
        LinkedStack<String> linkedStack = new LinkedStack<String>();
        for (String s : "a b c d e f".split(" ")) {
            linkedStack.push(s);
        }
        String s;
        // 栈先进后出
        while ((s = linkedStack.pop()) != null) {
            System.out.println(s);
        }
    }
}