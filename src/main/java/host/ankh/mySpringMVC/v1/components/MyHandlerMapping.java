package host.ankh.mySpringMVC.v1.components;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author ankh
 * @created at 2022-04-23 20:33
 */
public class MyHandlerMapping {
    private Object controller; // 目标方法所在的 controller 对象
    private Method method; // URL 对应的目标方法
    private Pattern pattern; // URL 的封装

    public MyHandlerMapping(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
