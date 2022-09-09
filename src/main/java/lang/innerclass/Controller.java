package lang.innerclass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 〈控制器〉<br>
 *
 * @author Carrie
 * @create 2020/4/4
 * @since 1.0.0
 */
public class Controller {
    private List<Event> eventList = new ArrayList<Event>();

    public void addEvent(Event event) {
        eventList.add(event);
    }

    public void run() {
        Event event;
        while (eventList.size() > 0) {
            Iterator<Event> iterator = new ArrayList<>(eventList).iterator();
            // You should always check Ali's development manual
            while (iterator.hasNext()) {
                event = iterator.next();
                if (event.ready()) {
                    System.out.println(event);
                    event.action();
                    iterator.remove();
                }
            }
        }
    }

}