package xyz.tanxiao.container;

import xyz.tanxiao.annotation.NeedInstantiate;
import xyz.tanxiao.exception.InstantiateObjectNoExistException;
import xyz.tanxiao.exception.ObjectCreateException;
import xyz.tanxiao.exception.ObjectNameConflictException;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 实例对象容器
 *
 * 为所有带有 @NeedInstantiate 注解的类通过其 Class 类创建实例对象。
 *
 * @author 谈笑、
 * @dateTime 2021/2/27 20:07
 */
public class InstantiateObjectContainer {

	/**
	 * 初始时实例对象容器
	 */
	private static final Map<String, Object> INIT_INSTANTIATE_OBJECT_CONTAINER = new LinkedHashMap<>();

	/**
	 * 获取时实例对象容器
	 */
	private static final Map<String, Object> OBTAIN_INSTANTIATE_OBJECT_CONTAINER = new LinkedHashMap<>();


	/**
	 * 判断是否添加注解
	 *
	 * 判断某个是否类上是否添加了 @NeedInstantiate 注解。
	 *
	 * @param allByteCode Class类实例
	 */
	public static void isNeedInstantiateAnnotation(List<Class> allByteCode) throws Exception {
		if (Objects.requireNonNull(allByteCode).size() > 0) {
			// 遍历所有的 Class 对象，只操作带有 @NeedInstantiate 注解的类。
			for (Class byteCode : allByteCode) {
				if (byteCode.isAnnotationPresent(NeedInstantiate.class) && exclude(byteCode)) {
					// 分发创建任务
					InstantiateObjectContainer.taskDistribution(byteCode,
						(NeedInstantiate) byteCode.getAnnotation(NeedInstantiate.class));
				}
			}
		}
	}


	/**
	 * 排除某些类型
	 *
	 * 排除指定的类型，不为接口、抽象类、内部类、注解和枚举创建实例对象。
	 *
	 * @param byteCode Class对象
	 * @return 排除结果
	 */
	private static boolean exclude(Class byteCode) throws Exception {
		// 接口
		if (byteCode.getModifiers() == 512) {
			throw new ObjectCreateException("@NeedInstantiate无法为接口 " + byteCode.getName()
					+ " 创建实例对象。");
		}
		// 抽象类
		if (byteCode.getModifiers() == 1024 || byteCode.getModifiers() == 1025) {
			throw new ObjectCreateException("@NeedInstantiate无法为抽象类 " + byteCode.getName()
					+ " 创建实例对象。");
		}
		// 内部类
		if (byteCode.isMemberClass()) {
			throw new ObjectCreateException("@NeedInstantiate无法为内部类 " + byteCode.getName()
					+ " 创建实例对象。");
		}
		// 枚举
		if (byteCode.isEnum()) {
			throw new ObjectCreateException("@NeedInstantiate无法为枚举 " + byteCode.getName()
					+ " 创建实例对象。");
		}
		// 注解
		if (byteCode.isAnnotation()) {
			throw new ObjectCreateException("@NeedInstantiate无法为注解 " + byteCode.getName()
					+ " 创建实例对象。");
		}
		return true;
	}


	/**
	 * 分发创建任务
	 *
	 * 通过类实例对象创建时机的不同来分发任务。
	 *
	 * @param byteCode Class类实例
	 * @param needInstantiate NeedInstantiate注解实例
	 */
	private static void taskDistribution(Class byteCode, NeedInstantiate needInstantiate) throws Exception {
		// 获取到指定的对象名
		String objectName = needInstantiate.objectName();
		if ("".equals(objectName)) {
			// 如果没有设置实例对象名，则默认设置为类名的首字母小写。
			objectName = byteCode.getSimpleName().substring(0, 1).toLowerCase() + byteCode.getSimpleName().substring(1);
		} else {
			// 如果设置了实例对象名，还是要设置为类名的首字母小写。
			objectName = objectName.substring(0, 1).toLowerCase() + objectName.substring(1);
		}

		if (needInstantiate.initCreate()) {
			// 初始时创建实例对象
			InstantiateObjectContainer.initInstantiateObject(objectName, byteCode);
		} else {
			// 获取时创建实例对象
			InstantiateObjectContainer.waitInstantiateObject(objectName, byteCode);
		}
	}


	/**
	 * 创建实例对象
	 *
	 * 通过 Class 类创建其对应的实例对象。
	 *
	 * @param objectName 对象名
	 * @param byteCode Class类实例
	 */
	private static void initInstantiateObject(String objectName, Class byteCode) throws Exception {
		/**
		 * 指定该对象在初始时实例对象容器中的新key，用包来取分实例对象的作用范围，
		 * 解决不同包内对象名重名问题。
		 */
		String newName = objectName + "_" + byteCode.getPackage();

		if (InstantiateObjectContainer.INIT_INSTANTIATE_OBJECT_CONTAINER.get(newName) == null) {
			// 将私有构造设置为外部可以访问。
			Constructor constructor = byteCode.getDeclaredConstructor();
			constructor.setAccessible(true);
			// 将该实例对象添加到初始时实例对象容器中。
			InstantiateObjectContainer.INIT_INSTANTIATE_OBJECT_CONTAINER.put(newName, constructor.newInstance());
		} else {
			// 给定的对象名冲突
			throw new ObjectNameConflictException("创建 " + byteCode.getTypeName() + " 实例对象失败，请检查给定的 "
				+ objectName + " 对象名是否冲突。");
		}
	}


	/**
	 * 等待创建实例对象
	 *
	 * 等待通过 Class 类创建其对应的实例对象。
	 *
	 * @param objectName 对象名
	 * @param byteCode Class类实例
	 */
	private static void waitInstantiateObject(String objectName, Class byteCode) throws Exception {
		/**
		 * 指定该对象在初始时实例对象容器中的新key，用包来取分实例对象的作用范围，
		 * 解决不同包内对象名重名问题。
		 */
		String newName = objectName + "_" + byteCode.getPackage();

		if (InstantiateObjectContainer.OBTAIN_INSTANTIATE_OBJECT_CONTAINER.get(newName) == null) {
			InstantiateObjectContainer.OBTAIN_INSTANTIATE_OBJECT_CONTAINER.put(newName, byteCode);
		} else {
			// 给定的对象名冲突
			throw new ObjectNameConflictException("创建 " + byteCode.getTypeName() + " 实例对象失败，请检查给定的 "
					+ objectName + " 对象名是否冲突。");
		}
	}


	/**
	 * 获取和创建对象
	 *
	 * 通过对象名和其字节码获取到匹配的实例对象，如果没有则尝试创建对象。
	 *
	 * @param objectName 获取的对象名
	 * @param byteCode Class字节码
	 * @return 实例对象
	 */
	public static Object getAndSetObject(String objectName, Class byteCode) throws Exception {
		String newName = objectName.substring(0, 1).toLowerCase() + objectName.substring(1)+ "_" + byteCode.getPackage();

		// 尝试通过指定对象名先从初始时实例对象容器中获取指定的对象实例。
		Object object = InstantiateObjectContainer.INIT_INSTANTIATE_OBJECT_CONTAINER.get(newName);

		// 如果成功获取到了，则再判断该对象实例是否与需要获取的实例对象一致。
		if (object != null && object.getClass() == byteCode) {
			return object;
		} else {
			//  尝试从获取时实例对象容器获取到该对象的 Class 类的实例对象。
			Class aClass = (Class) InstantiateObjectContainer.OBTAIN_INSTANTIATE_OBJECT_CONTAINER.get(newName);
			// 如果成功获取到了，则再判断该对象实例是否与需要获取的实例对象一致。
			if (aClass != null && aClass == byteCode) {
				// 将该对象添加初始时实例对象容器中。
				InstantiateObjectContainer.initInstantiateObject(objectName, aClass);
				// 将该待创建的实例对象从获取时实例对象容器移除。
				InstantiateObjectContainer.OBTAIN_INSTANTIATE_OBJECT_CONTAINER.remove(newName);
				// 递归获取
				return InstantiateObjectContainer.getAndSetObject(objectName, byteCode);
			} else {
				return null;
			}
		}
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
	public static Object getInstantiateObject(String objectName, Class byteCode) throws Exception {
		Object object = InstantiateObjectContainer.getAndSetObject(objectName, byteCode);
		if (object != null) {
			return object;
		} else {
			throw new InstantiateObjectNoExistException("获取名为 " + objectName + " 的实例对象失败，请检查该实例对象是否存在。");
		}
	}


	/**
	 * 清除实例对象容器
	 *
	 * @param initObject 清除初始时实例对象容器
	 * @param obtainObject 清除获取时实例对象容器
	 */
	public static void clearObject(boolean initObject, boolean obtainObject) {
		if (initObject) {
			InstantiateObjectContainer.INIT_INSTANTIATE_OBJECT_CONTAINER.clear();
		}
		if (obtainObject) {
			InstantiateObjectContainer.OBTAIN_INSTANTIATE_OBJECT_CONTAINER.clear();
		}
	}


	/**
	 * 返回初始时实例对象容器
	 */
	public static Map<String, Object> getInitInstantiateObjectContainer() {
		return INIT_INSTANTIATE_OBJECT_CONTAINER;
	}


	/**
	 * 返回获取时实例对象容器
	 */
	public static Map<String, Object> getObtainInstantiateObjectContainer() {
		return OBTAIN_INSTANTIATE_OBJECT_CONTAINER;
	}


	/**
	 * *** 待使用 ***
	 */
	public static Object getMatchField(String objectName, Class byteCode) throws Exception {
		Object object = InstantiateObjectContainer.getAndSetObject(objectName, byteCode);
		if (object != null) {
			return object;
		} else {
			return null;
		}
	}

}