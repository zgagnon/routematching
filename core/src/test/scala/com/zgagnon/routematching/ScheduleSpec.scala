package com.zgagnon.routematching

import com.zgagnon.routematching.packageItinerary.PackageItinerary
import com.zgagnon.routematching.schedule.Schedule
import org.specs2.Specification
import org.specs2.mock.Mockito
import org.specs2.specification.core.SpecStructure

import scala.io.Source

/**
 * Created by Zell on 9/13/2015.
 */
class ScheduleSpec extends Specification with Mockito{
  private val Stops =
    """
      | a3
      | a2
      | a1
    """.stripMargin

  private val LoadedPackages = """pA a1 a7
                                 |pB a2 a6
                                 |pC a3 a6
                                 |pD a3 a4
                                 |pE a5 a6""".stripMargin

  private val LoadedStops = """a1
                              |a2
                              |a3
                              |a4
                              |a5
                              |a6
                              |a7""".stripMargin

  override def is: SpecStructure = s2"""
    The schedule should load a messenger schedule from a file $load
    and compare that to an itinerary
     returning an empty string if there are no conflicts $conflictFree
     one item if there is a single conflict $singleConflict
     and collect all conflicts if there are more than one $multiConflict

    Additionaly, the schedule should verify the load during the journey
      returning an empty set if the load never exceeds the threshold $noLoad
      one item if there is a single step that is overloaded $singleOverload
      and collect all overloaded steps if there are more than one $multiOverload
  """

  def load = {
    var schedule = Schedule()
    schedule = schedule.load(Source.fromString(Stops))
    schedule.stops === "a3" :: "a2" :: "a1" :: Nil
  }

  def conflictFree = {
    var schedule = Schedule()
    schedule = schedule.load(Source.fromString(Stops))
    val itinerary = mock[PackageItinerary]
    itinerary.pickup("a3") returns Set("pA")
    itinerary.dropoff("a3") returns Set()
    itinerary.pickup("a2") returns Set()
    itinerary.dropoff("a2") returns Set("pA")
    itinerary.pickup("a1") returns Set()
    itinerary.dropoff("a1") returns Set()

    schedule.verifyPath(itinerary) === Set()
  }

  def singleConflict = {
    var schedule = Schedule()
    schedule = schedule.load(Source.fromString(Stops))
    val itinerary = mock[PackageItinerary]
    itinerary.pickup("a3") returns Set("pA")
    itinerary.dropoff("a3") returns Set("pB")
    itinerary.get("pB") returns "pB a3 a1"
    itinerary.pickup("a2") returns Set()
    itinerary.dropoff("a2") returns Set("pA")
    itinerary.pickup("a1") returns Set()
    itinerary.dropoff("a1") returns Set()

    schedule.verifyPath(itinerary) === Set("pB a3 a1")
  }


  def multiConflict = {
    var schedule = Schedule()
    schedule = schedule.load(Source.fromString(Stops))
    val itinerary = mock[PackageItinerary]
    itinerary.pickup("a3") returns Set("")
    itinerary.dropoff("a3") returns Set("pB", "pC")
    itinerary.pickup("a2") returns Set("pA")
    itinerary.dropoff("a2") returns Set("pC")
    itinerary.pickup("a1") returns Set()
    itinerary.dropoff("a1") returns Set("pA", "pB")
    itinerary.get("pB") returns "pB a3 a1"
    itinerary.get("pC") returns "pC a3 a2"
    schedule.verifyPath(itinerary) === Set("pB a3 a1", "pC a3 a2")
  }

  def noLoad = {
    val schedule = Schedule().load(Source.fromString("a1\na2"))
    val itinerary = mock[PackageItinerary]
    itinerary.pickup("a1") returns Set("pA")
    itinerary.dropoff("a1") returns Set()
    itinerary.pickup("a2") returns Set()
    itinerary.dropoff("a2") returns Set("pA")

    schedule.verifyLoad(itinerary, 2) must beEmpty
  }

  def singleOverload = {
    val schedule = Schedule().load(Source.fromString("a1\na2"))
    val itinerary = mock[PackageItinerary]
    itinerary.pickup("a1") returns Set("pA", "pB", "pC")
    itinerary.dropoff("a1") returns Set()
    itinerary.pickup("a2") returns Set()
    itinerary.dropoff("a2") returns Set("pA", "pB", "pC")

    schedule.verifyLoad(itinerary, 2) === Set("a1 a2")
  }

  def multiOverload = {
    val schedule = Schedule().load(Source.fromString(LoadedStops))
    val itinerary = PackageItinerary().load(Source.fromString(LoadedPackages))

    schedule.verifyLoad(itinerary,3) === Set("a3 a3", "a5 a6")
    pending
  }
}
