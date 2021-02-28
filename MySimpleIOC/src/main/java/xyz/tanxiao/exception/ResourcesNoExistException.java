package xyz.tanxiao.exception;

import xyz.tanxiao.annotation.NeedInstantiate;

/**
 * 资源不存在异常
 *
 * 指定的资源不存在，抛出此异常。
 *
 * @author 谈笑、
 * @dateTime 2021/2/27 19:25
 */
@NeedInstantiate(initCreate = false)
public class ResourcesNoExistException extends Exception {

	public ResourcesNoExistException() {}

	public ResourcesNoExistException(String msg) {
		super(msg);
	}

}