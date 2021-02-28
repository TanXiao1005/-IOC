package xyz.tanxiao.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 字节码容器
 *
 * 通过全限定类名来创建其对应的Class实例。
 *
 * @author 谈笑、
 * @dateTime 2021/2/27 20:08
 */
public class ByteCodeContainer {

	/**
	 * Class类对象容器，储存扫描包下的所有全限定名创建的Class实例对象。
	 */
	private static final List<Class> BYTE_CODE_OBJECT_CONTAINER = new ArrayList<>();


	/**
	 * 实例化Class类
	 *
	 * 通过每一个类的全限定名创建并实例化其Class类对象。
	 *
	 * @param allQualifiedName 所有的全限定名
	 * @return Class容器
	 */
	public static List<Class> getByteCode(List<String> allQualifiedName) throws Exception {
		if (Objects.requireNonNull(allQualifiedName).size() > 0) {
			/* 遍历所有的全限定名，创建其Class实例对象，添加到 BYTE_CODE_OBJECT_CONTAINER
			   Class类对象容器中。*/
			for (String qualifiedName : allQualifiedName) {
				ByteCodeContainer.BYTE_CODE_OBJECT_CONTAINER.add(Class.forName(qualifiedName));
			}
			return ByteCodeContainer.BYTE_CODE_OBJECT_CONTAINER;
		}
		return null;
	}


	/**
	 * 清空容器
	 *
	 * 清空容器中的所有Class对象数据。
	 */
	public static void clear() {
		ByteCodeContainer.BYTE_CODE_OBJECT_CONTAINER.clear();
	}

}