package host.ankh.mySpringMVC.context;

import host.ankh.mySpringMVC.core.MyBeanDefinition;
import host.ankh.mySpringMVC.core.MyBeanFactory;
import host.ankh.mySpringMVC.core.MyBeanWrapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ApplicationContext 是直接接触用户的入口
 * 主要实现refresh方法 和 getBean 方法
 * 完成 IOC DI 和 AOP 的衔接
 * @author ankh
 * @created at 2022-04-10 15:59
 */
public class MyApplicationContext extends MyDefaultListableBeanFactory implements MyBeanFactory {
    private String[] configLocations; // 用来存储配置文件的位置
    private MyBeanDefinition reader;

    // 单例的 IOC 容器
    private Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();
    // 通用的 IOC 容器
    private Map<String, MyBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    public MyApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() throws Exception {
        reader =
    }

    @Override
    public Object getBean(String beanName) {
        return null;
    }

    @Override
    public Object getBean(Class<?> beanClass) {
        return null;
    }
}
