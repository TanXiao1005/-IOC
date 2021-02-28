package xyz.tanxiao.injection;

import xyz.tanxiao.annotation.Obtain;
import xyz.tanxiao.container.ByteCodeContainer;
import xyz.tanxiao.container.InstantiateObjectContainer;
import xyz.tanxiao.container.QualifiedNameContainer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * 属性注入
 *
 * *** 待实现 ***
 *
 * 为所有添加了 @Obtain 注解成员属性注入匹配值。
 *
 * @author 谈笑、
 * @dateTime 2021/2/28 0:01
 */
public class FieldInjection {

	public static void isObtainAnnotation(String packageName) throws Exception {
		// 获取到项目根包名
		String rootPackageName = packageName.split("\\.")[0];
		// 获取到项目所有类的全限定类名
		List<String> allQualifiedName = QualifiedNameContainer.getQualifiedName(rootPackageName);
		// 获取到项目所有的 Class 对象
		List<Class> allByteCode = ByteCodeContainer.getByteCode(allQualifiedName);

		// 遍历每一个类中的成员属性，为所有添加了 @Obtain 注解成员属性注入匹配值。
		for (Class byteCode : Objects.requireNonNull(allByteCode)) {
			for (Field field : byteCode.getDeclaredFields()) {
				if (field.isAnnotationPresent(Obtain.class)) {
					field.setAccessible(true);
					Object object = InstantiateObjectContainer.getMatchField(field.getName(), field.getType());
					if (object != null) {
						Object main = InstantiateObjectContainer.getAndSetObject(byteCode.getSimpleName(), byteCode);
						if (main != null) {
							field.set(main, object);
						} else {
							field.set(byteCode.newInstance(), object);
						}
					}
				}
			}
		}

	}

}