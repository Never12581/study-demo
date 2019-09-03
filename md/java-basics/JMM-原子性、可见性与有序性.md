####  原子性

由java内存模型来直接保证的原子性变量包括read、load、assign、use、store、write；基本类型的读写是具备原子性的（一般会将long和double实现成原子性读写）

如果应用场景需要更大范围的原子性保证，java内存模型中还提供了lock和unlock操作来满足这种需求。虚拟机未把lock和unlock操作直接开放给用户，提供了更高层次的字节码指令monitorenter和monitorexit来隐式地使用这两个操作。synchtonized关键字之间的操作也具有原子性

#### 可见性

可见性是指某个线程修改了共享变量的值，其他线程能够立即得知这个修改。

除了volatile之外，还有synchronized和final也可以实现可见性。final关键字是首次赋值后不可以再修改，自然遵守可见性；synchronized关键字是一个变量执行unlock操作时，必须把此变量同步到主内存中。

#### 有序性

volatile和synchronized两个关键字可以保证线程之间的有序性。volatile是禁止指令重排序，synchronized是因为lock与unlock操作，保证了持有同个锁的两个同步快只能串行进入







#### 先行发生原则

todo

简单来说

```java
public class Test{
  public static void main(String[] args){
    int a = 0 ; 
    int b = a + 10 ;
  }
}
```

在这段代码中，b = a+10一定滞后于a = 0执行，因为其依赖于a。