package host.ankh.mySpring.aop.intercept;

import host.ankh.mySpring.aop.aspect.MyJoinPoint;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ankh
 * @created at 2022-10-12 21:25
 */
public class MyMethodInvocation implements MyJoinPoint {
    private Object proxy;
    private Object target;
    private Method method;
    private Object[] args;
    private Class<?> targetClass;
    private List<Object> interceptorsAndDynamicMethodMatchers;

    // 用来保存自定义属性
    private Map<String, Object> userAttributes;
    private int currentInterceptorIndex =-1;

    public MyMethodInvocation(Object proxy, Object target, Method method, Object[] args, Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.args = args;
        this.targetClass = targetClass;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    public Object proceed() throws Throwable {
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            // 如果interceptor拦截链执行到了最后一个，调用方法并返回
            return this.method.invoke(this.target, this.args);
        }
        Object interceptorOrInterceptionAdvice = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);

        if (interceptorOrInterceptionAdvice instanceof MyMethodInterceptor) {
            // 调用拦截函数
            var mi = (MyMethodInterceptor) interceptorOrInterceptionAdvice;
            return mi.invoke(this);
        }
        return proceed();
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return  this.args;
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public void setUserAttribute(String key, Object value) {
        // 如果value为null就删除，非null就添加
        if (value != null) {
            if (this.userAttributes == null) {
                this.userAttributes = new HashMap<String, Object>();
            }
            this.userAttributes.put(key, value);
        } else {
            if (this.userAttributes != null) {
                this.userAttributes.remove(key);
            }
        }
    }

    @Override
    public Object getUserAttribute(String key) {
        return this.userAttributes != null ? this.userAttributes.get(key) : null;
    }

}
