package host.ankh.mySpring.aop.advice;

import host.ankh.mySpring.aop.aspect.MyJoinPoint;

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

    protected Object invokeAdviceMethod(MyJoinPoint joinPoint, Object returnValue, Throwable ex) throws Throwable {
        Class<?>[] paramsTypes = this.aspectMethod.getParameterTypes();
        if (null == paramsTypes || paramsTypes.length == 0) {
            // 如果没有参数，则直接调用
            return this.aspectMethod.invoke(aspectTarget);
        } else {
            Object[] args = new Object[paramsTypes.length];
            for (int i = 0; i < paramsTypes.length; i++) {
                if (paramsTypes[i] == MyJoinPoint.class) {
                    args[i] = joinPoint;
                } else if (paramsTypes[i] == Throwable.class) {
                    args[i] = ex;
                } else if (paramsTypes[i] == Object.class) {
                    args[i] = returnValue;
                }
            }

            return this.aspectMethod.invoke(aspectTarget, args);
        }
    }
}
