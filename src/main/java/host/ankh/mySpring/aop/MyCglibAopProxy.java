package host.ankh.mySpring.aop;

import host.ankh.mySpring.aop.support.MyAdvisedSupport;

/**
 * @author ankh
 * @created at 2022-10-14 19:43
 */
public class MyCglibAopProxy implements MyAopProxy{
    private MyAdvisedSupport config;

    public MyCglibAopProxy(MyAdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
