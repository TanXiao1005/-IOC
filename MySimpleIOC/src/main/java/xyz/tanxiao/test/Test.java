package xyz.tanxiao.test;

import xyz.tanxiao.dispatch.MainDispatcher;

/**
 * @author 谈笑、
 * @dateTime 2021/2/28 22:55
 */
public class Test {

	public static void main(String[] args) throws Exception {

		// 扫描包，创建实例对象。
		MainDispatcher mainDispatcher = MainDispatcher.packageScanner("xyz.tanxiao");
		// 获取到创建的实例对象。
		IOCDemo iocDemo = (IOCDemo) mainDispatcher.getInstantiateObject("iOCDemo", IOCDemo.class);
		// 调用该类的实例方法。
		System.out.println(iocDemo.sum(100, 200));

	}

}