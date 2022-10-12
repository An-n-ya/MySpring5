package host.ankh.mySpring.aop.aspect;

import java.lang.reflect.Method;

/**
 * 回调连接点，通过它获得被代理的业务方法的所有信息
 * @author ankh
 * @created at 2022-10-12 21:14
 */
public interface MyJoinPoint {
    Method getMethod(); // 业务方法本身
    Object[] getArguments(); // 方法的实参
    Object getThis(); // 方法所属的实例

    // 在 JoinPoint 中添加自定义属性
    void setUserAttribute(String key, Object value);
    // 从已添加的属性中获取一个属性值
    Object getUserAttribute(String key);
}
