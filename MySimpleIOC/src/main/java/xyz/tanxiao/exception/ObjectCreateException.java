package xyz.tanxiao.exception;

import xyz.tanxiao.annotation.NeedInstantiate;

/**
 * 对象创建异常
 *
 * 未成功创建实例对象，抛出此异常。
 *
 * @author 谈笑、
 * @dateTime 2021/2/27 19:25
 */
@NeedInstantiate
public class ObjectCreateException extends Exception {

	public ObjectCreateException() {}

	public ObjectCreateException(String msg) {
		super(msg);
	}

}