package host.ankh.mySpring.aop;

/**
 * 获取代理对象的顶层抽象
 * @author ankh
 * @created at 2022-10-14 19:41
 */
public interface MyAopProxy {
    // 获得一个代理对象
    Object getProxy();
    // 通过自定义类加载器获得一个代理对象
    Object getProxy(ClassLoader classLoader);
}
