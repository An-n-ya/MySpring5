package host.ankh.mySpringMVC.v1.servlet;

import host.ankh.mySpringMVC.annotation.MyController;
import host.ankh.mySpringMVC.annotation.MyRequestMapping;
import host.ankh.mySpringMVC.annotation.MyService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author ankh
 * @created at 2022-03-09 11:30 AM
 */
public class MyDispatcherServlet extends HttpServlet {
    private Map<String, Object> mapping = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        InputStream is = null;
        try {
            Properties configContext = new Properties();
            is = this.getClass().getClassLoader().getResourceAsStream(config.getInitParameter("contextConfigLocation"));
            configContext.load(is);
            String scanPackage = configContext.getProperty("scanPackage");
            doScanner(scanPackage);
            // 这里如果直接使用mapping.keySet()做遍历会出错,所以就用了这种麻烦的形式
            Set<String> classNames = mapping.keySet();
            Object[] classArr = classNames.toArray();
            for (Object classNameObj : classArr) {
                String className = (String) classNameObj;
                if (!className.contains(".")) continue;
                // 利用反射获取Class
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(MyController.class)) {
                    // 如果是controller注解的类
                    mapping.put(className, clazz.newInstance());

                    // 处理在类上注解的MyRequestMapping,这个url作为基准url
                    String baseUrl = "";
                    if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
                        // 获取url信息
                        MyRequestMapping requestMapping = clazz.getAnnotation(MyRequestMapping.class);
                        baseUrl = requestMapping.value();
                    }
                    Method[] methods = clazz.getMethods();
                    for (Method method : methods) {
                        // 遍历controller类里的每一个被MyRequestMapping注解的方法
                        if (!method.isAnnotationPresent(MyRequestMapping.class)) continue;

                        // 获取url信息
                        MyRequestMapping requestMapping = method.getAnnotation(MyRequestMapping.class);
                        String subUrl = requestMapping.value();
                        String url = baseUrl + subUrl;
                        // 将方法放入"IOC容器"
                        mapping.put(url, method);
                        System.out.println("Mapped " + url + "," + method);
                    }

                } else if (clazz.isAnnotationPresent(MyService.class)) {
                    // 如果是service注解的类
                    MyService service = clazz.getAnnotation(MyService.class);
                    // 获取service注解定义的bean名称
                    String beanName = service.value();
                    // 如果没有设置bean名称,则设置为类名
                    if("".equals(beanName)) beanName = clazz.getName();
                    // 创建实例
                    Object obj = clazz.newInstance();
                    // 将创建出来的实例放入"IOC容器"
                    mapping.put(beanName, obj);

                    // service 所实现的接口也都需要有对应的bean
                    for (Class<?> i: clazz.getInterfaces()) {
                        mapping.put(i.getName(), obj);
                    }

                } else {
                    continue;
                }
            }


        } catch (Exception e) {} finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.init(config);
    }

    // 根据类地址扫描Bean,加载到IOC容器中
    private void doScanner(String scanPackage) {
        // 这里的replaceAll需要转义"."
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classDir = new File(url.getFile());
        for (File file : classDir.listFiles()) {
            if (file.isDirectory()) {
                // 递归搜索目录
                doScanner(scanPackage + "." + file.getName());
            } else {
                // 找到class文件
                if (!file.getName().endsWith(".class")) continue;
                String clazzName = (scanPackage + "." + file.getName().replace(".class", ""));
                mapping.put(clazzName, null);
            }
        }

    }

    // 做地址路由
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, InvocationTargetException, IllegalAccessException {
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        System.out.println("contextPath = " + contextPath);
        System.out.println("requestURI = " + requestURI);
        System.out.println("mapping = " + mapping);
        if (!mapping.containsKey(requestURI)) {resp.getWriter().write("404 NOT FOUND!"); return;}
        // 如果mapping中有对应的url路径,拿到对应的method
        Method method = (Method) mapping.get(requestURI);
        // 解析参数名称
        String paraName = null;
//        Parameter[] parameters = method.getParameters();
//        for (Parameter para : parameters) {
//            // 这里的参数老是拿不到
//            if (!para.isAnnotationPresent(MyRequestMapping.class)) continue;
//            // 找到被MyRquestMapping注解的参数
//            MyRequestMapping requestMapping = para.getAnnotation(MyRequestMapping.class);
//            // 接受的参数
//            paraName = requestMapping.value();
//        }
        paraName = "name";
        Map<String, String[]> params = req.getParameterMap();
        // 获取方法所属类
        Object obj = mapping.get(method.getDeclaringClass().getName());
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            // 对该类做属性注入
            String fieldName = field.getName();
            if (!mapping.containsKey(fieldName)) continue;
            field.setAccessible(true);
            field.set(obj, mapping.get(fieldName));
        }
        // 获取参数值
        Object[] args = {req, resp, params.get(paraName)[0]};
        // 调用对应的方法,并传入参数
        method.invoke(obj, args);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
