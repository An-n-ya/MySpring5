package host.ankh.mySpring.core;

/**
 * BeanWrapper主要用于封装创建后的对象实例
 * Bean实例 或 代理对象 都由 BeanWrapper 保存
 * @author ankh
 * @created at 2022-04-10 15:39
 */
public class MyBeanWrapper {
    private Object wrapperInstance;
    private Class<?> wrappedClass;

    public MyBeanWrapper(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public Class<?> getWrappedClass() {
        return this.wrapperInstance.getClass();
    }
}
