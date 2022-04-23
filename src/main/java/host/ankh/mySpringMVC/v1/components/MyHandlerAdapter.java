package host.ankh.mySpringMVC.v1.components;

import host.ankh.mySpringMVC.annotation.MyRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 完成请求传递到服务端的"参数列表"与Method"实参列表"的对应关系
 * @author ankh
 * @created at 2022-04-23 20:35
 */
public class MyHandlerAdapter {
    public boolean supports(Object handler) {
        return handler instanceof MyHandlerMapping;
    }

    public MyModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        MyHandlerMapping handlerMapping = (MyHandlerMapping) handler;

        // 这里保存形参列表
        // 参数名字 --- 参数索引 的 map
        HashMap<String, Integer> paramMapping = new HashMap<>();

        // 通过反射获取"命名参数"
        Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < pa.length; i++) {
            for (Annotation a : pa[i]) {
                if (a instanceof MyRequestParam) {
                    String paramName = ((MyRequestParam) a).value();
                    if (!"".equals(paramName.trim())) {
                        paramMapping.put(paramName, i);
                    }
                }
            }
        }

        // 通过反射获取 response 和 request 类型的参数
        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> type = paramTypes[i];
            if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                paramMapping.put(type.getName(), i);
            }
        }

        // 通过 req 传进来的参数列表
        Map<String, String[]> reqParameterMap = req.getParameterMap();

        // 构造实参列表
        Object[] paramValues = new Object[paramTypes.length];

        for (Map.Entry<String, String[]> param : reqParameterMap.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replace("\\s", "");
            if (!paramMapping.containsKey(param.getKey())) continue;

            // 获取参数对应的索引位置
            int index = paramMapping.get(param.getKey());

            paramValues[index] = caseStringValue(value, paramTypes[index]);

        }

        if(paramMapping.containsKey(HttpServletRequest.class.getName())) {
            int reqIndex = paramMapping.get(HttpServletRequest.class.getName());
            // 如果方法需要 request 参数, 把这里的 req 传进去
            paramValues[reqIndex] = req;
        }

        if (paramMapping.containsKey(HttpServletResponse.class.getName())) {
            int respIndex = paramMapping.get(HttpServletResponse.class.getName());
            // 如果方法需要 response 参数, 把这里的 resp 传进去
            paramValues[respIndex] = resp;
        }

        // 从 handler 中取出 Controller, Method, 利用反射进行调用
        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);

        if (result == null) return null;

        // 对返回值进行转换
        // 这里写了ModelAndView的转换
        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == MyModelAndView.class;
        if (isModelAndView) {
            return (MyModelAndView) result;
        } else {
            return null;
        }


    }

    /**
     * 因为传进来的参数都是字符串格式的, 需要根据需要将字符串转换成需要的格式
     * 目前只支持整数的转换
     * @param value
     * @param clazz
     * @return
     */
    private Object caseStringValue(String value, Class<?> clazz) {
        if (clazz == String.class) {
            // 如果本来就是字符串, 直接返回
            return value;
        } else if (clazz == Integer.class) {
            return Integer.valueOf(value);
        } else if (clazz == int.class) {
            return Integer.valueOf(value).intValue();
        } else {
            return null;
        }
    }
}
