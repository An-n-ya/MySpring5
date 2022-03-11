package host.ankh.mySpringMVC.annotation;

import java.lang.annotation.*;

/**
 * @author ankh
 * @created at 2022-03-09 11:35 AM
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyService {
    String value() default "";
}
