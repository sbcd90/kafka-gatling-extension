package tests

object TestClass {
  def main(args: Array[String]): Unit = {
    val usageClassWithInt = new UsageClass[Int]()
    usageClassWithInt.test()

    val usageClassWithString = new UsageClass[String]()
    usageClassWithString.test()

    val usageClassWithArray = new UsageClass[Array[String]]()
    usageClassWithArray.test()

    val usageClassWithDouble = new UsageClass[Double]()
    usageClassWithDouble.test()
  }
}