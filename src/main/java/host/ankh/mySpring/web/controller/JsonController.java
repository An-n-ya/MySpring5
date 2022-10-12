package host.ankh.mySpring.web.controller;

import host.ankh.mySpring.annotation.MyAutowired;
import host.ankh.mySpring.annotation.MyController;
import host.ankh.mySpring.annotation.MyRequestMapping;
import host.ankh.mySpring.annotation.MyRequestParam;
import host.ankh.mySpring.web.service.MyDemoService;
import host.ankh.mySpring.v1.components.MyModelAndView;

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
