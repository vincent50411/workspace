package test

/**
  * Created by liuwens on 2017/9/19.
  */
class SuperUser[T](param:T) {
  var newName:T = _;

  def getUserName[U <: T]:T = param;

  def setUserName[U >: T](value:T) = newName = value;
}
