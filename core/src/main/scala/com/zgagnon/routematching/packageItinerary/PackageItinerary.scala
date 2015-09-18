package com.zgagnon.routematching.packageItinerary

import scala.io.Source

/**
 * Created by Zell on 9/13/2015.
 */
trait PackageItinerary {
  def load(file:Source):PackageItinerary
  def pickup(stop:String):Set[String]
  def dropoff(stop:String):Set[String]
  def get(pack:String):String
}


object PackageItinerary {
  def apply():PackageItinerary = new PackageItineraryImpl()
}
