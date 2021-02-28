package xyz.tanxiao.exception;

import xyz.tanxiao.annotation.NeedInstantiate;

/**
 * 对象名冲突异常
 *
 * 对象名冲突，抛出此异常。
 *
 * @author 谈笑、
 * @dateTime 2021/2/27 19:25
 */
@NeedInstantiate
public class ObjectNameConflictException extends Exception {

	public ObjectNameConflictException() {}

	public ObjectNameConflictException(String msg) {
		super(msg);
	}

}