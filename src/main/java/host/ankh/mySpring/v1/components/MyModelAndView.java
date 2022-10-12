package host.ankh.mySpring.v1.components;

import java.util.Map;

/**
 * @author ankh
 * @created at 2022-04-23 20:39
 */
public class MyModelAndView {
    private String viewName; // 页面模板的名称
    private Map<String, ?> model; // 往页面传送的参数

    public MyModelAndView(String viewName) {
        this(viewName, null);
    }

    public MyModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
