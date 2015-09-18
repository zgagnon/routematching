package com.zgagnon.routematching

import com.google.inject.Guice
import com.zgagnon.routematching.modules.ProductionModule

import scala.io.Source

object MainVerify2 extends App{
  val injector = Guice.createInjector(new ProductionModule())
  val packages = Source.fromFile(args(0))
  val stops = Source.fromFile(args(1))

  val output = Core.verifyLoad(3,packages, stops, injector)
  println(output)
}
