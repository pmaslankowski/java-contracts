package pl.coco.coco.invariant;

import pl.coco.api.code.Contract;
import pl.coco.api.code.InvariantMethod;

public class InvariantSubject100 {

    private int field = -1;

    @InvariantMethod
    public void invariants() {
        Contract.classInvariant(field != 0);
        Contract.classInvariant(field != 1);
        Contract.classInvariant(field != 2);
        Contract.classInvariant(field != 3);
        Contract.classInvariant(field != 4);
        Contract.classInvariant(field != 5);
        Contract.classInvariant(field != 6);
        Contract.classInvariant(field != 7);
        Contract.classInvariant(field != 8);
        Contract.classInvariant(field != 9);
        Contract.classInvariant(field != 10);
        Contract.classInvariant(field != 11);
        Contract.classInvariant(field != 12);
        Contract.classInvariant(field != 13);
        Contract.classInvariant(field != 14);
        Contract.classInvariant(field != 15);
        Contract.classInvariant(field != 16);
        Contract.classInvariant(field != 17);
        Contract.classInvariant(field != 18);
        Contract.classInvariant(field != 19);
        Contract.classInvariant(field != 20);
        Contract.classInvariant(field != 21);
        Contract.classInvariant(field != 22);
        Contract.classInvariant(field != 23);
        Contract.classInvariant(field != 24);
        Contract.classInvariant(field != 25);
        Contract.classInvariant(field != 26);
        Contract.classInvariant(field != 27);
        Contract.classInvariant(field != 28);
        Contract.classInvariant(field != 29);
        Contract.classInvariant(field != 30);
        Contract.classInvariant(field != 31);
        Contract.classInvariant(field != 32);
        Contract.classInvariant(field != 33);
        Contract.classInvariant(field != 34);
        Contract.classInvariant(field != 35);
        Contract.classInvariant(field != 36);
        Contract.classInvariant(field != 37);
        Contract.classInvariant(field != 38);
        Contract.classInvariant(field != 39);
        Contract.classInvariant(field != 40);
        Contract.classInvariant(field != 41);
        Contract.classInvariant(field != 42);
        Contract.classInvariant(field != 43);
        Contract.classInvariant(field != 44);
        Contract.classInvariant(field != 45);
        Contract.classInvariant(field != 46);
        Contract.classInvariant(field != 47);
        Contract.classInvariant(field != 48);
        Contract.classInvariant(field != 49);
        Contract.classInvariant(field != 50);
        Contract.classInvariant(field != 51);
        Contract.classInvariant(field != 52);
        Contract.classInvariant(field != 53);
        Contract.classInvariant(field != 54);
        Contract.classInvariant(field != 55);
        Contract.classInvariant(field != 56);
        Contract.classInvariant(field != 57);
        Contract.classInvariant(field != 58);
        Contract.classInvariant(field != 59);
        Contract.classInvariant(field != 60);
        Contract.classInvariant(field != 61);
        Contract.classInvariant(field != 62);
        Contract.classInvariant(field != 63);
        Contract.classInvariant(field != 64);
        Contract.classInvariant(field != 65);
        Contract.classInvariant(field != 66);
        Contract.classInvariant(field != 67);
        Contract.classInvariant(field != 68);
        Contract.classInvariant(field != 69);
        Contract.classInvariant(field != 70);
        Contract.classInvariant(field != 71);
        Contract.classInvariant(field != 72);
        Contract.classInvariant(field != 73);
        Contract.classInvariant(field != 74);
        Contract.classInvariant(field != 75);
        Contract.classInvariant(field != 76);
        Contract.classInvariant(field != 77);
        Contract.classInvariant(field != 78);
        Contract.classInvariant(field != 79);
        Contract.classInvariant(field != 80);
        Contract.classInvariant(field != 81);
        Contract.classInvariant(field != 82);
        Contract.classInvariant(field != 83);
        Contract.classInvariant(field != 84);
        Contract.classInvariant(field != 85);
        Contract.classInvariant(field != 86);
        Contract.classInvariant(field != 87);
        Contract.classInvariant(field != 88);
        Contract.classInvariant(field != 89);
        Contract.classInvariant(field != 90);
        Contract.classInvariant(field != 91);
        Contract.classInvariant(field != 92);
        Contract.classInvariant(field != 93);
        Contract.classInvariant(field != 94);
        Contract.classInvariant(field != 95);
        Contract.classInvariant(field != 96);
        Contract.classInvariant(field != 97);
        Contract.classInvariant(field != 98);
        Contract.classInvariant(field != 99);
    }

    public int target(int x) {
        if (field != Integer.MIN_VALUE) {
            field--;
        } else {
            field = 0;
        }
        return x;
    }
}
