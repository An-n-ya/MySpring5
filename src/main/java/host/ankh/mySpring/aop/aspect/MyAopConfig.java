package host.ankh.mySpring.aop.aspect;

import lombok.Data;

/**
 * AOP的配置封装
 * @author ankh
 * @created at 2022-10-12 21:18
 */
@Data
public class MyAopConfig {
    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName;
}
