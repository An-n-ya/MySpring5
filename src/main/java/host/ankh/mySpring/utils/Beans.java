package host.ankh.mySpring.utils;

/**
 * @author ankh
 * @created at 2022-04-22 16:17
 */
public class Beans {
    /**
     * 把全类名变为BeanName
     * @param className
     * @return
     */
    public static String getBeanName(String className) {
        String[] splits = className.split("\\.");
        return splits[splits.length - 1];
    }
}
