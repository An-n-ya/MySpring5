package host.ankh.mySpring.annotation;

import java.lang.annotation.*;

/**
 * @author ankh
 * @created at 2022-03-09 11:35 AM
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestParam {
    String value() default "";
}
