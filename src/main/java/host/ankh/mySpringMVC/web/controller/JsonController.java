package host.ankh.mySpringMVC.web.controller;

import host.ankh.mySpringMVC.annotation.MyAutowired;
import host.ankh.mySpringMVC.annotation.MyController;
import host.ankh.mySpringMVC.annotation.MyRequestMapping;
import host.ankh.mySpringMVC.annotation.MyRequestParam;
import host.ankh.mySpringMVC.web.service.MyDemoService;
import host.ankh.mySpringMVC.v1.components.MyModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ankh
 * @created at 2022-10-09 15:34
 */
@MyController
@MyRequestMapping("/web")
public class JsonController {
    @MyAutowired("DemoServiceImpl")
    MyDemoService demoService;

    @MyRequestMapping("/query")
    public MyModelAndView query(HttpServletRequest req, HttpServletResponse resp, @MyRequestParam("name") String name) {
        String result = demoService.get(name);
        try {
            resp.getWriter().write(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
