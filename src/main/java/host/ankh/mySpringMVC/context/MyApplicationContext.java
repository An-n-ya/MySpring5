package host.ankh.mySpringMVC.context;

import host.ankh.mySpringMVC.annotation.MyAutowired;
import host.ankh.mySpringMVC.annotation.MyController;
import host.ankh.mySpringMVC.annotation.MyService;
import host.ankh.mySpringMVC.core.MyBeanDefinition;
import host.ankh.mySpringMVC.core.MyBeanFactory;
import host.ankh.mySpringMVC.core.MyBeanWrapper;
import host.ankh.mySpringMVC.utils.Beans;

import java.lang.reflect.Field;
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

    // 下面两个 IOC 容器都是 注册式 单例模式的应用
    // 单例的 IOC 容器
    private Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();
    // 被代理的对象的 IOC 容器
    private Map<String, MyBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    public MyApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        System.out.println("configLocations = " + configLocations);
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
        doAutowried();


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
                    // 只调用 不使用返回值
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
        // 从父类中拿到 beanDefinition
        MyBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);

        // 初始化后处理器
        MyBeanPostProcessor beanPostProcessor = new MyBeanPostProcessor();

        // 将beanDefinition转化为实例
        Object instance = initiateBean(beanDefinition);
        // 确保实例化成功, 才进行后面的操作
        if (instance == null) return null;

        // 后处理器调用
        instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);

        // 引入 wrapper 是为了方便在 wrapper 中对bean进行操作
        MyBeanWrapper beanWrapper = new MyBeanWrapper(instance);
        // 将 wrapper 放入 IOC 容器中
        this.factoryBeanInstanceCache.put(beanName, beanWrapper);

        // 处理autowired
        // 注解相关的逻辑都在这里
        populateBean(beanName, instance);

        // 初始化完毕, 调用后处理器
        instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);

        return instance;
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception{
        // 转到上面的getBean去处理

        return getBean(Beans.getBeanName(beanClass.getName()));
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

    // 将 beanDefinition 转化为实例
    public Object initiateBean(MyBeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();

        // 先查一下IOC容器中是否已经有了
        if (this.factoryBeanObjectCache.containsKey(className)) {
            instance = this.factoryBeanObjectCache.get(className);
        } else {
            // 如果容器没有, 利用反射自己造
            try {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.factoryBeanObjectCache.put(beanDefinition.getBeanClassName(), instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    // 对该对象里的 autowired 属性进行注入
    public void populateBean(String beanName, Object instance) {
        Class<?> clazz = instance.getClass();

        if (!(clazz.isAnnotationPresent(MyController.class) || clazz.isAnnotationPresent(MyService.class))) {
            // 如果类没有被 @Controller 和 @MyService 注解, 直接返回
            return;
        }

        // 获取所有的域, 并检查每个域是否被 autowired 注解
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            if (!field.isAnnotationPresent(MyAutowired.class)) continue;

            MyAutowired autowired = field.getAnnotation(MyAutowired.class);
            String autowiredBeanName = autowired.value().trim();

            // 如果注解的名称为空, 使用类型注解
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }

            // 进行注入
            field.setAccessible(true);
            try {
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrapperInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
