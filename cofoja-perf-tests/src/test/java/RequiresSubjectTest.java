import org.junit.Test;

import pl.coco.perf.RequiresSubject;

import com.google.java.contract.PreconditionError;

public class RequiresSubjectTest {

    @Test(expected = PreconditionError.class)
    public void test1() {
        RequiresSubject subject = new RequiresSubject();
        subject.target10(0);
    }

}
