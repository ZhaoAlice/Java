# Java
The note for java learning
[openjdk](https://openjdk.org/projects/jdk/10/)
# jdk9
# jdk10
# jdk11
## 17个特性
### ZGC垃圾回收期 最重要的特性 我想这也是jdk使用量超过jdk8的本质原因
### Epsilon垃圾收集器
### optional
### collection
### stream
### HttpClient
### NestClass
### File readString writeString
### var
### Predicate
### String

# jdk12
## 8大特性
### 添加Shenandoah:一个低暂停时间的垃圾收集器(实验特性)
  GC暂停时间与堆大小无关
### JMH，即Java Microbenchmark Harness，是专门用于代码微基准测试的工具套件
  方法层面的性能优化
  * 接口不同实现在给定条件下的吞吐量
  * 准确知道某个方法的执行时间，执行时间与输入之间的相关性
### switch表达式
  * 返回值
  * lamda表达式
### JVM Constants API
### 只保留一个AArch64端口
### 默认CDS存档(默认生成类数据共享(CDS)归档文件)
### 可中断的G1 Mixed GC
### G1增强，自动返回未用堆内存给操作系统
### String transform()和indent()
### Files类中新增mismatch()
### 支持unicode11和压缩数字格式化

# jdk13
## 5个特性 
### 对jdk12中switch表达式的二次预览
### TextBlocks
### 动态CDS档案（动态类数据共享归档）
* JVM退出时动态创建共享归档文件：导出jsa
  `java -XX:ArchiveClassesAtExit=hello.jsa -cp hello.jar Hello`
*  用动态创建的共享归档文件运行应用:使用jsa
  `java -XX:SharedArchiveFile=hello.jsa -cp hello.jar Hello`
### ZGC取消提交未使用的内存(实验特性)
### 重新实现旧版套接字API
  `PlainSocketImpl` 通过`jdk.net.usePlainSocketImpl`来切换到旧版本

# jdk14
## 16个特性
### instanceOf 预览特性
类型判断的同时支持类型转换
### 空指针异常
* 空指针异常会打印出是哪个对象导致的
* 在jdk15中默认开启，在jdk14中需要配置虚拟机参数`-XX:+ShowCodeDetailsInExceptionMessages`
### record语法 预览特性
### switch标准化
在jdk12和jdk13中新增的相关特性 确定使用
### TextBlocks特性 二次预览
  * 添加\:表示取消换行操作
  * 添加\s:表示一个空格
### 弃用ParallelScavenge + SerialOld GC组合
使用少 维护工作大
### 删除CMS垃圾回收器
  * 会产生内存碎片，导致并发清除后，用于线程可用的空间不足

# jdk15
## 14个特性 其中有个重要特性
### 密封类 sealed classes
用于类的使用，密封的类和接口限制其他可能实现他们的类和接口
声明一个类是sealed类时，使用permits限制可以被继承的子类，子类继承被sealed修饰的父类时，必须使用关键字，final/sealed/non-sealed
进行二次修饰，做二次声明
final： 不可再被继承
sealed: 继续声明为密封类
non-sealed: 该类解除密封限制
### 隐藏类hidden classes
不能直接使用的类，只能通过反射机制来调用
### 模式匹配instanceOf（二次预览）
特性细节同JDK14
### ZGC功能作为正式特性
默认GC仍然为G1，只需要通过-XX:+UseZGC就可以启用
### TextBlocks作为正式特性
特性细节同JDK14
### Record特性二次预览