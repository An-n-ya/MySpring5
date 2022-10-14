package host.ankh.mySpring.aop.advice;

import host.ankh.mySpring.aop.aspect.MyJoinPoint;
import host.ankh.mySpring.aop.intercept.MyMethodInterceptor;
import host.ankh.mySpring.aop.intercept.MyMethodInvocation;

import java.lang.reflect.Method;

/**
 * @author ankh
 * @created at 2022-10-12 21:23
 */
public class MyAfterReturningAdvice extends MyAbstractAspectJAdvice implements MyAdvice, MyMethodInterceptor {
    private MyJoinPoint joinPoint;
    public MyAfterReturningAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        invokeAdviceMethod(joinPoint, returnValue, null);
    }

    @Override
    public Object invoke(MyMethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.joinPoint = mi;
        this.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }
}
