/**
 * Copyright (C), 2019
 * FileName: Controller
 * Author:   ZLG
 * Date:     2019/7/6 20:08
 * Description: 控制器
 * History:
 * <author>          <time>          <version>          <desc>
 */
package lang.innerclass.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈控制器〉
 *
 * @author ZLG
 * @create 2019/7/6
 * @since 1.0.0
 */
public class Controller {

    private List<Event> eventList = new ArrayList<Event>();
    public void addEvent(Event event) {
        eventList.add(event);
    }
    public void run() {
        while (eventList.size() > 0) {
            for (Event event : eventList) {
                if (event.ready()) {
                    System.out.println(event);
                    event.action();
                    eventList.remove(event);
                }
            }
        }
    }
}