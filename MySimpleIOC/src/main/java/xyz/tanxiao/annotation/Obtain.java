package xyz.tanxiao.annotation;

import java.lang.annotation.*;

/**
 * 获取匹配的实例对象
 *
 * 在指定的属性上添加此注解，获取到匹配的实例对象，并将该对象注入
 * 到指定的属性中。
 *
 * 1.先通过对象名来获取到实例对象，再通过字节码对象进行对比。
 * 2.如果存在继承或实现关系，则
 *
 * @author 谈笑、
 * @dateTime 2021/2/26 14:34
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Obtain {

}