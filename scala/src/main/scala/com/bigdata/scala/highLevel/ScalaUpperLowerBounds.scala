package com.bigdata.scala.highLevel

/**
 * Date:2023/11/3
 * Author:wfm
 * Desc:上下界
 * [T <: xx] =》T都是xx的子类或者说xx就T的上界（Upper Bounds） 类似 java <T extends xx>
 * [T >: xx] =》T都是xx的父类或者说xx就T的下界（lower bounds）类似 java <T super D>
 */
object ScalaUpperLowerBounds {

  def main(args: Array[String]): Unit = {
    val cmpInt = new CmpLong(8L, 9L)
    println(cmpInt.bigger)

    //    val cmpComm1 = new CmpComm1(8, 9) // 上界的时候会报错
    val cmpComm1 = new CmpComm1(Integer.valueOf(8), Integer.valueOf(10))
    println(cmpComm1.bigger)

    val cmpComm11 = new CmpComm1(new Students1("Tom", 18), new Students1("Jim", 20))
    println(cmpComm11.bigger)

    val cmpComm2 = new CmpComm2(8, 9)
    println(cmpComm2.bigger)

    val cmpComm3 = new CmpComm3(8, 9)
    println(cmpComm3.bigger)

    val cmpComm4 = new CmpComm4(8, 9)
    println(cmpComm4.bigger)


    import ImplicitClass._
    val cmpComm41 = new CmpComm4(new Students2("Tom", 18), new Students2("Jim", 20))
    println(cmpComm41.bigger)


  }

}

class Students2(val name: String, val age: Int) {
  override def toString: String = this.name + "\t" + this.age
}

class Students1(val name: String, val age: Int) extends Ordered[Students1] {
  override def compare(that: Students1): Int = this.age - that.age

  override def toString: String = this.name + "\t" + this.age
}

class CmpInt(a: Int, b: Int) {
  def bigger = if (a > b) a else b
}

class CmpLong(a: Long, b: Long) {
  def bigger = if (a > b) a else b
}

// 如果还要定义 Double 类型的比较，也许还需要比较 2 个类的比较，咋办，还重复的劳动吗？
// 其实我们可以使用泛型, 但是泛型类型必须实现了 Comparable, 相当于约束了泛型的范围


// 不会发生隐式转换，除非用户显示的指定
// T 实现了 Comparable 接口
class CmpComm1[T <: Comparable[T]](o1: T, o2: T) {
  def bigger = if (o1.compareTo(o2) > 0) o1 else o2
}

/**
 * <% 视图界定 view bounds
 * 会发生隐式转换
 */
class CmpComm2[T <% Comparable[T]](o1: T, o2: T) {
  def bigger = if (o1.compareTo(o2) > 0) o1 else o2
}

/**
 * Ordered 继承了 Comparable
 * 跟 CmpComm2 一样的效果
 */
class CmpComm3[T <% Ordered[T]](o1: T, o2: T) {
  def bigger = if (o1 > o2) o1 else o2
}

// Comparator 传递比较器
/**
 * 上下文界定
 * 也会隐式转换
 * CmpComm4 - CmpComm6效果一样
 */
class CmpComm4[T: Ordering](o1: T, o2: T)(implicit cmptor: Ordering[T]) {
  def bigger = if (cmptor.compare(o1, o2) > 0) o1 else o2
}

class CmpComm5[T: Ordering](o1: T, o2: T) {
  def bigger = {
    def inner(implicit cmptor: Ordering[T]) = cmptor.compare(o1, o2)

    if (inner > 0) o1 else o2
  }
}

class CmpComm6[T: Ordering](o1: T, o2: T) {
  def bigger = {
    val cmptor = implicitly[Ordering[T]]
    if (cmptor.compare(o1, o2) > 0) o1 else o2
  }
}