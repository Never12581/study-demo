package com.hzx.lamda;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *  对这四种核心函数式接口的使用
 *  用lambda表达式的时候，有些参数的变量是可以自己随意定义的，但是类型会根据它的实现接口的类型继承下来
 *  例如：Myfunction(String str,Function<String, String> fun) 这个方法
 *  R apply(T t);   这个是function的抽象类  
 *  在实现类中 Myfunction("   hello Alice    ", f->f.trim());
 *  这个f就是随意定义的
 * @author scc
 */
public class Demo4 {
    public static void main(String[] args) {
        System.out.println("---------消费型接口的使用  Consumer--------------");
        happy(20000.0, m->System.out.println("聚餐消费了："+m+"元"));
        
        System.out.println("--------供给型接口  Supplier--------------");
        List<Integer> randomList = getRandom(9, ()->new Random().nextInt(101));
        System.out.println(randomList);
        
        System.out.println("--------函数型接口   --------------");
        String f1 = Myfunction("   hello Alice    ", f->f.trim());
        System.out.println("去掉字符串两头的空格："+f1);
        String f2 = Myfunction("HELLO ALICE", f->f.toLowerCase());
        System.out.println("把大写字母转换成小写字母："+f2);
        
        System.out.println("-------断言型接口   --------------");
        List<String> list=new ArrayList<>();
        list.add("张三");
        list.add("李四");
        list.add("王五");
        list.add("张三丰");
        list.add("张会");
        list.add("赵六");
        //找到所有姓张的人 在实现类重写的方法中  定义判断逻辑
        List<String> mydemo = mydemo(list, s->s.startsWith("张"));
        System.out.println(mydemo);
    }
    
    //消费型接口的使用  Consumer   聚餐例子
    public static void happy(Double money,Consumer<Double> com) {
        com.accept(money);
    }
    //供给型接口  Supplier    产生给定个数的随机数  0-100之间
    public static List<Integer> getRandom(int count,Supplier<Integer> sup) {
        List<Integer> list=new ArrayList<>();
        for(int i=0;i<count;i++) {
            list.add(sup.get());
        }
        return list;
    }
    //函数型接口   
    public static String Myfunction(String str,Function<String, String> fun) {
        return fun.apply(str);
    }
    //断言型接口   
    public static List<String> mydemo(List<String> list,Predicate<String> pre){
        List<String> mylist=new ArrayList<>();
        for (String s : list) {
            if(pre.test(s)) {
                mylist.add(s);
            }
        }
        return mylist;
    }
    
}