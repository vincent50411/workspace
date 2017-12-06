import vo.{TestDto, UserInfoDto}

/**
  * Created by liuwens on 2017/9/12.
  */
object MainTest {

  def main(args:Array[String]):Unit =
  {
    var refFunctionByName:RefFunctionByName = new RefFunctionByName();

    refFunctionByName.delayFunction(refFunctionByName.time);

    var result = refFunctionByName.callB("eeee", 2, 222);


    var addFunction = refFunctionByName.add(10);

    println(addFunction(100));
    println(addFunction(200));
    println(addFunction(300));


    var testDto:TestDto = new TestDto(3, "张三", "123");

    refFunctionByName.userSayMessage(testDto);

    var userMap:Map[String, UserInfoDto] = Map();

    var user001:UserInfoDto = new UserInfoDto();
    user001.userName = "001";

    userMap += ("user001" -> user001);

    userMap.keys.foreach{
      i =>
        printf("map key is %s", i);
        printf("use name is %s", userMap.get(i));
        println();
    }

    val list:List[Int] = List(1, 2, 3);

    list.map(i => i + 5).foreach(value => println(value));



  }


}
