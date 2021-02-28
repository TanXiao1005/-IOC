### MySimpleIOC ###

### 一、主要类和注解

#####  (一) @NeedInstantiate注解

- objectName属性：
  - 指定创建实例对象的名称。
- initCreate属性：
  - 是否在项目初始化时就创建该类的实例对象。

##### (二) MainDispatcher类

- packageScanner方法：
  - 扫描指定的包内的所有添加了@NeedInstantiate注解的类。
- getInstantiateObject方法：
  - 获取指定名称的实例对象。
- getContainerInfo方法：
  - 获取初始时实例对象容器和获取时实例对象容器容器信息。



### 二、使用说明

##### (一) @NeedInstantiate注解的使用 

- 在指定需要自动创建实例对象的类之上添加 @NeedInstantiate 注解，可以选择性指定该实例对象的名称以及该实例对象的创建时机。

#####  (二) MainDispatcher类的使用

- **packageScanner() 方法：**
  - 使用MainDispatcher类调用其 packageScanner() 方法扫描指定包内的所有类，为添加了@NeedInstantiate注解的类创建实例对象。               
- **getInstantiateObject() 方法：**
  - 使用MainDispatcher类的实例对象调用其 getInstantiateObject() 方法获取指定名称的实例对象。
- **getContainerInfo() 方法：**
  - 使用MainDispatcher类的实例对象调用其 getContainerInfo() 方法获取初始时实例对象容器和获取时实例对象容器容器信息。



### 三、操作示例

##### (一) 需要创建实例对象类

- ```java
  @NeedInstantiate()
  // @NeedInstantiate(objectName = "iOCDemo", initCreate = true)
  // @NeedInstantiate(objectName = "iOCDemo", initCreate = false)
  public class IOCDemo {
      public int sum(int a, int b) {
     		return a + b;
      }
  }
  ```

  

##### (二) 获取创建的实例对象

- ```java
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
  ```
