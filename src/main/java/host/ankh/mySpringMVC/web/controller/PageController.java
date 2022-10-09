package host.ankh.mySpringMVC.web.controller;

import host.ankh.mySpringMVC.annotation.MyAutowired;
import host.ankh.mySpringMVC.annotation.MyController;
import host.ankh.mySpringMVC.annotation.MyRequestMapping;
import host.ankh.mySpringMVC.annotation.MyRequestParam;
import host.ankh.mySpringMVC.web.service.MyDemoService;
import host.ankh.mySpringMVC.v1.components.MyModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ankh
 * @created at 2022-10-09 15:37
 */
@MyController
@MyRequestMapping("/")
public class PageController {
    @MyAutowired("DemoServiceImpl")
    MyDemoService demoService;

    @MyRequestMapping("/first.html")
    public MyModelAndView query(@MyRequestParam("teacher") String teacher) {
        String result = demoService.get(teacher);
        Map<String, Object> model = new HashMap<>();
        model.put("teacher", teacher);
        model.put("data", result);
        model.put("token", "123456");
        return new MyModelAndView("first.html", model);
    }
}
