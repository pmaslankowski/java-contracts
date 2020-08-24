import org.junit.Test;

import com.google.java.contract.PostconditionError;

import pl.coco.perf.EnsuresSubject;

public class EnsuresSubjectTest {

    @Test(expected = PostconditionError.class)
    public void test1() {
        EnsuresSubject subject = new EnsuresSubject();
        subject.target10(0);
    }

}
