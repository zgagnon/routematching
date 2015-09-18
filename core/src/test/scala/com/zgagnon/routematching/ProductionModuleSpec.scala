package com.zgagnon.routematching

import com.google.inject.Guice
import com.zgagnon.routematching.modules.ProductionModule
import com.zgagnon.routematching.packageItinerary.{PackageItineraryImpl, PackageItinerary}
import com.zgagnon.routematching.schedule.{ScheduleImpl, Schedule}
import org.specs2.Specification

/**
 * Created by Zell on 9/13/2015.
 */
class ProductionModuleSpec extends Specification{
  import net.codingwell.scalaguice.InjectorExtensions._
 def is = s2"""
  The production module should correctly produce implementation objects $instances
 """

  def instances = {
    val injector = Guice.createInjector(new ProductionModule())
    val schedule = injector.instance[Schedule]
    val itinerary = injector.instance[PackageItinerary]
    (schedule must beLike{case s:ScheduleImpl => ok}) and
      (itinerary must beLike{case p:PackageItineraryImpl => ok})
  }
}
