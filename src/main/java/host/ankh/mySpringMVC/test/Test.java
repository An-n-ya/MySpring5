package host.ankh.mySpringMVC.test;

import host.ankh.mySpringMVC.context.MyApplicationContext;
import host.ankh.mySpringMVC.test.beans.MyBean;

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
