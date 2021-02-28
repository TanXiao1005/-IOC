package xyz.tanxiao.exception;

import xyz.tanxiao.annotation.NeedInstantiate;

/**
 * 实例对象不存在异常
 *
 * 实例对象不存在，抛出此异常。
 *
 * @author 谈笑、
 * @dateTime 2021/2/27 19:25
 */
@NeedInstantiate
public class InstantiateObjectNoExistException extends Exception {

	public InstantiateObjectNoExistException() {}

	public InstantiateObjectNoExistException(String msg) {
		super(msg);
	}

}