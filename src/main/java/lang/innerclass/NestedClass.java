package lang.innerclass;

public class NestedClass {
    private static class NCpntents implements Contents {
        int label;
        public NCpntents(int lbl) {
            label = lbl;
        }
        @Override
        public int value() {
            return label;
        }
        static int j = 9;
    }
    protected static class Ndestination implements Destination {
        String to;
        public Ndestination(String t) {
            to = t;
        }
        @Override
        public String readLabel() {
            return to;
        }
        private static String test = "test";
        static String getTest() {
            return test;
        }
    }

    public static Contents getContents() {
        return new NCpntents(9);
    }
    public static Destination getDestination() {
        return new Ndestination("t");
    }
    public static void main(String[] args) {
        NestedClass nestedClass = new NestedClass();
        NestedClass.Ndestination.getTest();
        Contents c = getContents();

    }
}
