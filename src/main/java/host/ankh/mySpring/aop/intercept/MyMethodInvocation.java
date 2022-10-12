package host.ankh.mySpring.aop.intercept;

import host.ankh.mySpring.aop.aspect.MyJoinPoint;

import java.lang.reflect.Method;

/**
 * @author ankh
 * @created at 2022-10-12 21:25
 */
public class MyMethodInvocation implements MyJoinPoint {
    @Override
    public Method getMethod() {
        return null;
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public Object getThis() {
        return null;
    }

    @Override
    public void setUserAttribute(String key, Object value) {

    }

    @Override
    public Object getUserAttribute(String key) {
        return null;
    }
}
