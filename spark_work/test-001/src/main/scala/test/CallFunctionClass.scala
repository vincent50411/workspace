package test

/**
  * Created by liuwens on 2017/9/19.
  */
class CallFunctionClass[T] {

  def sayHello[U >: T](param:T): Unit =
  {

  }

  def sayHello2[U <: T](param:T): Unit =
  {

  }

}
