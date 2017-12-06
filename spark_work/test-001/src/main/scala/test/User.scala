package test

/**
  * Created by liuwens on 2017/9/19.
  */
class User[T](userName:T, age:Int) extends SuperUser[T](userName:T)
{

  override def getUserName[U <: T]: T = super.getUserName

  def getAge(): Int = age


}
