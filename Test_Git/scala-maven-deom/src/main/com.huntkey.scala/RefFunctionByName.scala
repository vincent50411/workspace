import vo.{TestDto, UserInfoDto}

/**
  * Created by liuwens on 2017/9/12.
  */
class RefFunctionByName
{

  def createUserInfo(userName:String) : UserInfoDto =
  {

    var userInfoDto:UserInfoDto = new UserInfoDto();

    userInfoDto.userName = "测试用户";

    userInfoDto.getUserAge;


    return userInfoDto;
  }



  var attriFunction = (value:Int, tt:String) => {

     "sdfdsfdsf" + "_" + value;

  };



  def time(value:String):String = {
    return "1111111" + "_" + value + "_" + attriFunction(100, "111");
  }

  def callB(value:String, value2:Int*) : String =
  {

    var result:String = value;

    for(intValue <- value2)
      {
        result += intValue;
      }

    return result;
  }


  def delayFunction(functionName:(String) => String) =
  {
    println("In delayed method");
    println("Param: " + functionName("Hello, world"));
  }

  def add(more:Int) =
    {
          //定义匿名函数
          var function = (value:Int) =>
          {
            var name:String = "userName;";

            //内部函数可以使用父函数的变量
            var message:String = (more + value) + name;

            //返回消息
            message;
          }

      //返回匿名函数的名称
      function;

    }


  def userSayMessage(testDto:TestDto) =
  {

      var result:String = testDto.sayHello("你好");

      print(result);



  }


}

object RefFunctionByName {

  def apply: RefFunctionByName = new RefFunctionByName();


}
