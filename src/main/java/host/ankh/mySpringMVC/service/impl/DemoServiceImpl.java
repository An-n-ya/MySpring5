package host.ankh.mySpringMVC.service.impl;

import host.ankh.mySpringMVC.annotation.MyService;
import host.ankh.mySpringMVC.service.IDemoService;

/**
 * @author ankh
 * @created at 2022-03-11 8:00 PM
 */
@MyService("demoService")
public class DemoServiceImpl implements IDemoService {
    @Override
    public String get(String name) {
        return "Hello, my name is " + name;
    }
}
