package host.ankh.mySpring.web.controller;

import host.ankh.mySpring.annotation.MyAutowired;
import host.ankh.mySpring.annotation.MyController;
import host.ankh.mySpring.annotation.MyRequestMapping;
import host.ankh.mySpring.annotation.MyRequestParam;
import host.ankh.mySpring.web.service.MyDemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ankh
 * @created at 2022-03-11 7:58 PM
 */
@MyController
public class DemoController {
    // 这里的field名称对应ioc容器里的bean名称
    @MyAutowired("DemoServiceImpl")
    private MyDemoService demoService;

    @MyRequestMapping("/name")
    public void name(HttpServletRequest req, HttpServletResponse resp, @MyRequestParam("name") String name) {
        String res = demoService.get(name);
        try {
            resp.getWriter().write(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
