package tests

class UsageClass[T: Manifest] {
  val StringManifest = manifest[String]
  val IntManifest = manifest[Int]

  def test(): Unit = {

    manifest[T] match {
      case StringManifest => println("A string is received")
      case IntManifest => println("An int is received")
      case x if x.runtimeClass.isArray => println("An array is received")
      case _ => println("unknown type")
    }
    println("Hello World")
  }
}