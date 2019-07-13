### JDK1.8 LongAdder源码分析

- Striped64

  ```java
  abstract class Striped64 extends Number {
    
      /** 当前环境cpu核心数量 */
      static final int NCPU = Runtime.getRuntime().availableProcessors();
  
      /**
       * cell对象的数组，大小为2的幂次方
       * 在对base值进行cas失败后，会根据当前的线程探针，hash到对应的桶里（即计算出下标），
  		 * 对桶里的cell对象进行CAS
       */
      transient volatile Cell[] cells;
  
      /**
       * 基值
       * 当不存在争用的时候，直接对该值进行cas
       * 当cells数组在进行扩容的时候，进行计算时也是用当前值进行cas
       */
      transient volatile long base;
  
      /**
       * cellsBusy为0时 才可以进行初始化或者扩容等操作
       */
      transient volatile int cellsBusy;
    
      /**
       * Cell类
       * 其中value值由volatile修饰，保证其可见行
       * 类使用@Contended修饰，防止伪共享现象出现（在启动命令中加上-XX:-RestrictContended才会生效）
       * 
       * 伪共享：
  		 *		1、在java中使用volatile修饰的对象保证了可见性，
  		 *		2、cpu在将内存中的数据读取到cpu缓存中使用的是cacheline（64B）的策略（时间局部性、空间局部性）
  		 * 		3、数组时连续的内存块。
  		 *		4、long类型占据8B。
  		 *		当两个线程将相邻的两个cell对象以同个cacheline加载进不同的cpu高速缓存，
  		 *		其中一个线程将一个cell（A）对象进行了更新，
  		 *		此时另一个线程对cell（B）进行更新，却发现当前对象所在的cacheline是脏的，
  		 *		需要重新从内存中加载这个cacheline，造成耗时。
       */
      @sun.misc.Contended static final class Cell {
            volatile long value;
            Cell(long x) { value = x; }
            final boolean cas(long cmp, long val) {
                return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
            }
  
            // Unsafe mechanics
            private static final sun.misc.Unsafe UNSAFE;
        		// value对象在当前类中的偏移量
            private static final long valueOffset;
            static {
                try {
                    UNSAFE = sun.misc.Unsafe.getUnsafe();
                    Class<?> ak = Cell.class;
                    valueOffset = UNSAFE.objectFieldOffset
                        (ak.getDeclaredField("value"));
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
        }
  
      /**
       * 私有构造函数
       */
      Striped64() {
      }
  
      /**
       * 处理数组的初始化，扩容，在桶内对象初始化，桶内cell争用
       *
       * @param x the value
       * @param fn 
       * @param wasUncontended 如果在之前cas失败，则为false
       */
      final void longAccumulate(long x, LongBinaryOperator fn,
                                boolean wasUncontended) {
        	// 线程的探针值
          int h;
         	// 若探针值为0则初始化
          if ((h = getProbe()) == 0) {
              ThreadLocalRandom.current(); // force initialization
              h = getProbe();
              wasUncontended = true;
          }
        
          boolean collide = false;                // 如果最后一个槽不空，则为真
          for (;;) {
              Cell[] as; 
            	Cell a; 
            	int n; 
            	long v;
            	// 如果当前对象的cells数组不为空，且长度大于0
              if ((as = cells) != null && (n = as.length) > 0) {
                	// 根据当前线程探针值取模计算得的index对应的桶 为 空 ，需要重新申明一个对象
                  if ((a = as[(n - 1) & h]) == null) {
  										// cellsBusy为0时 才可以进行初始化或者扩容等操作
                      if (cellsBusy == 0) {       // Try to attach new Cell
                          Cell r = new Cell(x);   // Optimistically create
                        	// 确认cellsBusy为0，且对cellsBusy进行CAS成功
                          if (cellsBusy == 0 && casCellsBusy()) {
                            	// 进行赋值
                              boolean created = false;
                              try {               // Recheck under lock
                                  Cell[] rs; 
                                	int m, j;
                                	// 将之前的判断条件重新判断，成立则进行赋值
                                  if ((rs = cells) != null &&
                                      (m = rs.length) > 0 &&
                                      rs[j = (m - 1) & h] == null) {
                                      rs[j] = r;
                                      created = true;
                                  }
                              } finally {
                                	// 将cellsBusy置为0，此时不需要CAS
                                  cellsBusy = 0;
                              }
                            	// 在new的时候，已将待计算的值赋入
                              if (created)
                                  break;
                              continue;           // Slot is now non-empty
                          }
                      }
                      collide = false;
                  }
                	// 已知CAS失败
                  else if (!wasUncontended)       
                    	// 暂时没有领悟到这里处理的理由
                      wasUncontended = true;   
                	// 对于当前cell进行CAS
                  else if (a.cas(v = a.value, ((fn == null) ? v + x :
                                               fn.applyAsLong(v, x))))
                      break;
                	// 如果cell数组长度大于cpu核心数 
                  else if (n >= NCPU || cells != as)
                      collide = false;            // At max size or stale
                  else if (!collide)
                      collide = true;
                	// cas加锁，以下进行扩容
                  else if (cellsBusy == 0 && casCellsBusy()) {
                      try {
                          if (cells == as) {      // Expand table unless stale
                              Cell[] rs = new Cell[n << 1];
                              for (int i = 0; i < n; ++i)
                                  rs[i] = as[i];
                              cells = rs;
                          }
                      } finally {
                          cellsBusy = 0;
                      }
                      collide = false;
                      continue;                   // Retry with expanded table
                  }
                  h = advanceProbe(h);
              }
            	// 上一个if中，as！=null，此时cells==as证明两个都为null，故需要初始化，当然先加CAS锁
              else if (cellsBusy == 0 && cells == as && casCellsBusy()) {
                  boolean init = false;
                  try {                           // Initialize table
                      if (cells == as) {
                          Cell[] rs = new Cell[2];
                          rs[h & 1] = new Cell(x);
                          cells = rs;
                          init = true;
                      }
                  } finally {
                      cellsBusy = 0;
                  }
                  if (init)
                      break;
              }
            	// 即不符合第一个if（cells不为空），第二个if（初始化）失败，则对base进行CAS
              else if (casBase(v = base, ((fn == null) ? v + x :
                                          fn.applyAsLong(v, x))))
                  break;                          // Fall back on using base
          }
      }
  
    // 获取 当前类中 各个成员变量在类中的偏移量
    private static final sun.misc.Unsafe UNSAFE;
    private static final long BASE;
    private static final long CELLSBUSY;
    private static final long PROBE;
    static {
      try {
        UNSAFE = sun.misc.Unsafe.getUnsafe();
        Class<?> sk = Striped64.class;
        BASE = UNSAFE.objectFieldOffset
          (sk.getDeclaredField("base"));
        CELLSBUSY = UNSAFE.objectFieldOffset
          (sk.getDeclaredField("cellsBusy"));
        Class<?> tk = Thread.class;
        PROBE = UNSAFE.objectFieldOffset
          (tk.getDeclaredField("threadLocalRandomProbe"));
      } catch (Exception e) {
        throw new Error(e);
      }
    }
    
    /**
     * CASes the base field.
     */
     final boolean casBase(long cmp, long val) {
          return UNSAFE.compareAndSwapLong(this, BASE, cmp, val);
     }
  
     /**
       * CASes the cellsBusy field from 0 to 1 to acquire lock.
       */
      final boolean casCellsBusy() {
          return UNSAFE.compareAndSwapInt(this, CELLSBUSY, 0, 1);
      }
  
      /**
       * Returns the probe value for the current thread.
       * Duplicated from ThreadLocalRandom because of packaging restrictions.
       */
      static final int getProbe() {
          return UNSAFE.getInt(Thread.currentThread(), PROBE);
      }
  
      /**
       * Pseudo-randomly advances and records the given probe value for the
       * given thread.
       * Duplicated from ThreadLocalRandom because of packaging restrictions.
       */
      static final int advanceProbe(int probe) {
          probe ^= probe << 13;   // xorshift
          probe ^= probe >>> 17;
          probe ^= probe << 5;
          UNSAFE.putInt(Thread.currentThread(), PROBE, probe);
          return probe;
      }
    
  }
  
  ```

  

- LongAdder

```java
public class LongAdder extends Striped64 implements Serializable {
    
    public void add(long x) {
        Cell[] as; 
        long b, v; 
        int m; 
        Cell a;
        // 如果cell数组不为空------->为空为false，不为空为true
        // 或者对主数据（base）进行cas失败，则进行以下方法体
        if ((as = cells) != null 
            || !casBase(b = base, b + x)) {
          
            boolean uncontended = true;
          	// 如果as即当前对象的cell数组为空
            // 或者cell数组长度小于1
            // 或者cell数组中，当前线程所落的桶中的对象（a）为null
            // 或者对a进行cas失败（此时uncontended对象为false）则进行以下方法体
            if (as == null 
								|| (m = as.length - 1) < 0 
								|| (a = as[getProbe() & m]) == null 
								|| !(uncontended = a.cas(v = a.value, v + x))
               )
              	// 该方法解决 数组的初始化，扩容，在桶内对象初始化，桶内cas失败
                longAccumulate(x, null, uncontended);
        }
    }

  	/**
  	 * 	计算当前对象的值，即将基值base，加上数组中所有cell的和
  	 */
    public long sum() {
        Cell[] as = cells; Cell a;
        long sum = base;
        if (as != null) {
            for (int i = 0; i < as.length; ++i) {
                if ((a = as[i]) != null)
                    sum += a.value;
            }
        }
        return sum;
    }

}

```

