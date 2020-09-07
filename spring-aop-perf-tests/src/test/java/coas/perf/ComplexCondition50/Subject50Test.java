package coas.perf.ComplexCondition50;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Subject50Test {

    @Test
    public void test() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("user-bean-config.xml");
        Subject50 subject = context.getBean(Subject50.class);

        subject.target(10);
    }

}
