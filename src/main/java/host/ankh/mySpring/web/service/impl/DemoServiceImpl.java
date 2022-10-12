package host.ankh.mySpring.web.service.impl;

import host.ankh.mySpring.annotation.MyService;
import host.ankh.mySpring.web.service.MyDemoService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ankh
 * @created at 2022-03-11 8:00 PM
 */
@MyService("demoService")
public class DemoServiceImpl implements MyDemoService {
    @Override
    public String get(String name) {
        var sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        String json = "{name:\"" + name + "\", time:\"" + time + "\"}";
//        log.info("hello");
        return json;
    }
}
