package lang.innerclass;

/**
 * 〈main〉<br>
 *
 * @author Carrie
 * @create 2020/4/4
 * @since 1.0.0
 */
public class GreenhouseController {
    public static void main(String[] args) {
        GreenhouseControls gc = new GreenhouseControls();
        gc.addEvent(gc.new Bell(900));

        Event[] eventList = {
                gc.new ThermostatDay(0),
                gc.new LightOn(200),
                gc.new LightOff(400),
                gc.new WaterOn(600),
                gc.new WaterOff(800),
                gc.new ThermostatNight(1400),
        };
        gc.addEvent(gc.new Restart(2000, eventList));
        if (args.length == 1) {
            gc.addEvent(new GreenhouseControls.Terminal(new Integer(args[0])));
        }
        gc.run();
    }
}