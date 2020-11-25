/**
 * Copyright (C), 2019
 * FileName: Event
 * Author:   ZLG
 * Date:     2019/7/6 9:53
 * Description: common methods
 * History:
 * <author>          <time>          <version>          <desc>
 */
package lang.innerclass.controller;

/**
 * 〈一句话功能简述〉<br> 
 * 〈common methods〉
 *
 * @author ZLG
 * @create 2019/7/6
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
        eventTime = System.nanoTime() + delayTime;
    }
    public boolean ready() {
        return System.nanoTime() >= eventTime;
    }
    public abstract void action();
}