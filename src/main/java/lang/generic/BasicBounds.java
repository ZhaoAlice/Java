package lang.generic;

import java.awt.*;

/**
 * 〈泛型边界〉<br>
 *
 * @author Carrie
 * @create 2020/4/17
 * @since 1.0.0
 */
interface HasColor {
    java.awt.Color getColor();
}

interface Weight {
    int weight();
}

class Coord {
    public int x, y, z;
}

class HoldItem<T> {
    T item;

    HoldItem(T item) {
        this.item = item;
    }

}
// 通过继承消除泛型边界中冗余代码 一个持有对象 颜色 坐标 线条
class WithColor<T extends HasColor> extends HoldItem<T> {
    WithColor(T item) {
        super(item);
    }

    java.awt.Color color() {
        return item.getColor();
    }
}

class WithColorCoord<T extends Coord & HasColor> extends WithColor<T> {

    WithColorCoord(T item) {
        super(item);
    }

    int getX() {
        return item.x;
    }

    int getY() {
        return item.y;
    }

    int getZ() {
        return item.z;
    }
}

class Solid<T extends Coord & HasColor & Weight> extends WithColorCoord<T> {

    Solid(T item) {
        super(item);
    }

    int weight() {
        return item.weight();
    }
}
class Bounded extends Coord implements HasColor, Weight {

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public int weight() {
        return 0;
    }
}

public class BasicBounds {

    public static void main(String[] args) {
        Solid<Bounded> solid = new Solid<>(new Bounded());
        solid.getX();
        solid.weight();
        solid.color();
    }
}
