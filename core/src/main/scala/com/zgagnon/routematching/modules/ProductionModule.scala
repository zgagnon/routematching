package com.zgagnon.routematching.modules

import com.google.inject.AbstractModule
import com.zgagnon.routematching.packageItinerary.{PackageItineraryImpl, PackageItinerary}
import com.zgagnon.routematching.schedule.{ScheduleImpl, Schedule}
import net.codingwell.scalaguice.ScalaModule

/**
 * Created by Zell on 9/13/2015.
 */
class ProductionModule extends AbstractModule with ScalaModule{
  override def configure(): Unit = {
    bind[Schedule].to[ScheduleImpl]
    bind[PackageItinerary].to[PackageItineraryImpl]
  }
}
