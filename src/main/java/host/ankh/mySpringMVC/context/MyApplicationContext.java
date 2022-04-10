package host.ankh.mySpringMVC.context;

import host.ankh.mySpringMVC.core.MyBeanDefinition;
import host.ankh.mySpringMVC.core.MyBeanFactory;
import host.ankh.mySpringMVC.core.MyBeanWrapper;

import java.util.List;
import java.util.Map;
import java.util.Properties;
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
    private MyBeanDefinitionReader reader;

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
        // 1. 定位配置文件
        reader = new MyBeanDefinitionReader(this.configLocations);

        // 2. 加载配置文件, 将它们封装成 BeanDefinition, 通过 reader 的 loadBeanDefinition 方法完成
        List<MyBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        // 3. 把 beanDefinitions 放入容器, 即注册
        doRegisterBeanDefinition(beanDefinitions);

        // 4. 把不是延时加载的类提前初始化



    }

    /**
     * 注册BeanDefinitions 到 IOC容器
     * @param beanDefinitions
     */
    private void doRegisterBeanDefinition(List<MyBeanDefinition> beanDefinitions) throws Exception {
        for (MyBeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception(beanDefinition.getFactoryBeanName() + " 已经存在了, 禁止重复注册");
            }
            // 注册到 DefaultListableBeanFactory 中的 IOC 容器里
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    /**
     * 处理非延时加载的情况
     */
    private void doAutowried() {
        for (Map.Entry<String, MyBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                // 如果不是延迟加载
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



    @Override
    public Object getBean(String beanName) throws Exception{
        // 在这里进行 DI AOP 等操作
        return null;
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception{
        // 转到上面的getBean去处理
        return getBean(beanClass.getName());
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }
}
