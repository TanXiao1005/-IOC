package xyz.tanxiao.exception;

import xyz.tanxiao.annotation.NeedInstantiate;

/**
 * 包不存在异常
 *
 * 指定的包不存在，抛出此异常。
 *
 * @author 谈笑、
 * @dateTime 2021/2/27 19:25
 */
@NeedInstantiate(initCreate = false)
public class PackageNoExistException extends Exception {

	public PackageNoExistException() {}

	public PackageNoExistException(String msg) {
		super(msg);
	}

}