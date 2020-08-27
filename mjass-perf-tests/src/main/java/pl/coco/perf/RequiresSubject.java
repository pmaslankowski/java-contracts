package pl.coco.perf;

import jass.modern.Pre;

public class RequiresSubject {

    @Pre("x != 0")
    public int target10(int x) {
        return x;
    }
}
