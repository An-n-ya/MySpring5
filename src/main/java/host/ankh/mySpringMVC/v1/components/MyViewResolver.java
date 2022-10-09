package host.ankh.mySpringMVC.v1.components;

import java.io.File;
import java.util.Locale;

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

    public MyView resolveViewNames(String viewName, Locale locale) throws Exception {
        this.viewName = viewName;
        if (viewName == null || "".equals((viewName.trim()))) {
            return null;
        }
        // 加上html后缀
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));
        return new MyView(templateFile);
    }

    public String getViewName() {
        return viewName;
    }



}
