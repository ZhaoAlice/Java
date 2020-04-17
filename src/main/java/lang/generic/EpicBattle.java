package lang.generic;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2020/4/17
 * @since 1.0.0
 */
interface SuperPower {

}

interface XRayVision extends SuperPower {
    void seeThroughWalls();
}

interface SuperHearing extends SuperPower {
    void hearSubtleNoises();
}

interface SuperSmell extends SuperPower {
    void trackBySmell();
}

class SuperHero<POWER extends SuperPower> {
    POWER power;
    SuperHero(POWER power) {
        this.power = power;
    }

    POWER getPower() {
        return power;
    }
}

class SuperSleuth<POWER extends XRayVision> extends SuperHero<POWER> {

    SuperSleuth(POWER power) {
        super(power);
    }
    void see() {
        power.seeThroughWalls();
    }
}

class CanieHero<POWER extends SuperHearing & SuperSmell> extends SuperHero<POWER> {

    CanieHero(POWER power) {
        super(power);
    }

    void hear() {
        power.hearSubtleNoises();
    }

    void smell() {
        power.trackBySmell();
    }
}

class SuperHearSmell implements SuperHearing, SuperSmell {

    @Override
    public void hearSubtleNoises() {

    }

    @Override
    public void trackBySmell() {

    }
}

class DogPerson extends CanieHero<SuperHearSmell> {

    DogPerson(SuperHearSmell superHearSmell) {
        super(superHearSmell);
    }
}
public class EpicBattle {

    public static void main(String[] args) {
        DogPerson dogPerson = new DogPerson(new SuperHearSmell());
    }
}