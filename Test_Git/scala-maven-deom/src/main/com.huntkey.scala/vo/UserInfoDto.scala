package vo

/**
  * Created by liuwens on 2017/9/13.
  */
class UserInfoDto
{

  var userName:String = "";

  private var userAge:Int = 0;

  //如果定义的方式没有(), 则引用时也不能有
  def getUserAge:Int = this.userAge;

  def setUserAge(age:Int):Unit =
  {
    this.userAge = age;
  }


}
