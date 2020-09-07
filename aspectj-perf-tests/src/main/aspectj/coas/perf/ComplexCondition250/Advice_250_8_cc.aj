
public aspect Advice_250_8_cc {

    pointcut test(): execution(int coas.perf.ComplexCondition250.Subject250.target(..)) && (execution(* *.junk0(..)) || execution(* *.junk1(..)) || execution(* *.junk2(..)) || execution(* *.junk3(..)) || execution(* *.junk4(..)) || execution(* *.junk5(..)) || execution(* *.junk6(..)) || execution(* *.junk7(..)) || execution(* *.junk8(..)) || execution(* *.junk9(..)) || execution(* *.junk10(..)) || execution(* *.junk11(..)) || execution(* *.junk12(..)) || execution(* *.junk13(..)) || execution(* *.junk14(..)) || execution(* *.junk15(..)) || execution(* *.junk16(..)) || execution(* *.junk17(..)) || execution(* *.junk18(..)) || execution(* *.junk19(..)) || execution(* *.junk20(..)) || execution(* *.junk21(..)) || execution(* *.junk22(..)) || execution(* *.junk23(..)) || execution(* *.junk24(..)) || execution(* *.junk25(..)) || execution(* *.junk26(..)) || execution(* *.junk27(..)) || execution(* *.junk28(..)) || execution(* *.junk29(..)) || execution(* *.target(..)));

    int around(): test() {

        int res = proceed();
        
        for (int i=0; i < 1000; i++) {
            if (res % 2 == 0) {
                res /= 2;
            } else {
                res = 2 * res + 1;
            }
        }

        return res;
  
    }
}
