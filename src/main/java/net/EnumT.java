package net;

public enum EnumT {
    A("a"), B("b");
    private String str;
    private EnumT(String s) {
        this.str = s;
    }
    public String getStr() {
        return str;
    }
    public void setStr(String a) {
        this.str= a;
    }

    public static void main(String[] args) {
        for(EnumT t : EnumT.values()) {
            System.out.println(t.getStr());
            t.setStr("as");
            System.out.println(t.getStr());

        }
        for(EnumT t : EnumT.values()) {
            System.out.println(t.getStr());

        }
    }
}
