package host.ankh.mySpringMVC.controller;

import host.ankh.mySpringMVC.annotation.MyAutowired;
import host.ankh.mySpringMVC.annotation.MyController;
import host.ankh.mySpringMVC.annotation.MyRequestMapping;
import host.ankh.mySpringMVC.annotation.MyRequestParam;
import host.ankh.mySpringMVC.service.IDemoService;

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
    @MyAutowired
    private IDemoService demoService;

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
