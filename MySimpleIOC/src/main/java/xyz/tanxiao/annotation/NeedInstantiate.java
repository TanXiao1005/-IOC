package xyz.tanxiao.annotation;

import java.lang.annotation.*;

/**
 * 实例化注解
 *
 * 在指定的类上添加此注解即可创建实例对象(不包括内部类)，通过objectName属性
 * 设置实例对象的对象名，如果不指定该属性的值，则默认值为该类名的首字母小写。
 *
 * 可以通过initCreate属性值来设置创建实例对象的方式；initCreate为true在类初
 * 始化时就创建实例对象。initCreate为false在获取指定类对象时才创建该类的实例
 * 对象。
 *
 * 该注解只能作用于普通类上，不能直接作用于接口和抽象类，并且不能作用于内部类
 * 、枚举和注解。
 *
 * @author 谈笑、
 * @dateTime 2021/2/26 14:24
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedInstantiate {

	/**
	 * 实例对象名，不设置则设置为类名首字母小写。
	 */
	String objectName() default "";

	/**
	 * 是否在初始化类初始化时创建该类的实例对象，默认为true。
	 */
	boolean initCreate() default true;

}