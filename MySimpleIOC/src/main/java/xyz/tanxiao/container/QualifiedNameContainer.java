package xyz.tanxiao.container;

import xyz.tanxiao.exception.PackageNoExistException;
import xyz.tanxiao.exception.ResourcesNoExistException;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 全限定名容器
 *
 * 获取指定包内的所有类的全限定名，例如xx.xx.Xxx。
 *
 * @author 谈笑、
 * @dateTime 2021/2/27 18:44
 */
public class QualifiedNameContainer {

	/**
	 * 全限定名容器，储存扫描包下的所有类的全限定名。
	 */
	private static final List<String> QUALIFIED_NAME_CONTAINER = new ArrayList<>();


	/**
	 * 扫描包，获取全限定类名。
	 *
	 * 扫描包指定包下的所有类文件，并获取到该类的全限定类名。
	 *
	 * @param packageName 扫描的包名
	 * @return 全限定类名数据
	 */
	public static List<String> getQualifiedName(String packageName) throws Exception {
		if (packageName == null || "".equals(packageName.trim())) {
			throw new PackageNoExistException("包扫描失败，指定扫描的包不存在。");
		}

		// 将包名的小数点分隔符转为通用路径分隔符
		String changeSign = packageName.replace(".", File.separator);
		// 通过当前线程的类加载找到指定包的文件路径
		URL resourcePath = Thread.currentThread().getContextClassLoader().getResource(changeSign);

		if (resourcePath != null) {
			// 获取该路径下的所有目录和文件，遍历每一个文件和目录。
			File[] files = new File(resourcePath.toURI()).listFiles();
			for (File file : Objects.requireNonNull(files)) {
				QualifiedNameContainer.ergodicFiles(file, packageName);
			}
			// 全限定名数据去重，避免产生的重复的数据。
			List<String> qualifiedNameData = QualifiedNameContainer.QUALIFIED_NAME_CONTAINER.stream()
				.distinct().collect(Collectors.toList());
			return qualifiedNameData;
		} else {
			throw new ResourcesNoExistException("资源获取失败，请检查资源路径是否正确。");
		}
	}


	/**
	 * 遍历目录
	 *
	 * 遍历每一个文件夹，获取到该文件夹内的所有类全限定名，并将该类全限
	 * 定名添加到 QUALIFIED_NAME_CONTAINER 限定名容器中。
	 *
	 * @param file 子文件
	 * @param packageName 包名
	 */
	private static void ergodicFiles(File file, String packageName) {
		// 判断传入的File是否是文件夹，如果是文件夹并且文件内有文件，则继续递归遍历。
		if(Objects.requireNonNull(file).isDirectory() && Objects.requireNonNull(file.listFiles()).length > 0) {
			for(File f : Objects.requireNonNull(file.listFiles())) {
				ergodicFiles(f, packageName);
			}
		} else {
			// 获取编译后的字节码文件名，文件名以 .class 结尾。
			if(file.getName().endsWith(".class")) {
				// 将包名的通用文件路径分隔符转为小数点分隔符
				String byteCodePath = file.getPath().replace(File.separator, ".");
				// 获取字节码文件路径的长度
				int length = byteCodePath.length();
				// 获取字节码文件路径中，包名开始的位置。
				int state = byteCodePath.lastIndexOf(packageName);
				// 截取字节码文件路径中的包名和类名，并去除.class拓展名。
				String qualifiedName = byteCodePath.substring(state, length).replace(".class", "");
				// 将字节码的全限定类名添加到容器中
				QualifiedNameContainer.QUALIFIED_NAME_CONTAINER.add(qualifiedName);
			}
		}
	}


	/**
	 * 清空容器
	 *
	 * 清空容器中的所有全限定名数据。
	 */
	public static void clear() {
		QualifiedNameContainer.QUALIFIED_NAME_CONTAINER.clear();
	}

}