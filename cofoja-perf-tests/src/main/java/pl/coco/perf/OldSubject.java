package pl.coco.perf;

import com.google.java.contract.Ensures;

public class OldSubject {

    public int target(int x) {
        return x;
    }

    @Ensures({
            "old (x) != 0",
            "old (x) != 1",
            "old (x) != 2",
            "old (x) != 3",
            "old (x) != 4",
            "old (x) != 5",
            "old (x) != 6",
            "old (x) != 7",
            "old (x) != 8",
            "old (x) != 9",
    })
    public int target10(int x) {
        return x;
    }

    @Ensures({
            "old (x) != 0",
            "old (x) != 1",
            "old (x) != 2",
            "old (x) != 3",
            "old (x) != 4",
            "old (x) != 5",
            "old (x) != 6",
            "old (x) != 7",
            "old (x) != 8",
            "old (x) != 9",
            "old (x) != 10",
            "old (x) != 11",
            "old (x) != 12",
            "old (x) != 13",
            "old (x) != 14",
            "old (x) != 15",
            "old (x) != 16",
            "old (x) != 17",
            "old (x) != 18",
            "old (x) != 19",
            "old (x) != 20",
            "old (x) != 21",
            "old (x) != 22",
            "old (x) != 23",
            "old (x) != 24",
            "old (x) != 25",
            "old (x) != 26",
            "old (x) != 27",
            "old (x) != 28",
            "old (x) != 29",
            "old (x) != 30",
            "old (x) != 31",
            "old (x) != 32",
            "old (x) != 33",
            "old (x) != 34",
            "old (x) != 35",
            "old (x) != 36",
            "old (x) != 37",
            "old (x) != 38",
            "old (x) != 39",
            "old (x) != 40",
            "old (x) != 41",
            "old (x) != 42",
            "old (x) != 43",
            "old (x) != 44",
            "old (x) != 45",
            "old (x) != 46",
            "old (x) != 47",
            "old (x) != 48",
            "old (x) != 49",
            "old (x) != 50",
            "old (x) != 51",
            "old (x) != 52",
            "old (x) != 53",
            "old (x) != 54",
            "old (x) != 55",
            "old (x) != 56",
            "old (x) != 57",
            "old (x) != 58",
            "old (x) != 59",
            "old (x) != 60",
            "old (x) != 61",
            "old (x) != 62",
            "old (x) != 63",
            "old (x) != 64",
            "old (x) != 65",
            "old (x) != 66",
            "old (x) != 67",
            "old (x) != 68",
            "old (x) != 69",
            "old (x) != 70",
            "old (x) != 71",
            "old (x) != 72",
            "old (x) != 73",
            "old (x) != 74",
            "old (x) != 75",
            "old (x) != 76",
            "old (x) != 77",
            "old (x) != 78",
            "old (x) != 79",
            "old (x) != 80",
            "old (x) != 81",
            "old (x) != 82",
            "old (x) != 83",
            "old (x) != 84",
            "old (x) != 85",
            "old (x) != 86",
            "old (x) != 87",
            "old (x) != 88",
            "old (x) != 89",
            "old (x) != 90",
            "old (x) != 91",
            "old (x) != 92",
            "old (x) != 93",
            "old (x) != 94",
            "old (x) != 95",
            "old (x) != 96",
            "old (x) != 97",
            "old (x) != 98",
            "old (x) != 99"
    })
    public int target100(int x) {
        return x;
    }

//    @Ensures({
//            "old (x) != 0",
//            "old (x) != 1",
//            "old (x) != 2",
//            "old (x) != 3",
//            "old (x) != 4",
//            "old (x) != 5",
//            "old (x) != 6",
//            "old (x) != 7",
//            "old (x) != 8",
//            "old (x) != 9",
//            "old (x) != 10",
//            "old (x) != 11",
//            "old (x) != 12",
//            "old (x) != 13",
//            "old (x) != 14",
//            "old (x) != 15",
//            "old (x) != 16",
//            "old (x) != 17",
//            "old (x) != 18",
//            "old (x) != 19",
//            "old (x) != 20",
//            "old (x) != 21",
//            "old (x) != 22",
//            "old (x) != 23",
//            "old (x) != 24",
//            "old (x) != 25",
//            "old (x) != 26",
//            "old (x) != 27",
//            "old (x) != 28",
//            "old (x) != 29",
//            "old (x) != 30",
//            "old (x) != 31",
//            "old (x) != 32",
//            "old (x) != 33",
//            "old (x) != 34",
//            "old (x) != 35",
//            "old (x) != 36",
//            "old (x) != 37",
//            "old (x) != 38",
//            "old (x) != 39",
//            "old (x) != 40",
//            "old (x) != 41",
//            "old (x) != 42",
//            "old (x) != 43",
//            "old (x) != 44",
//            "old (x) != 45",
//            "old (x) != 46",
//            "old (x) != 47",
//            "old (x) != 48",
//            "old (x) != 49",
//            "old (x) != 50",
//            "old (x) != 51",
//            "old (x) != 52",
//            "old (x) != 53",
//            "old (x) != 54",
//            "old (x) != 55",
//            "old (x) != 56",
//            "old (x) != 57",
//            "old (x) != 58",
//            "old (x) != 59",
//            "old (x) != 60",
//            "old (x) != 61",
//            "old (x) != 62",
//            "old (x) != 63",
//            "old (x) != 64",
//            "old (x) != 65",
//            "old (x) != 66",
//            "old (x) != 67",
//            "old (x) != 68",
//            "old (x) != 69",
//            "old (x) != 70",
//            "old (x) != 71",
//            "old (x) != 72",
//            "old (x) != 73",
//            "old (x) != 74",
//            "old (x) != 75",
//            "old (x) != 76",
//            "old (x) != 77",
//            "old (x) != 78",
//            "old (x) != 79",
//            "old (x) != 80",
//            "old (x) != 81",
//            "old (x) != 82",
//            "old (x) != 83",
//            "old (x) != 84",
//            "old (x) != 85",
//            "old (x) != 86",
//            "old (x) != 87",
//            "old (x) != 88",
//            "old (x) != 89",
//            "old (x) != 90",
//            "old (x) != 91",
//            "old (x) != 92",
//            "old (x) != 93",
//            "old (x) != 94",
//            "old (x) != 95",
//            "old (x) != 96",
//            "old (x) != 97",
//            "old (x) != 98",
//            "old (x) != 99",
//            "old (x) != 100",
//            "old (x) != 101",
//            "old (x) != 102",
//            "old (x) != 103",
//            "old (x) != 104",
//            "old (x) != 105",
//            "old (x) != 106",
//            "old (x) != 107",
//            "old (x) != 108",
//            "old (x) != 109",
//            "old (x) != 110",
//            "old (x) != 111",
//            "old (x) != 112",
//            "old (x) != 113",
//            "old (x) != 114",
//            "old (x) != 115",
//            "old (x) != 116",
//            "old (x) != 117",
//            "old (x) != 118",
//            "old (x) != 119",
//            "old (x) != 120",
//            "old (x) != 121",
//            "old (x) != 122",
//            "old (x) != 123",
//            "old (x) != 124",
//            "old (x) != 125",
//            "old (x) != 126",
//            "old (x) != 127",
//            "old (x) != 128",
//            "old (x) != 129",
//            "old (x) != 130",
//            "old (x) != 131",
//            "old (x) != 132",
//            "old (x) != 133",
//            "old (x) != 134",
//            "old (x) != 135",
//            "old (x) != 136",
//            "old (x) != 137",
//            "old (x) != 138",
//            "old (x) != 139",
//            "old (x) != 140",
//            "old (x) != 141",
//            "old (x) != 142",
//            "old (x) != 143",
//            "old (x) != 144",
//            "old (x) != 145",
//            "old (x) != 146",
//            "old (x) != 147",
//            "old (x) != 148",
//            "old (x) != 149",
//            "old (x) != 150",
//            "old (x) != 151",
//            "old (x) != 152",
//            "old (x) != 153",
//            "old (x) != 154",
//            "old (x) != 155",
//            "old (x) != 156",
//            "old (x) != 157",
//            "old (x) != 158",
//            "old (x) != 159",
//            "old (x) != 160",
//            "old (x) != 161",
//            "old (x) != 162",
//            "old (x) != 163",
//            "old (x) != 164",
//            "old (x) != 165",
//            "old (x) != 166",
//            "old (x) != 167",
//            "old (x) != 168",
//            "old (x) != 169",
//            "old (x) != 170",
//            "old (x) != 171",
//            "old (x) != 172",
//            "old (x) != 173",
//            "old (x) != 174",
//            "old (x) != 175",
//            "old (x) != 176",
//            "old (x) != 177",
//            "old (x) != 178",
//            "old (x) != 179",
//            "old (x) != 180",
//            "old (x) != 181",
//            "old (x) != 182",
//            "old (x) != 183",
//            "old (x) != 184",
//            "old (x) != 185",
//            "old (x) != 186",
//            "old (x) != 187",
//            "old (x) != 188",
//            "old (x) != 189",
//            "old (x) != 190",
//            "old (x) != 191",
//            "old (x) != 192",
//            "old (x) != 193",
//            "old (x) != 194",
//            "old (x) != 195",
//            "old (x) != 196",
//            "old (x) != 197",
//            "old (x) != 198",
//            "old (x) != 199",
//            "old (x) != 200",
//            "old (x) != 201",
//            "old (x) != 202",
//            "old (x) != 203",
//            "old (x) != 204",
//            "old (x) != 205",
//            "old (x) != 206",
//            "old (x) != 207",
//            "old (x) != 208",
//            "old (x) != 209",
//            "old (x) != 210",
//            "old (x) != 211",
//            "old (x) != 212",
//            "old (x) != 213",
//            "old (x) != 214",
//            "old (x) != 215",
//            "old (x) != 216",
//            "old (x) != 217",
//            "old (x) != 218",
//            "old (x) != 219",
//            "old (x) != 220",
//            "old (x) != 221",
//            "old (x) != 222",
//            "old (x) != 223",
//            "old (x) != 224",
//            "old (x) != 225",
//            "old (x) != 226",
//            "old (x) != 227",
//            "old (x) != 228",
//            "old (x) != 229",
//            "old (x) != 230",
//            "old (x) != 231",
//            "old (x) != 232",
//            "old (x) != 233",
//            "old (x) != 234",
//            "old (x) != 235",
//            "old (x) != 236",
//            "old (x) != 237",
//            "old (x) != 238",
//            "old (x) != 239",
//            "old (x) != 240",
//            "old (x) != 241",
//            "old (x) != 242",
//            "old (x) != 243",
//            "old (x) != 244",
//            "old (x) != 245",
//            "old (x) != 246",
//            "old (x) != 247",
//            "old (x) != 248",
//            "old (x) != 249",
//            "old (x) != 250",
//            "old (x) != 251",
//            "old (x) != 252",
//            "old (x) != 253",
//            "old (x) != 254",
//            "old (x) != 255",
//            "old (x) != 256",
//            "old (x) != 257",
//            "old (x) != 258",
//            "old (x) != 259",
//            "old (x) != 260",
//            "old (x) != 261",
//            "old (x) != 262",
//            "old (x) != 263",
//            "old (x) != 264",
//            "old (x) != 265",
//            "old (x) != 266",
//            "old (x) != 267",
//            "old (x) != 268",
//            "old (x) != 269",
//            "old (x) != 270",
//            "old (x) != 271",
//            "old (x) != 272",
//            "old (x) != 273",
//            "old (x) != 274",
//            "old (x) != 275",
//            "old (x) != 276",
//            "old (x) != 277",
//            "old (x) != 278",
//            "old (x) != 279",
//            "old (x) != 280",
//            "old (x) != 281",
//            "old (x) != 282",
//            "old (x) != 283",
//            "old (x) != 284",
//            "old (x) != 285",
//            "old (x) != 286",
//            "old (x) != 287",
//            "old (x) != 288",
//            "old (x) != 289",
//            "old (x) != 290",
//            "old (x) != 291",
//            "old (x) != 292",
//            "old (x) != 293",
//            "old (x) != 294",
//            "old (x) != 295",
//            "old (x) != 296",
//            "old (x) != 297",
//            "old (x) != 298",
//            "old (x) != 299",
//            "old (x) != 300",
//            "old (x) != 301",
//            "old (x) != 302",
//            "old (x) != 303",
//            "old (x) != 304",
//            "old (x) != 305",
//            "old (x) != 306",
//            "old (x) != 307",
//            "old (x) != 308",
//            "old (x) != 309",
//            "old (x) != 310",
//            "old (x) != 311",
//            "old (x) != 312",
//            "old (x) != 313",
//            "old (x) != 314",
//            "old (x) != 315",
//            "old (x) != 316",
//            "old (x) != 317",
//            "old (x) != 318",
//            "old (x) != 319",
//            "old (x) != 320",
//            "old (x) != 321",
//            "old (x) != 322",
//            "old (x) != 323",
//            "old (x) != 324",
//            "old (x) != 325",
//            "old (x) != 326",
//            "old (x) != 327",
//            "old (x) != 328",
//            "old (x) != 329",
//            "old (x) != 330",
//            "old (x) != 331",
//            "old (x) != 332",
//            "old (x) != 333",
//            "old (x) != 334",
//            "old (x) != 335",
//            "old (x) != 336",
//            "old (x) != 337",
//            "old (x) != 338",
//            "old (x) != 339",
//            "old (x) != 340",
//            "old (x) != 341",
//            "old (x) != 342",
//            "old (x) != 343",
//            "old (x) != 344",
//            "old (x) != 345",
//            "old (x) != 346",
//            "old (x) != 347",
//            "old (x) != 348",
//            "old (x) != 349",
//            "old (x) != 350",
//            "old (x) != 351",
//            "old (x) != 352",
//            "old (x) != 353",
//            "old (x) != 354",
//            "old (x) != 355",
//            "old (x) != 356",
//            "old (x) != 357",
//            "old (x) != 358",
//            "old (x) != 359",
//            "old (x) != 360",
//            "old (x) != 361",
//            "old (x) != 362",
//            "old (x) != 363",
//            "old (x) != 364",
//            "old (x) != 365",
//            "old (x) != 366",
//            "old (x) != 367",
//            "old (x) != 368",
//            "old (x) != 369",
//            "old (x) != 370",
//            "old (x) != 371",
//            "old (x) != 372",
//            "old (x) != 373",
//            "old (x) != 374",
//            "old (x) != 375",
//            "old (x) != 376",
//            "old (x) != 377",
//            "old (x) != 378",
//            "old (x) != 379",
//            "old (x) != 380",
//            "old (x) != 381",
//            "old (x) != 382",
//            "old (x) != 383",
//            "old (x) != 384",
//            "old (x) != 385",
//            "old (x) != 386",
//            "old (x) != 387",
//            "old (x) != 388",
//            "old (x) != 389",
//            "old (x) != 390",
//            "old (x) != 391",
//            "old (x) != 392",
//            "old (x) != 393",
//            "old (x) != 394",
//            "old (x) != 395",
//            "old (x) != 396",
//            "old (x) != 397",
//            "old (x) != 398",
//            "old (x) != 399",
//            "old (x) != 400",
//            "old (x) != 401",
//            "old (x) != 402",
//            "old (x) != 403",
//            "old (x) != 404",
//            "old (x) != 405",
//            "old (x) != 406",
//            "old (x) != 407",
//            "old (x) != 408",
//            "old (x) != 409",
//            "old (x) != 410",
//            "old (x) != 411",
//            "old (x) != 412",
//            "old (x) != 413",
//            "old (x) != 414",
//            "old (x) != 415",
//            "old (x) != 416",
//            "old (x) != 417",
//            "old (x) != 418",
//            "old (x) != 419",
//            "old (x) != 420",
//            "old (x) != 421",
//            "old (x) != 422",
//            "old (x) != 423",
//            "old (x) != 424",
//            "old (x) != 425",
//            "old (x) != 426",
//            "old (x) != 427",
//            "old (x) != 428",
//            "old (x) != 429",
//            "old (x) != 430",
//            "old (x) != 431",
//            "old (x) != 432",
//            "old (x) != 433",
//            "old (x) != 434",
//            "old (x) != 435",
//            "old (x) != 436",
//            "old (x) != 437",
//            "old (x) != 438",
//            "old (x) != 439",
//            "old (x) != 440",
//            "old (x) != 441",
//            "old (x) != 442",
//            "old (x) != 443",
//            "old (x) != 444",
//            "old (x) != 445",
//            "old (x) != 446",
//            "old (x) != 447",
//            "old (x) != 448",
//            "old (x) != 449",
//            "old (x) != 450",
//            "old (x) != 451",
//            "old (x) != 452",
//            "old (x) != 453",
//            "old (x) != 454",
//            "old (x) != 455",
//            "old (x) != 456",
//            "old (x) != 457",
//            "old (x) != 458",
//            "old (x) != 459",
//            "old (x) != 460",
//            "old (x) != 461",
//            "old (x) != 462",
//            "old (x) != 463",
//            "old (x) != 464",
//            "old (x) != 465",
//            "old (x) != 466",
//            "old (x) != 467",
//            "old (x) != 468",
//            "old (x) != 469",
//            "old (x) != 470",
//            "old (x) != 471",
//            "old (x) != 472",
//            "old (x) != 473",
//            "old (x) != 474",
//            "old (x) != 475",
//            "old (x) != 476",
//            "old (x) != 477",
//            "old (x) != 478",
//            "old (x) != 479",
//            "old (x) != 480",
//            "old (x) != 481",
//            "old (x) != 482",
//            "old (x) != 483",
//            "old (x) != 484",
//            "old (x) != 485",
//            "old (x) != 486",
//            "old (x) != 487",
//            "old (x) != 488",
//            "old (x) != 489",
//            "old (x) != 490",
//            "old (x) != 491",
//            "old (x) != 492",
//            "old (x) != 493",
//            "old (x) != 494",
//            "old (x) != 495",
//            "old (x) != 496",
//            "old (x) != 497",
//            "old (x) != 498",
//            "old (x) != 499",
//            "old (x) != 500"
//    })
//    public int target500(int x) {
//        return x;
//    }
}
