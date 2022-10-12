package host.ankh.mySpring.aop.advice;

import host.ankh.mySpring.aop.aspect.MyJoinPoint;
import host.ankh.mySpring.aop.aspect.MyMethodInterceptor;
import host.ankh.mySpring.aop.intercept.MyMethodInvocation;

import java.lang.reflect.Method;

/**
 * @author ankh
 * @created at 2022-10-12 21:22
 */
public class MyMethodBeforeAdvice extends MyAbstractAspectJAdvice implements MyAdvice, MyMethodInterceptor {
    private MyJoinPoint joinPoint;

    public MyMethodBeforeAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MyMethodInvocation mi) throws Throwable {
        return null;
    }
}
