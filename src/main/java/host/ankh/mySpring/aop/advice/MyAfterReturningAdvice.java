package host.ankh.mySpring.aop.advice;

import host.ankh.mySpring.aop.aspect.MyMethodInterceptor;
import host.ankh.mySpring.aop.intercept.MyMethodInvocation;

import java.lang.reflect.Method;

/**
 * @author ankh
 * @created at 2022-10-12 21:23
 */
public class MyAfterReturningAdvice extends MyAbstractAspectJAdvice implements MyAdvice, MyMethodInterceptor {
    public MyAfterReturningAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MyMethodInvocation mi) throws Throwable {
        return null;
    }
}
