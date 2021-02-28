package xyz.tanxiao.dispatch;

import xyz.tanxiao.container.ByteCodeContainer;
import xyz.tanxiao.container.InstantiateObjectContainer;
import xyz.tanxiao.container.QualifiedNameContainer;

import java.util.List;
import java.util.Map;

/**
 * 中央调度器
 *
 * 调度各类功能，包扫描、全限定名获取、Class对象生成、获取指定对象的实例等。
 *
 * @author 谈笑、
 * @dateTime 2021/2/27 18:36
 */
public class MainDispatcher {

	private volatile static MainDispatcher mainDispatcher;


	private MainDispatcher() {}


	/**
	 * 返回 MainDispatcher 类的单例对象。
	 */
	private static MainDispatcher getMainDispatcher() {
		if (MainDispatcher.mainDispatcher == null) {
			synchronized (MainDispatcher.class) {
				if (MainDispatcher.mainDispatcher == null) {
					MainDispatcher.mainDispatcher =  new MainDispatcher();
				}
			}
		}
		return MainDispatcher.mainDispatcher;
	}


	/**
	 * 包扫描器
	 *
	 * 扫描指定的包，并为该包下所有添加@NeedInstantiate注解的类创
	 * 建实例对象。
	 *
	 * @param packageName 扫描的包名
	 * @return MainDispatcher类实例对象
	 */
	public static MainDispatcher packageScanner(String packageName) throws Exception {
		// 获取到扫描包下的所有类的全限定类名。
		List<String> allQualifiedName = QualifiedNameContainer.getQualifiedName(packageName);
		// 将所有的全限定类名创建并实例其Class类对象。
		List<Class> allByteCode = ByteCodeContainer.getByteCode(allQualifiedName);

		// 为所有 Class 类创建其实例对象。
		InstantiateObjectContainer.isNeedInstantiateAnnotation(allByteCode);

		// 清空容器数据
		QualifiedNameContainer.clear();
		ByteCodeContainer.clear();

		// 为所有属性注入匹配值
		// FieldInjection.isObtainAnnotation(packageName);

		return MainDispatcher.getMainDispatcher();
	}


	/**
	 * 获取实例对象
	 *
	 * 通过对象名和其字节码获取到匹配的实例对象。
	 *
	 * @param objectName 获取的对象名
	 * @param byteCode Class字节码
	 * @return 实例对象
	 */
	public Object getInstantiateObject(String objectName, Class byteCode) throws Exception {
		return InstantiateObjectContainer.getInstantiateObject(objectName, byteCode);
	}


	/**
	 * 获取初始时实例对象容器和获取时实例对象容器容器信息。
	 */
	public void getContainerInfo() {
		System.out.println("|== 1.初始时实例对象容器 =======================================================================|");
		System.out.println(" => 容器内实例对象数量：" + InstantiateObjectContainer
				.getInitInstantiateObjectContainer().size() + " 个");
		for (Map.Entry<String, Object> init : InstantiateObjectContainer
				.getInitInstantiateObjectContainer().entrySet()) {
			System.out.println(" => K --> 对象名称: " + init.getKey());
			System.out.println("    V --> 实例对象: " + init.getValue());
		}
		System.out.println("|=============================================================================================|");


		System.out.println("\n" + "|== 2.获取时实例对象容器 =======================================================================|");
		System.out.println(" => 容器内Class对象数量：" + InstantiateObjectContainer
				.getObtainInstantiateObjectContainer().size() + " 个");
		for (Map.Entry<String, Object> init : InstantiateObjectContainer
				.getObtainInstantiateObjectContainer().entrySet()) {
			System.out.println(" => K --> 预定对象名称: " + init.getKey());
			System.out.println("    V --> Class实例对象: " + init.getValue());
		}
		System.out.println("|=============================================================================================|" + "\n");
	}


	/**
	 * 清除实例对象容器
	 *
	 * @param initObject 清除初始时实例对象容器
	 * @param obtainObject 清除获取时实例对象容器
	 */
	public void clear(boolean initObject, boolean obtainObject) {
		InstantiateObjectContainer.clearObject(initObject, obtainObject);
	}

}