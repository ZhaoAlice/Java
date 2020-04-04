package lang.innerclass;

/**
 * 〈exercise inner class callee〉<br>
 *
 * @author Carrie
 * @create 2020/4/4
 * @since 1.0.0
 */
public abstract class Event {
    private long eventTime;
    protected final long delayTime;

    public Event(long delayTime) {
        this.delayTime = delayTime;
        start();
    }

    public void start() {
        // 纳秒
        eventTime = System.nanoTime() + delayTime;
    }

    public boolean ready() {
        return System.nanoTime() >= eventTime;
    }

    public abstract void action();
}