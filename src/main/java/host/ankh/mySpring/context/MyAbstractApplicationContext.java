package host.ankh.mySpring.context;

/** IOC 容器的顶层设计
 * @author ankh
 * @created at 2022-04-10 15:55
 */
public abstract class MyAbstractApplicationContext {
    // 只给子类重写
    public void refresh() throws Exception {}
}


