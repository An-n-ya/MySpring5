package host.ankh.mySpringMVC.core;

/**
 * BeanDefinition用来保存 Bean 的相关配置信息
 * @author ankh
 * @created at 2022-04-10 15:35
 */
public class MyBeanDefinition {
    private String beanClassName; // Bean的全类名
    private boolean lazyInit = false; // 是否延迟加载
    private String factoryBeanName; // 保存beanName, 在IOC容器中存储 key

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}
