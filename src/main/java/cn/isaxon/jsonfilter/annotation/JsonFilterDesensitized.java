package cn.isaxon.jsonfilter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Jackson filter the Java-Type corresponding the fieldname</p>
 * <p>Copyright:@isaxon.cn</p>
 *
 * @author saxon/isaxon
 * Create 2018-04-27 下午7:59 By isaxon
 */
@Target(ElementType.METHOD)  // 注解只能用于方法
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时依然保留
public @interface JsonFilterDesensitized
{
    ClassFieldFilter[] value() default {}; // 对多个类型进行字段过滤

    @interface ClassFieldFilter
    {
        Class<?> type(); // 要过滤的字段类型, 如User.class

        // 其中约定include和exclude只能一个有效。
        String[] include() default {}; // 不需要过滤的字段，只留下这些。

        String[] exclude() default {}; // 需要过滤的字段。
    }
}