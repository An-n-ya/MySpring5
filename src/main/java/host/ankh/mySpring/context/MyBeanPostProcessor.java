package host.ankh.mySpring.context;

/**
 * @author ankh
 * @created at 2022-04-15 11:11
 */
public class MyBeanPostProcessor {
    // Bean初始化之前调用
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    // Bean初始化之后调用
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
