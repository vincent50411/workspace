package vo

/**
  *
  * Created by liuwens on 2017/9/13.
  */
class TestDto (userName:String, userAge:String)
{

  printf("test----------------")

  private var value2:Int = 0;

  def this(value2:Int,userName:String, userAge:String)
  {
    //调用主构造函数
    this(userName, userAge);

    this.value2 = value2;
  }


  def sayHello(message:String):String =
  {

    var resultMessage:String = userName + " say, he current age is " + userAge + ", and say messag is: " + message;

    return resultMessage;

  }




}
