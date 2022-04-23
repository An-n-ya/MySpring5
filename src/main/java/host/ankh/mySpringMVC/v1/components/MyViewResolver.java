package host.ankh.mySpringMVC.v1.components;

import java.io.File;

/**
 * 视图解析器
 * 根据用户传送的不同参数, 返回不同的结果
 * @author ankh
 * @created at 2022-04-23 21:11
 */
public class MyViewResolver {
    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private File templateRootDir;

    private String viewName;

    public MyViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(templateRootPath);
    }



}
