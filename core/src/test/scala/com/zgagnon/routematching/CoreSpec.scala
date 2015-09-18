package com.zgagnon.routematching

import com.google.inject.{Guice, AbstractModule}
import com.zgagnon.routematching.packageItinerary.PackageItinerary
import com.zgagnon.routematching.schedule.Schedule
import net.codingwell.scalaguice.ScalaModule
import org.specs2.Specification
import org.specs2.mock.Mockito
import org.specs2.specification.core.SpecStructure

import scala.io.Source

/**
 * Created by Zell on 9/13/2015.
 */
class CoreSpec extends Specification with Mockito{
  import net.codingwell.scalaguice.InjectorExtensions._

  private val Packages =
    """
      |pA a1 a2
      |pB a2 a3
    """.stripMargin

  private val OverloadedPackages =
  """
    |pA a3 a2
    |pB a3 a2
    |pC a3 a2
    |pD a3 a2
  """.stripMargin

  private val Stops =
    """
      | a3
      | a2
      | a1
    """.stripMargin

  override def is: SpecStructure = s2"""
    The verifyPath method should load the packages into a package itinerary $loadPackages
      and it should load the schedule into a schedule object $loadSchedule

    With both sources loaded, it should finally compare the two and return the mismatches $compare

    The verifyLoad method should also load the packages,
    but should compare the two using a maximum load size and return the overloaded journeys $overload
  """

  def loadPackages = {
    val injector = Guice.createInjector(new StartSpecModule)
    val itinerary = injector.instance[PackageItinerary]
    val schedule = injector.instance[Schedule]
    val packages = Source.fromString(Packages)
    val stops = Source.fromString("")

    schedule.verifyPath(any[PackageItinerary]) returns Set()

    Core.verifyPath(packages, stops, injector)
    there was one(itinerary).load(packages)
  }

  def loadSchedule = {
    val injector = Guice.createInjector(new StartSpecModule)
    val schedule = injector.instance[Schedule]
    val packages = Source.fromString("")
    val stops = Source.fromString(Stops)

    schedule.verifyPath(any[PackageItinerary]) returns Set()

    Core.verifyPath(packages, stops, injector)
    there was one(schedule).load(stops)
  }

  def compare = {
    val injector = Guice.createInjector(new StartSpecModule)
    val schedule = injector.instance[Schedule]
    val itinerary = injector.instance[PackageItinerary]
    schedule.verifyPath(any[PackageItinerary]) returns Set("pA a1 a2", "pB a2 a3")
    val packages = Source.fromString(Packages)
    val stops = Source.fromString(Stops)

    val result = Core.verifyPath(packages, stops, injector)
    (result.contains("pA a1 a2")) and
      (result.contains("pB a2 a3"))
  }

  def overload = {
    val injector = Guice.createInjector(new StartSpecModule)
    val schedule = injector.instance[Schedule]
    schedule.verifyLoad(any[PackageItinerary], any[Int]) returns Set("a3 a2")
    val packages = Source.fromString(Packages)
    val stops = Source.fromString(Stops)

    val result = Core.verifyLoad(3, packages, stops, injector)
    result.contains("a3 a2")
  }
}

class StartSpecModule extends AbstractModule with ScalaModule with Mockito{
  val itinerary = mock[PackageItinerary]
  val schedule = mock[Schedule]

  itinerary.load(any[Source]) returns itinerary
  schedule.load(any[Source]) returns schedule
  override def configure(): Unit = {
    bind[PackageItinerary].toInstance(itinerary)
    bind[Schedule].toInstance(schedule)
  }
}
