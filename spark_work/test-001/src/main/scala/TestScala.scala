import test._

/**
  * Created by liuwens on 2017/9/15.
  */
object TestScala {
  out =>

  def main(arg:Array[String]):Unit = {

    val message = "abc ab c ab abc c c c ab a ab abc abc";
    val message2 = "ab ab abc"

    var messageList = List[String](message, message2)

    val result = messageList.flatMap(word => word.split(" ")).map(word => (word, 1)).groupBy(x => x._1).
      mapValues(value => value.map(tuple => tuple._2).reduce(_ + _))

    println(result)

    println("---------------")

    val map1 = Map("001" -> "abc", "002" -> "a", "003" -> "abcd")


    println(map1.groupBy(x => x._1))


    val testDto = new TestDto("testvalue")


  }






}
