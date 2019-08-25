## Class类文件结构

Class文件是一组以8位字节为基础的二进制流，哥哥数据项目严格按照顺序紧凑地排序在Class文件之中，中间没有添加任何分割符，没有空隙存在。当遇到需要占用8位字节以上克难攻坚的数据项时，则会按照高位在前的方式分割成若干个8位字节进行存储。

Class文件格式采用一种类C语言结构题的伪结构来存储数据，这种结构中只有两种数据类型：无符号数和表。

> - 无符号数属于基本数据类型，以u1，u2，u3，u4来分别代表1个字节，2个字节，4个字节，8个字节的无符号数，无符号数可以用来描述数字，索引引用，数量值或者按照UTF-8表吗构成字符串值
> - 表是由多个无符号数或者其他表作为数据项构成的复合数据类型，所有表都习惯地以“_info"结尾。表用于描述有层次关系的复合结构的数据，整个Class文件本质上就是一张表

| 类型          | 名称                | 数量                  |
| ------------- | ------------------- | --------------------- |
| u4            | magic               | 1                     |
| u2            | minor_version       | 1                     |
| u2            | major_version       | 1                     |
| u2            | constant_pool_count | 1                     |
| cp_info       | constants_pool      | constant_pool_count-1 |
| u2            | access_flags        | 1                     |
| u2            | this_class          | 1                     |
| u2            | super_class         | 1                     |
| u2            | interfaces_count    | 1                     |
| u2            | interfaces          | interfaces_count      |
| u2            | fidlds_count        | 1                     |
| field_info    | fields              | fields_count          |
| u2            | methods_count       | 1                     |
| methods_info  | methods             | methods_count         |
| u2            | attributes_count    | 1                     |
| attrbute_info | attrbutes           | attrbutes_count       |

无论是无符号数还是表，当需要描述同一类型但是数量不定的多个数据时，经常会使用一个前置的容量计数器家若干个连续的数据项的形式，这时称这一系列连续的某一个类型的数据为某一类型的集合。

### 魔数与Class文件的版本

#### 魔数

每个class文件的4个字节称为魔数（Magic Number），唯一作用时确定这个文件是否为一个能被虚拟机接受的Class文件。很多文件存储标准中都适用魔数来进行身份识别，譬如图片格式png，gif等在文件头中都存在魔数。使用魔数而不是用扩展名主要是基于安全方面的考虑，因为文件扩展名可以被随意改动。

Class文件的魔数为：0xCAFEBABE

#### 版本号

紧接着魔数的4个字节存储的是Class文件的版本号：第5和第6两个字节是次版本号（Minor Version），第7和第8个字节是主版本号（Major Version）。

java的版本号是从45开始的，JDK1.1之后每个JDK大版本发布主版本号向上加1，高版本JDK能向下兼容运行以前版本的Class文件。

### 常量池

紧接着主次版本号之后的是常量池入口，常量池可以理解为Class文件之中的资源仓库，它是Class文件结构中与其他项目关联最多的数据类型，也是占用Class文件空间最大的数据项目之一，同时它还是Class文件中第一个出现的表类型数据项目。

由于常量池中的常量的数量是不固定，所以在常量池入口需要放置一项u2类型的数据，代表常量池容量计数值（constant_pool_count）。这个容量计数值是从1开始计数的而非0。若常量池容量为0x0016，即十进制中的22，这就代表着常量池中有21项常量，索引值范围在1～21。

> todo：为理解
>
> 在Class文件格式规范制定时，设计者将第0项常量空出来时有特殊考虑的，这样做的目的在于满足后哦面某些指向常量池的索引值的数据在特定情况下需要表达“不引用任何一个常量池项目”的含义，这种情况就可以把索引值置为0来表示。

常量池中主要存放两大常量：字面量（Literal）和符号引用（Symbolic Reference）。

- 字面量比较接近Java语言层面的常量概念，如文本字符串，声明为final的常量值等
- 符号引用则属于编译原理方面等概念：
  - 类和接口等全限定名（Fully Qualified Name）
  - 字段的名称和描述符（Descriptor）
  - 方法的名称和描述符

常量池中每一项常量都是一个表，共有14项，如下表：

| 类型                            | 标志 | 描述                     |
| :------------------------------ | ---- | ------------------------ |
| CONSTANT_Utf-8_info             | 1    | UTF-8编码的字符串        |
| CONSTANT_Integer_info           | 3    | 整型字面量               |
| CONSTANT_Float_info             | 4    | 浮点型字面量             |
| CONSTANT_Long_info              | 5    | 长整型字面量             |
| CONSTANT_Double_info            | 6    | 双精度浮点型字面量       |
| CONSTANT_Class_info             | 7    | 类或者接口的符号引用     |
| CONSTANT_String_info            | 8    | 字符串类型字面量         |
| CONSTANT_Fieldref_info          | 9    | 字段的符号引用           |
| CONSTANT_Methodref_info         | 10   | 类中方法的符号引用       |
| CONSTNT_InterfaceMethodref_info | 11   | 接口中方法符号引用       |
| CONSTANT_NameAndType_info       | 12   | 字段或方法的部分符号引用 |
| CONSTANT_MethodHandle_info      | 15   | 表示方法句柄             |
| CONSTANT_MethodType_info        | 16   | 标识方法类型             |
| CONSTANT_InvokeDynamic_info     | 18   | 表示一个动态方法调用点   |

之所以说常量池繁琐，是因为这14种类型都有个自己的结构，且均不同。

todo：待续

### 访问标志

在常量池结束后，紧接着两个字节代表着访问标志（access_flags），这个标志用于识别一些类或者接口层次的访问信息，包括：这个Class时类还时接口；是否定义为public类型；是否时abstract类型；如果时类是否被声明为final等，具体标志位预计标志位含义见下表

| 标志名称      | 标志值 | 含义                                                         |
| ------------- | ------ | ------------------------------------------------------------ |
| ACC_PUBLIC    | 0x0001 | 是否为public类型                                             |
| ACC_FINAL     | 0x0010 | 是否被声明为final，只有类可设置                              |
| ACC_SUPER     | 0x0020 | 是否允许使用invokespecial字节码指令的新语意，invokerspecial指令的语意在jdk 1.0.2发生过改变，为了区别这条指令使用哪种予以，jdk1.0.2之后编译出来的类的这个标志都必须为真 |
| ACC_INTERFACE | 0x0200 | 标识这是一个接口                                             |
| ACC_ABSTRACT  | 0x0400 | 是否为abstract类型，对于接口与抽象类来说，此标志值为真，其他类值为假 |
| ACC_SYNTHETIC | 0x1000 | 标识这个类非用户代码产生的                                   |
| ACC_ANNOTATIO | 0x2000 | 标识这是一个注解                                             |
| ACC_ENUM      | 0x4000 | 标识这是一个枚举                                             |

access_flags中一共有16个标志位可用（u2类型），当前指定医疗其中8个，没有使用到的标识为一律为0。

### 类索引、父类索引与接口索引集合

类索引（this class）和父类索引（super class）都是一个u2类型的数据，而接口索引集合（integerfaces）是一组u2类型的数据的集合，Class文件中由这三项数据来确定这个类的继承关系。

- 类索引：用于确定该类全限定名
- 父类索引：用于确定该类的父类的全限定名，出了Object对象外，所有java类的父类索引都不为0
- 接口索引集合：描述了这个类实现了哪些接口，按implements（如果本身是接口则为extends）后的顺序从左到右排列在索引集合中

类索引，父类索引和接口索引集合都按顺序排列在访问标志之后，类索引和父类索引引用两个u2类型的索引值表示，它们各自指向一个类型为CONSTANT_Class_info的类描述符常量，通过CONSTANT_Class_info的常量值中的索引值可以找到定义在CONSTANT_Utf-8_info 类型的常量中的全限定名 字符串。

### 字段表的集合

字段表（field_info）用于描述接口或者类中声明的变量。字段（field）包括类级变量以及实例级变量，不包括方法内部声明的局部变量。

### 方法表集合

### 属性表集合

