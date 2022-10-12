package host.ankh.mySpring.aop.advice;

import java.lang.reflect.Method;

/**
 * @author ankh
 * @created at 2022-10-12 21:21
 */
public class MyAbstractAspectJAdvice implements MyAdvice{

    private Method aspectMethod;
    private Object aspectTarget;

    public MyAbstractAspectJAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }
}
