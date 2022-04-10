package host.ankh.mySpringMVC.core;

/**
 * @author ankh
 * @created at 2022-04-10 15:32
 */
public interface MyBeanFactory {
    /**
     * 根据beanName从 IOC 容器中获得一个实例 Bean
     * @param beanName
     * @return
     */
    Object getBean(String beanName);

    /**
     * 根据类型从 IOC 容器中获得一个实例 Bean
     * @param beanClass
     * @return
     */
    Object getBean(Class<?> beanClass);
}
