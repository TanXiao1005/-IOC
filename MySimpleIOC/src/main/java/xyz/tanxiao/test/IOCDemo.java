package xyz.tanxiao.test;

import xyz.tanxiao.annotation.NeedInstantiate;

/**
 * @author 谈笑、
 * @dateTime 2021/2/28 22:55
 */
@NeedInstantiate(objectName = "iOCDemo", initCreate = true)
public class IOCDemo {

	public int sum(int a, int b) {
		return a + b;
	}

}