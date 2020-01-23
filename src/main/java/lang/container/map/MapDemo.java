package lang.container.map;

import java.util.HashMap;
import java.util.Map;

public class MapDemo {

    public static void main(String[] args) {
        Map<Integer, String> demo = new HashMap<Integer, String>();
        for (int i = 0; i < 1000; i++) {
            demo.put(i, "b");
        }


    }
}
