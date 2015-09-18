package com.zgagnon.routematching

import com.zgagnon.routematching.packageItinerary.PackageItinerary
import org.specs2.Specification
import org.specs2.mock.Mockito
import org.specs2.specification.core.SpecStructure

import scala.io.Source

/**
 * Created by Zell on 9/13/2015.
 */
class PackageItinerarySpec extends Specification with Mockito{
  private val Packages =
    """
      |pA a1 a2
      |pB a2 a3
    """.stripMargin

  override def is: SpecStructure = s2"""
    The package itinerary needs to load from a file $load
    once loaded, it should be able to list all packages for pickup at a location $pickup
    and olse list all packages for dropoff at a location $dropoff
    for either pickup or dropoff, if there isn't anything it should return an empty set $noop
  """

  def load = {
    var itinerary = PackageItinerary()
    itinerary = itinerary.load(Source.fromString(Packages))
    (itinerary.get("pA") === "pA a1 a2") and
      (itinerary.get("pB") == "pB a2 a3")
  }

  def pickup = {
    val itinerary = PackageItinerary().load(Source.fromString(Packages))
    (itinerary.pickup("a1") === Set("pA")) and
      (itinerary.pickup("a2") === Set("pB"))
  }

  def dropoff = {
    val itinerary = PackageItinerary().load(Source.fromString(Packages))
    (itinerary.dropoff("a2") === Set("pA")) and
      (itinerary.dropoff("a3") === Set("pB"))
  }

  def noop = {
    val itinerary = PackageItinerary().load(Source.fromString(Packages))
    (itinerary.pickup("a4") === Set()) and
      (itinerary.dropoff("a4") === Set())
  }
}
