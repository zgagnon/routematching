<!--- Uses github flavor markdown, best if viewed in a github flavor markdown viewer -->

# Readme

## General technology
Solution is written in Scala, using the Google Guice scala wrapper from 
[CodingWell](https://github.com/codingwell/scala-guice) and the 
[Specs2 test framework](http://etorreborre.github.io/specs2/). The solution can be built using the [SBT build tool]
(http://www.scala-sbt.org/download.html) and the command: `sbt compile` or to test `sbt test`. Test coverage was 
calculated via the built-in IntelliJ test coverage tool, and currently stands at 95% method coverage and 91% line coverage.

To build, please use sbt with the commands "compile" and "assembly", as such:
```
sbt compile
sbt assembly
```

This will create jar files for the verify1 and verify2 projects in the respective target directories. For convenience

## Implementation
The files are parsed by task-specific structures: `PackageItinerary` and `Schedule` - each is provided with a generic
interface for use and a specific implementation. 

The `PackageItinerary` breaks each line into individual tokens and 
stores it in three maps: a package map mapping from package id to the complete line, a pickup map mapping from pickup
location id to the set of package ids to be picked up, and a dropoff map mapping from the dropoff location to the 
set of package ids to be dropped off. This is done in a single pass through the file, reducing computational complexity
in exchange for higher memory footprint. It also allows constant time access for all package lines, pickup locations,
and drop off locations.

The `Schedule` parses the stops into an ordered list of location ids. The `Schedule` also has the capability to compare
a given schedule with a `PackageItinerary`. For each stop in the schedule, the set of all packages picked up so far
is compared to the set of packages to be dropped off at that location. If a package is to be dropped off, but not yet
picked up, the full line for that package is retrieved and stored. Otherwise the package is removed from the set of
picked-up but not dropped-off packages, and all packages to be picked up at the location are added to the list. 
Disregarding the running time of the set difference the comparison itself has a run time of O(n) where n is 
the number of locations to be visited. With the set difference included, the worst case run time is O(nm) where n 
is the number of locations and m is the number of packages.

The `Schedule` also has the ability to calculate load at each of the stops, keeping a running count of the number of 
packages being carried and returning any step of the journey which is overloaded.

## Assumptions
For this exercise, I assume all input files are valid; no input validation is performed. I also assume that the output
order of any improper packages doesn't matter. In both cases, I would like to check what the expected behavior is
before proceeding to implementation. For input validation, it may be useful to produce a list of invalid lines
while still computing the invalid packages from the valid lines. For output order, I would assume either sorting
by package id or pick up location id would be preferred.