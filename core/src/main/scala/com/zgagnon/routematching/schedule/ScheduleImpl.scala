package com.zgagnon.routematching.schedule

import com.google.inject.Inject
import com.zgagnon.routematching.packageItinerary.PackageItinerary

import scala.io.Source

/**
 * Created by Zell on 9/13/2015.
 */
class ScheduleImpl(val stops:List[String]) extends Schedule {
  @Inject def this() = this(List())
  override def load(file: Source): Schedule = {
    val lines = file.getLines() collect {case line if !line.trim.isEmpty => line.trim}
    new ScheduleImpl(lines.toList)
  }

  override def verifyPath(itinerary: PackageItinerary): Set[String] = {
    var active = Set[String]()
    val errors = for(stop <- stops) yield {
      val dropoffs = itinerary.dropoff(stop)
      val errors = dropoffs -- active
      active = active -- dropoffs

      val pickups = itinerary.pickup(stop)
      active = active ++ pickups
      errors map {itinerary.get(_)}
    }

    errors.flatten.toSet
  }

  override def verifyLoad(itinerary: PackageItinerary, maxLoad: Int): Set[String] = {
    var load = 0
    var overloaded = Set[String]()
    stops.sliding(2) foreach {
      case pick :: drop :: _ =>
        load -= itinerary.dropoff(pick).size
        load += itinerary.pickup(pick).size
        if(load > maxLoad) {
          overloaded = overloaded + s"$pick $drop"
        }
    }
    overloaded
  }
}
