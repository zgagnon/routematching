package com.zgagnon.routematching.schedule

import com.zgagnon.routematching.packageItinerary.PackageItinerary

import scala.io.Source

/**
 * Created by Zell on 9/13/2015.
 */
trait Schedule {
  def load(file:Source):Schedule
  def verifyPath(itinerary: PackageItinerary): Set[String]
  def verifyLoad(itinerary: PackageItinerary, maxLoad:Int):Set[String]
  def stops: List[String]
}

object Schedule {
  def apply():Schedule = new ScheduleImpl
}
