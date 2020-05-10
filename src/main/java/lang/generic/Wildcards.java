package lang.generic;

/**
 * 〈原生类型 具有具体的类型参数以及具有无界通配符参数〉<br>
 *
 * @author Carrie
 * @create 2020/5/10
 * @since 1.0.0
 */
public class Wildcards {
    static void rawArgs(Holder holder, Object object) {
        holder.setValue(object);
//        holder.setValue(new Wildcards());
        Object o = holder.getValue();
    }
    static void unboundedArg(Holder<?> holder, Object arg) {
        // 需要?
//        holder.setValue(arg);
//        holder.setValue(new Wildcards());
        Object o = holder.getValue();
    }

    static <T> T exact1(Holder<T> holder) {
        T t = holder.getValue();
        return t;
    }
    static <T> T exact2(Holder<T> holder, T item) {
        holder.setValue(item);
        T t = holder.getValue();
        return t;
    }

    static <T> T wildSubtype(Holder<? extends T> holder, T item) {
//        holder.setValue(item);
        T t = holder.getValue();
        return t;
    }

    static <T> T wildSupertype(Holder<? super T> holder, T item) {
        holder.setValue(item);
//        T t = holder.getValue();
        Object o1 = holder.getValue();
        T o = (T) holder.getValue();
        return o;
    }

    public static void main(String[] args) {
        Holder raw = new Holder();
        // or
//        raw = new Holder<Long>();
        Holder<Long> qualified = new Holder<Long>();
        Holder<?> unbounded = new Holder();
        Holder<? extends Long> bounded = new Holder<Long>();
        Long lng = 1L;
        rawArgs(raw, lng);
        rawArgs(qualified, lng);
        rawArgs(unbounded, lng);
        rawArgs(bounded, lng);

        unboundedArg(raw, lng);
        unboundedArg(qualified, lng);
        unboundedArg(unbounded, lng);
        unboundedArg(bounded, lng);

        Object o1 = exact1(raw);
        System.out.println(o1);
        Long l2 = exact1(qualified);

        Object l3 = exact1(unbounded);
        Long l4 = exact1(bounded);

        Long l1 = exact2(raw, lng);
        System.out.println(l1);
        Long l5 = exact2(qualified, lng);
//        Long l6 = exact2(unbounded, lng);
//        Long l7 = exact2(bounded, lng);
        // 感觉现在的编译器可以处理这种情况了 跟书中是不一样的
        Long l8 = wildSubtype(raw, lng);

        Long l9 = wildSubtype(qualified, lng);
        Object l10 = wildSubtype(unbounded, lng);
        Long l11 = wildSubtype(bounded, lng);

//        Object l12 = wildSupertype(unbounded, lng);

        Long l13 = wildSupertype(raw, lng);
//        Object l12 = wildSupertype(bounded, lng);
        Long l14 = wildSupertype(qualified, lng);
    }
 }