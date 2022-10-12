package host.ankh.mySpring.aop.advice;

import host.ankh.mySpring.aop.aspect.MyMethodInterceptor;
import host.ankh.mySpring.aop.intercept.MyMethodInvocation;

import java.lang.reflect.Method;

/**
 * @author ankh
 * @created at 2022-10-12 21:24
 */
public class MyAfterThrowingAdvice extends MyAbstractAspectJAdvice implements MyAdvice, MyMethodInterceptor {
    private String throwingName;
    private MyMethodInvocation mi;

    public MyAfterThrowingAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MyMethodInvocation mi) throws Throwable {
        return null;
    }
}
