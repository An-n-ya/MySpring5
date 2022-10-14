package host.ankh.mySpring.web.aspect;

import host.ankh.mySpring.aop.aspect.MyJoinPoint;

import java.util.Arrays;

/**
 * @author ankh
 * @created at 2022-10-12 21:11
 */
public class LogAspect {
    public void before(MyJoinPoint joinPoint) {
        // 前置拦截
        joinPoint.setUserAttribute("startTime_" + joinPoint.getMethod().getName(), System.currentTimeMillis());
        System.out.println("Invoker Before Method!" +
                "\nTargetObject: " + joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
    }

    public void after(MyJoinPoint joinPoint) {
        System.out.println("Invoker After Method!" +
                "\nTargetObject: " + joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
        long startTime = (Long) joinPoint.getUserAttribute("startTime_" + joinPoint.getMethod().getName());
        long endTime = System.currentTimeMillis();
        System.out.println("use time: " + (endTime - startTime) + "ms");
    }

    public void afterThrowing(MyJoinPoint joinPoint, Throwable ex) {
        System.out.println("Exception!" +
                "\nTargetObject: " + joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()) +
                "\nThrows:" + ex.getMessage());
    }
}
