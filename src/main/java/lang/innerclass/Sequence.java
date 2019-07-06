package lang.innerclass;

interface Selector {
    boolean end();
    Object current();
    void next();
}
// 5内部类与外部类对象，内部类可以引用外部类的变量，
// 从外部类的非静态方法之外创建对象使用outerclassname.innterclassname
public class Sequence {
    private Object[] items;
    private int next = 0;
    private static int ts = 0;
    public Sequence(int size) {
        items = new Object[size];
    }
    public void add(Object x) {
        if (next < items.length) {
            items[next++] = x;
        }
    }
    // 内部私有类，可以访问其外围类的所有成员（字段与方法），即使是private的
    private class SequenceSelector implements Selector {

        private int i = 0;
        @Override
        public boolean end() {
            return i == items.length;
        }

        @Override
        public Object current() {
            return items[i];
        }

        @Override
        public void next() {
            //静态变量也是可以访问的
            ts = 1;
            if (i < items.length) {
                i++;
            }
        }
    }
    public Selector selector() {
        return new SequenceSelector();
    }

    public static void main(String[] args) {
        Sequence sequence = new Sequence(10);
        for (int i = 0; i < 10; i++) {
            sequence.add(Integer.toString(i));
        }
        Selector selector = sequence.selector();
        while (!selector.end()) {
            System.out.println(selector.current());
            selector.next();
        }
    }
}
