package host.ankh.mySpring.test;

import host.ankh.mySpring.context.MyApplicationContext;
import host.ankh.mySpring.test.beans.MyBean;

/**
 * @author ankh
 * @created at 2022-04-22 15:21
 */
public class Test {
    public static void main(String[] args) {
        MyApplicationContext context = new MyApplicationContext("application.properties");

        Object bean = null;
        try {
            bean = context.getBean(MyBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("bean = " + bean);

    }
}
