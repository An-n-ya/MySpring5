package host.ankh.mySpring.aop;

import host.ankh.mySpring.aop.intercept.MyMethodInvocation;
import host.ankh.mySpring.aop.support.MyAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author ankh
 * @created at 2022-10-14 19:44
 */
public class MyJdkDynamicAopProxy implements MyAopProxy, InvocationHandler {
    private MyAdvisedSupport config;

    public MyJdkDynamicAopProxy(MyAdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object getProxy() {
        // 把对应类的类加载器放进去
        return getProxy(this.config.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.config.getTargetClass().getInterfaces(), this);
    }


    // 执行代理的入口
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptorsAndDynamicMethodMatchers = config.getInterceptorsAndDynamicInterceptionAdvice(method, this.config.getTargetClass());
        // 委托给拦截器链去执行
        MyMethodInvocation invocation = new MyMethodInvocation(
                proxy,
                this.config.getTarget(),
                method,
                args,
                this.config.getTargetClass(),
                interceptorsAndDynamicMethodMatchers);
        return invocation.proceed();
    }
}
