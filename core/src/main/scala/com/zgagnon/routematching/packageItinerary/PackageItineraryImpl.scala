package com.zgagnon.routematching.packageItinerary

import com.google.inject.Inject

import scala.collection.mutable.Map
import scala.io.Source

/**
 * Created by Zell on 9/13/2015.
 */
class PackageItineraryImpl(packages:Map[String, String],
                           pickups:Map[String, Set[String]],
                           dropoffs:Map[String, Set[String]])
  extends PackageItinerary{

  @Inject def this() = this(Map(), Map(), Map())
  override def load(file: Source): PackageItinerary = {
    val packages = Map[String, String]()
    val pickups = Map[String, Set[String]]().withDefaultValue(Set())
    val dropoffs = Map[String, Set[String]]().withDefaultValue(Set())
    file.getLines() collect {
      case line if !line.trim.isEmpty => line.trim
    } foreach {
      l =>
        val parts = l.split(" ")
        packages(parts(0)) = l
        pickups(parts(1)) =  pickups(parts(1)) + parts(0)
        dropoffs(parts(2)) = dropoffs(parts(2)) + parts(0)
    }

    new PackageItineraryImpl(packages, pickups, dropoffs)
  }

  override def get(pack: String): String = {
    packages(pack)
  }

  override def pickup(stop: String): Set[String] = pickups(stop)

  override def dropoff(stop: String): Set[String] = dropoffs(stop)
}

