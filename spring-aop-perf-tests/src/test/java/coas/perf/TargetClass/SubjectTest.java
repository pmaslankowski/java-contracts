package coas.perf.TargetClass;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SubjectTest {

    @Test
    public void test() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("user-bean-config.xml");
        Subject subject = context.getBean(Subject.class);

        subject.target(10);
    }
}
