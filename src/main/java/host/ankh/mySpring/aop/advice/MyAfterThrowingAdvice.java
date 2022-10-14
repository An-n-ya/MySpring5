package host.ankh.mySpring.aop.advice;

import host.ankh.mySpring.aop.intercept.MyMethodInterceptor;
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
        try {
            return mi.proceed();
        } catch (Throwable e) {
            // 调用错误处理拦截函数
            invokeAdviceMethod(mi, null, e.getCause());
            // 把错误抛出去
            throw e;
        }
    }

    public void setThrowingName(String aspectAfterThrowingName) {
        throwingName = aspectAfterThrowingName;
    }
}
