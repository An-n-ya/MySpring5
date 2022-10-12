package host.ankh.mySpring.context;

import host.ankh.mySpring.core.MyBeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** IOC子类的典型代表
 * 这里简化以下, 只用来定义 IOC 缓存容器
 * @author ankh
 * @created at 2022-04-10 15:57
 */
public class MyDefaultListableBeanFactory extends MyAbstractApplicationContext{

    // 用来存储 BeanDefinition
    protected final Map<String, MyBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

}
