package manoj.gatling.sample

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.core.structure.ChainBuilder

class SampleSim extends SampleGatlingConfig {
  val namespaces: List[(String, String)] = List(("xlink", "http://www.w3.org/1999/xlink"));
  val homwReqstBldr: HttpRequestBuilder = http("GET HOME PAGE").get("/sqlrest/").check(xpath("//@xlink:href", namespaces).findAll.saveAs("difflists"));
  val queryParamFeeder = csv("data/abcd.csv").random
  var detailUrls: String = "detailUrls"
  var detailUrl: String = "detailUrl"
  var detailedChain: ChainBuilder = exec(getHttpRequestBlr(getGatlingVariable("difflist"), detailUrls))
    .foreach(getGatlingVariable(detailUrls), detailUrl)(exec(http(getGatlingVariable(detailUrl)).get(getGatlingVariable(detailUrl))))
  val scn: ScenarioBuilder = scenario("BasicSimulation").feed(queryParamFeeder)
    .exec(homwReqstBldr)
    .foreach("${difflists}", "difflist")(detailedChain)
  def getHttpRequestBlr(myUrl: String, storeLst: String): HttpRequestBuilder = {
    println("URL is " + myUrl)
    http(myUrl)
      .get(myUrl).check(xpath("//@xlink:href", namespaces).findAll.saveAs(storeLst))

  }
  def getGatlingVariable(variable: String): String = {
    "${" + variable + "}"
  }

  setUp(
    scn.inject(
      nothingFor(4), // 1
      atOnceUsers(10), // 2
      rampUsers(10) during (5), // 3
      constantUsersPerSec(20) during (15), // 4
      constantUsersPerSec(20) during (15), // 5
      rampUsersPerSec(10) to 20 during (10), // 6
      rampUsersPerSec(10) to 20 during (10), // 7
      heavisideUsers(1000) during (20) // 8
    ).protocols(httpConf))

}
