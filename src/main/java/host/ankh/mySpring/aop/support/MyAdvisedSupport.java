package host.ankh.mySpring.aop.support;

import host.ankh.mySpring.aop.advice.MyAfterReturningAdvice;
import host.ankh.mySpring.aop.advice.MyAfterThrowingAdvice;
import host.ankh.mySpring.aop.advice.MyMethodBeforeAdvice;
import host.ankh.mySpring.aop.aspect.MyAopConfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用来完成对aop配置的解析和封装
 * @author ankh
 * @created at 2022-10-14 19:13
 */
public class MyAdvisedSupport {
    private Class targetClass;
    private Object target;
    private Pattern pointCutClassPattern;

    private transient Map<Method, List<Object>> methodCache;

    private MyAopConfig config;

    public MyAdvisedSupport(MyAopConfig config) {
        this.config = config;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public Object getTarget() {
        return target;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws Exception {
        List<Object> cached = methodCache.get(method);
        if (cached == null) {
            // 如果缓存未命中
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            cached = methodCache.get(m);
            // 存入缓存
            this.methodCache.put(m, cached);
        }
        return cached;
    }

    public boolean pointCutMatch() {
        return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }

    private void parse() {
        String pointCut = config.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");
        String pointCutForClass = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " +
                pointCutForClass.substring(pointCutForClass.lastIndexOf(" ") + 1));

        methodCache = new HashMap<Method, List<Object>>();
        Pattern pattern = Pattern.compile(pointCut);

        try {
            Class aspectClass = Class.forName(config.getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<String, Method>();
            for (Method m : aspectClass.getMethods()) {
                aspectMethods.put(m.getName(), m);
            }
            for (Method m : targetClass.getMethods()) {
                String methodString = m.toString();
                if (methodString.contains("throws")) {
                    methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
                }
                Matcher matcher = pattern.matcher(methodString);
                if (matcher.matches()) {
                    // 如果方法名满足切面规则，则添加到AOP配置中
                    List<Object> advices = new LinkedList<Object>();
                    // 前置通知
                    String before = config.getAspectBefore();
                    if(!(null == before || "".equals(before.trim()))) {
                        Method aspectMethod = aspectMethods.get(before);
                        advices.add(new MyMethodBeforeAdvice(aspectMethod, aspectClass.newInstance()));
                    }
                    // 后置通知
                    String after = config.getAspectAfter();
                    if(!(null == after || "".equals(after.trim()))) {
                        Method aspectMethod = aspectMethods.get(after);
                        advices.add(new MyAfterReturningAdvice(aspectMethod, aspectClass.newInstance()));
                    }
                    // 异常通知
                    String expt = config.getAspectAfterThrow();
                    if(!(null == expt || "".equals(expt.trim()))) {
                        Method aspectMethod = aspectMethods.get(expt);
                        var afterThrowingAdvice = new MyAfterThrowingAdvice(aspectMethod, aspectClass.newInstance());
                        afterThrowingAdvice.setThrowingName(config.getAspectAfterThrowingName());
                        advices.add(afterThrowingAdvice);
                    }
                    methodCache.put(m, advices);;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
