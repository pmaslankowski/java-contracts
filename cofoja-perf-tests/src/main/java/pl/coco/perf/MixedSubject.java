package pl.coco.perf;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class MixedSubject {

    public int target(int x) {
        return x;
    }

    @Requires({
            "x != 0",
            "x != 1",
            "x != 2",
            "x != 3",
            "x != 4",
            "x != 5",
            "x != 6",
            "x != 7",
            "x != 8",
            "x != 9"
    })
    @Ensures({
            "old(x) != 0",
            "x != 0",
            "result != 0",
            "old(x) != 1",
            "x != 1",
            "result != 1",
            "old(x) != 2",
            "x != 2",
            "result != 2",
            "old(x) != 3",
            "x != 3",
            "result != 3",
            "old(x) != 4",
            "x != 4",
            "result != 4",
            "old(x) != 5",
            "x != 5",
            "result != 5",
            "old(x) != 6",
            "x != 6",
            "result != 6",
            "old(x) != 7",
            "x != 7",
            "result != 7",
            "old(x) != 8",
            "x != 8",
            "result != 8",
            "old(x) != 9",
            "x != 9",
            "result != 9",
    })
    public int target10(int x) {
        return x;
    }

    @Requires({
            "x != 0",
            "x != 1",
            "x != 2",
            "x != 3",
            "x != 4",
            "x != 5",
            "x != 6",
            "x != 7",
            "x != 8",
            "x != 9",
            "x != 10",
            "x != 11",
            "x != 12",
            "x != 13",
            "x != 14",
            "x != 15",
            "x != 16",
            "x != 17",
            "x != 18",
            "x != 19",
            "x != 20",
            "x != 21",
            "x != 22",
            "x != 23",
            "x != 24",
            "x != 25",
            "x != 26",
            "x != 27",
            "x != 28",
            "x != 29",
            "x != 30",
            "x != 31",
            "x != 32",
            "x != 33",
            "x != 34",
            "x != 35",
            "x != 36",
            "x != 37",
            "x != 38",
            "x != 39",
            "x != 40",
            "x != 41",
            "x != 42",
            "x != 43",
            "x != 44",
            "x != 45",
            "x != 46",
            "x != 47",
            "x != 48",
            "x != 49",
            "x != 50",
            "x != 51",
            "x != 52",
            "x != 53",
            "x != 54",
            "x != 55",
            "x != 56",
            "x != 57",
            "x != 58",
            "x != 59",
            "x != 60",
            "x != 61",
            "x != 62",
            "x != 63",
            "x != 64",
            "x != 65",
            "x != 66",
            "x != 67",
            "x != 68",
            "x != 69",
            "x != 70",
            "x != 71",
            "x != 72",
            "x != 73",
            "x != 74",
            "x != 75",
            "x != 76",
            "x != 77",
            "x != 78",
            "x != 79",
            "x != 80",
            "x != 81",
            "x != 82",
            "x != 83",
            "x != 84",
            "x != 85",
            "x != 86",
            "x != 87",
            "x != 88",
            "x != 89",
            "x != 90",
            "x != 91",
            "x != 92",
            "x != 93",
            "x != 94",
            "x != 95",
            "x != 96",
            "x != 97",
            "x != 98",
            "x != 99"
    })
    @Ensures({
            "old(x) != 0",
            "x != 0",
            "result != 0",
            "old(x) != 1",
            "x != 1",
            "result != 1",
            "old(x) != 2",
            "x != 2",
            "result != 2",
            "old(x) != 3",
            "x != 3",
            "result != 3",
            "old(x) != 4",
            "x != 4",
            "result != 4",
            "old(x) != 5",
            "x != 5",
            "result != 5",
            "old(x) != 6",
            "x != 6",
            "result != 6",
            "old(x) != 7",
            "x != 7",
            "result != 7",
            "old(x) != 8",
            "x != 8",
            "result != 8",
            "old(x) != 9",
            "x != 9",
            "result != 9",
            "old(x) != 10",
            "x != 10",
            "result != 10",
            "old(x) != 11",
            "x != 11",
            "result != 11",
            "old(x) != 12",
            "x != 12",
            "result != 12",
            "old(x) != 13",
            "x != 13",
            "result != 13",
            "old(x) != 14",
            "x != 14",
            "result != 14",
            "old(x) != 15",
            "x != 15",
            "result != 15",
            "old(x) != 16",
            "x != 16",
            "result != 16",
            "old(x) != 17",
            "x != 17",
            "result != 17",
            "old(x) != 18",
            "x != 18",
            "result != 18",
            "old(x) != 19",
            "x != 19",
            "result != 19",
            "old(x) != 20",
            "x != 20",
            "result != 20",
            "old(x) != 21",
            "x != 21",
            "result != 21",
            "old(x) != 22",
            "x != 22",
            "result != 22",
            "old(x) != 23",
            "x != 23",
            "result != 23",
            "old(x) != 24",
            "x != 24",
            "result != 24",
            "old(x) != 25",
            "x != 25",
            "result != 25",
            "old(x) != 26",
            "x != 26",
            "result != 26",
            "old(x) != 27",
            "x != 27",
            "result != 27",
            "old(x) != 28",
            "x != 28",
            "result != 28",
            "old(x) != 29",
            "x != 29",
            "result != 29",
            "old(x) != 30",
            "x != 30",
            "result != 30",
            "old(x) != 31",
            "x != 31",
            "result != 31",
            "old(x) != 32",
            "x != 32",
            "result != 32",
            "old(x) != 33",
            "x != 33",
            "result != 33",
            "old(x) != 34",
            "x != 34",
            "result != 34",
            "old(x) != 35",
            "x != 35",
            "result != 35",
            "old(x) != 36",
            "x != 36",
            "result != 36",
            "old(x) != 37",
            "x != 37",
            "result != 37",
            "old(x) != 38",
            "x != 38",
            "result != 38",
            "old(x) != 39",
            "x != 39",
            "result != 39",
            "old(x) != 40",
            "x != 40",
            "result != 40",
            "old(x) != 41",
            "x != 41",
            "result != 41",
            "old(x) != 42",
            "x != 42",
            "result != 42",
            "old(x) != 43",
            "x != 43",
            "result != 43",
            "old(x) != 44",
            "x != 44",
            "result != 44",
            "old(x) != 45",
            "x != 45",
            "result != 45",
            "old(x) != 46",
            "x != 46",
            "result != 46",
            "old(x) != 47",
            "x != 47",
            "result != 47",
            "old(x) != 48",
            "x != 48",
            "result != 48",
            "old(x) != 49",
            "x != 49",
            "result != 49",
            "old(x) != 50",
            "x != 50",
            "result != 50",
            "old(x) != 51",
            "x != 51",
            "result != 51",
            "old(x) != 52",
            "x != 52",
            "result != 52",
            "old(x) != 53",
            "x != 53",
            "result != 53",
            "old(x) != 54",
            "x != 54",
            "result != 54",
            "old(x) != 55",
            "x != 55",
            "result != 55",
            "old(x) != 56",
            "x != 56",
            "result != 56",
            "old(x) != 57",
            "x != 57",
            "result != 57",
            "old(x) != 58",
            "x != 58",
            "result != 58",
            "old(x) != 59",
            "x != 59",
            "result != 59",
            "old(x) != 60",
            "x != 60",
            "result != 60",
            "old(x) != 61",
            "x != 61",
            "result != 61",
            "old(x) != 62",
            "x != 62",
            "result != 62",
            "old(x) != 63",
            "x != 63",
            "result != 63",
            "old(x) != 64",
            "x != 64",
            "result != 64",
            "old(x) != 65",
            "x != 65",
            "result != 65",
            "old(x) != 66",
            "x != 66",
            "result != 66",
            "old(x) != 67",
            "x != 67",
            "result != 67",
            "old(x) != 68",
            "x != 68",
            "result != 68",
            "old(x) != 69",
            "x != 69",
            "result != 69",
            "old(x) != 70",
            "x != 70",
            "result != 70",
            "old(x) != 71",
            "x != 71",
            "result != 71",
            "old(x) != 72",
            "x != 72",
            "result != 72",
            "old(x) != 73",
            "x != 73",
            "result != 73",
            "old(x) != 74",
            "x != 74",
            "result != 74",
            "old(x) != 75",
            "x != 75",
            "result != 75",
            "old(x) != 76",
            "x != 76",
            "result != 76",
            "old(x) != 77",
            "x != 77",
            "result != 77",
            "old(x) != 78",
            "x != 78",
            "result != 78",
            "old(x) != 79",
            "x != 79",
            "result != 79",
            "old(x) != 80",
            "x != 80",
            "result != 80",
            "old(x) != 81",
            "x != 81",
            "result != 81",
            "old(x) != 82",
            "x != 82",
            "result != 82",
            "old(x) != 83",
            "x != 83",
            "result != 83",
            "old(x) != 84",
            "x != 84",
            "result != 84",
            "old(x) != 85",
            "x != 85",
            "result != 85",
            "old(x) != 86",
            "x != 86",
            "result != 86",
            "old(x) != 87",
            "x != 87",
            "result != 87",
            "old(x) != 88",
            "x != 88",
            "result != 88",
            "old(x) != 89",
            "x != 89",
            "result != 89",
            "old(x) != 90",
            "x != 90",
            "result != 90",
            "old(x) != 91",
            "x != 91",
            "result != 91",
            "old(x) != 92",
            "x != 92",
            "result != 92",
            "old(x) != 93",
            "x != 93",
            "result != 93",
            "old(x) != 94",
            "x != 94",
            "result != 94",
            "old(x) != 95",
            "x != 95",
            "result != 95",
            "old(x) != 96",
            "x != 96",
            "result != 96",
            "old(x) != 97",
            "x != 97",
            "result != 97",
            "old(x) != 98",
            "x != 98",
            "result != 98",
            "old(x) != 99",
            "x != 99",
            "result != 99"
    })
    public int target100(int x) {
        return x;
    }
}
