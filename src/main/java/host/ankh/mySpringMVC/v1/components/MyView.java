package host.ankh.mySpringMVC.v1.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ankh
 * @created at 2022-04-23 21:14
 */
public class MyView {
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";

    private File viewFile;

    public MyView(File viewFile) {
        this.viewFile = viewFile;
    }

    public String getContentType() {
        return DEFAULT_CONTENT_TYPE;
    }

    // 渲染模板
    public void render(Map<String, ?>model, HttpServletRequest request, HttpServletResponse response) throws Exception{
        StringBuilder sb = new StringBuilder();
        // 读文件
        RandomAccessFile ra = new RandomAccessFile(this.viewFile, "r");

        try {
            String line = null;
            while (null != (line = ra.readLine())) {
                line = new String(line.getBytes("ISO-8859-1"), "utf-8");
                Pattern pattern = Pattern.compile("\\$\\{[^\\}]+\\}", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    String paramName = matcher.group();
                    paramName = paramName.replaceAll("\\$\\{|\\}", "");
                    // 从内存中获取参数的值
                    Object paramValue = model.get(paramName);
                    if (null == paramValue) {continue;}
                    line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
                    matcher = pattern.matcher(line);
                }
                sb.append(line);
            }
        } finally {
            ra.close();
        }
        response.setCharacterEncoding("utf-8");;
        response.setContentType(DEFAULT_CONTENT_TYPE);
        response.getWriter().write(sb.toString());
    }

    public static String makeStringForRegExp(String str) {
        // 转义字符
        return str.replace("\\", "\\\\")
                .replace("*", "\\*")
                .replace("+", "\\+")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("^", "\\^")
                .replace("$", "\\$")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("?", "\\?")
                .replace(",", "\\,")
                .replace(".", "\\.")
                .replace("&", "\\&");

    }

}
