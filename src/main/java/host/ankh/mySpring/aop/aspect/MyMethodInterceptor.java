package host.ankh.mySpring.aop.aspect;

import host.ankh.mySpring.aop.intercept.MyMethodInvocation;

/**
 * 方法拦截器顶层接口
 * @author ankh
 * @created at 2022-10-12 21:17
 */
public interface MyMethodInterceptor {
    Object invoke(MyMethodInvocation mi) throws Throwable;
}
