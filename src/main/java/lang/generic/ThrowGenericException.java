package lang.generic;

import java.util.ArrayList;
import java.util.List;

interface Processor<T, E extends Exception, F extends Exception> {
    void process(List<T> resultCollector) throws E, F;
    void process1(List<T> resultCollector) throws F;

}

class ProcessRunner<T, E extends Exception, F extends Exception> extends ArrayList<Processor<T, E, F>> {
    List<T> processAll() throws E, F {
        List<T> arrayList = new ArrayList<>();
        for (Processor<T, E, F> processor : this) {
            processor.process(arrayList);
        }
        return arrayList;
    }
}

class Failure1 extends Exception {}

class Failure2 extends Exception {}

class Process1 implements Processor<Integer, Failure1, Failure2> {

    static int count = 2;
    @Override
    public void process(List<Integer> resultCollector) throws Failure1 {
        if ((count-- == 0)) {
            resultCollector.add(47);
        }
        else {
            resultCollector.add(11);
        }
        if (count < 0) {
            throw new Failure1();
        }
    }

    @Override
    public void process1(List<Integer> resultCollector) throws Failure2 {

    }
}

class Process2 implements Processor<String, Failure2, Failure1> {

    static int count = 3;

    /**
     * 实现接口总的方法时 可以只抛出基类中某个异常 不抛出异常 抛出派生类异常
     * 另外 还有一种继承与实现并存 且基类也实现了接口 则子类中该方法只能抛出基类中定义的异常 或者不抛出异常
     * @param resultCollector
     * @throws Failure2
     */
    @Override
    public void process(List<String> resultCollector) throws Failure2 {
        if ((count-- == 0)) {
            resultCollector.add("Margaret Hale");
        }
        else {
            resultCollector.add("John Thornton");
        }
        if (count < 0) {
            throw new Failure2();
        }
    }
    @Override
    public void process1(List<String> resultCollector) throws Failure1 {
        if ((count-- == 0)) {
            resultCollector.add("Margaret Hale");
        }
        else {
            resultCollector.add("John Thornton");
        }
        if (count < 0) {
            throw new Failure1();
        }
    }
}
/**
 * 〈在throws语句中抛出泛型异常〉<br>
 *
 * @author Carrie
 * @create 2020/5/17
 * @since 1.0.0
 */
public class ThrowGenericException {
    public static void main(String[] args) {
        ProcessRunner<String, Failure2, Failure1> runner = new ProcessRunner<>();
        for (int i = 0; i < 3; i++) {
            runner.add(new Process2());
        }
        try {
            runner.processAll();
        } catch (Failure2 failure2) {
            failure2.printStackTrace();
        } catch (Failure1 failure1) {
            failure1.printStackTrace();
        }

        ProcessRunner<Integer, Failure1, Failure2> runner1 = new ProcessRunner<>();
        for (int i = 0; i < 3; i++) {
            runner1.add(new Process1());
        }
        try {
            runner1.processAll();
        } catch (Failure1 failure1) {
            failure1.printStackTrace();
        } catch (Failure2 failure2) {
            failure2.printStackTrace();
        }

    }

 }