package host.ankh.mySpring.web.controller;

import host.ankh.mySpring.annotation.MyAutowired;
import host.ankh.mySpring.annotation.MyController;
import host.ankh.mySpring.annotation.MyRequestMapping;
import host.ankh.mySpring.annotation.MyRequestParam;
import host.ankh.mySpring.web.service.MyDemoService;
import host.ankh.mySpring.v1.components.MyModelAndView;

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
