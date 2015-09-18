package com.zgagnon.routematching

import com.google.inject.{Guice, Injector}
import com.zgagnon.routematching.packageItinerary.PackageItinerary
import com.zgagnon.routematching.schedule.Schedule
import com.zgagnon.routematching.modules.ProductionModule

import scala.io.Source

/**
 * Created by Zell on 9/13/2015.
 */
object Core extends App {
  import net.codingwell.scalaguice.InjectorExtensions._

  /**
   * Compares a file of packages with a file of stops in order to determine if any
   * packages are scheduled to be picked up before they are dropped off.
   * @param packages
   * @param stops
   * @param injector
   * @return The list of packages as a string, one per line. Assumes that the expected order
   *         of results is not defined.
   */
  def verifyPath(packages:Source, stops:Source, injector:Injector):String = {
    var itinerary = injector.instance[PackageItinerary]
    var schedule = injector.instance[Schedule]
    itinerary = itinerary.load(packages)
    schedule = schedule.load(stops)
    (schedule verifyPath itinerary).mkString("\n")
  }

  def verifyLoad(max:Int, packages:Source, stops:Source, injector:Injector):String = {
    val itinerary = injector.instance[PackageItinerary].load(packages)
    val schedule = injector.instance[Schedule].load(stops)
    schedule.verifyLoad(itinerary, max).mkString("\n")
  }
}
