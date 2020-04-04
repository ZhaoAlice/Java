package lang.innerclass;

/**
 * 〈green house〉<br>
 *
 * @author Carrie
 * @create 2020/4/4
 * @since 1.0.0
 */
public class GreenhouseControls extends Controller {
    private boolean light = false;

    public class LightOn extends Event {
        public LightOn(long delayTime) {
            super(delayTime);
        }

        public void action() {
            light = true;
        }

        public String toString() {
            return "light is on";
        }
    }
    public class LightOff extends Event {
        public LightOff(long delayTime) {
            super(delayTime);
        }

        public void action() {
            light = false;
        }

        public String toString() {
            return "light is off";
        }
    }
    private boolean water = false;
    public class WaterOn extends Event {
        public WaterOn(long delayTime) {
            super(delayTime);
        }

        public void action() {
            water = true;
        }

        public String toString() {
            return "water is on";
        }
    }
    public class WaterOff  extends Event {
        public WaterOff(long delayTime) {
            super(delayTime);
        }

        public void action() {
            water = false;
        }

        public String toString() {
            return "water is off";
        }
    }

    private String thermostat = "Day";

    public class ThermostatDay  extends Event {
        public ThermostatDay(long delayTime) {
            super(delayTime);
        }

        public void action() {
            thermostat = "Day";
        }

        public String toString() {
            return "thermostat on day setting";
        }
    }

    public class ThermostatNight  extends Event {
        public ThermostatNight(long delayTime) {
            super(delayTime);
        }

        public void action() {
            thermostat = "Night";
        }

        public String toString() {
            return "thermostat on night setting";
        }
    }

    public class Bell extends Event {

        public Bell(long delayTime) {
            super(delayTime);
        }

        @Override
        public String toString() {
            return "Bing";
        }

        public void action() {
            addEvent(new Bell(delayTime));
        }
    }

    public class Restart extends Event {
        private Event[] eventList;
        public Restart(long delayTime, Event[] eventList) {
            super(delayTime);
            this.eventList = eventList;
            for (Event event : eventList) {
                addEvent(event);
            }
        }

        public void action() {
            for (Event event : eventList) {
                event.start();
                addEvent(event);
            }
            start();
            addEvent(this);
        }

        public String toString() {
            return "Restart";
        }
    }

    public static class Terminal extends Event {
        public Terminal(long delayTime) {
            super(delayTime);
        }

        public void action() {
            System.exit(0);
        }
        public String toString() {
            return "system terminal";
        }
    }



}