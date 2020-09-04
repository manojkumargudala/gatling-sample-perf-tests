package manoj.gatling.sample

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.core.structure.ChainBuilder
import scala.util.Random

class OneMoreSimulation extends SampleGatlingConfig {
  val feeder = Iterator.continually(Map(
    "name" -> (Random.alphanumeric.take(20).mkString),
    "companynumber" -> (Random.nextInt(20).toString())))

  val scn = scenario("Scenario Name").feed(feeder) 
    .exec(http("request_1")
      .get("/"))
    .pause(7)
    .exec(http("request_2")
      .get("/computers?f=macbook"))
    .pause(2)
    .exec(http("request_3")
      .get("/computers/6"))
    .pause(3)
    .exec(http("request_4")
      .get("/"))
    .pause(2)
    .exec(http("request_5")
      .get("/computers?p=1"))
    .pause(5)
    .exec(http("request_9")
      .get("/computers/new"))
    .pause(1)
    .exec(http("request_10") // Here's an example of a POST request
      .post("/computers")
      .formParam("""name""", "${name}") // Note the triple double quotes: used in Scala for protecting a whole chain of characters (no need for backslash)
      .formParam("""introduced""", """2012-05-30""")
      .formParam("""discontinued""", """""")
      .formParam("""company""", "${companynumber}"))

  setUp(scn.inject(atOnceUsers(1)).protocols(httpProtocol))
}