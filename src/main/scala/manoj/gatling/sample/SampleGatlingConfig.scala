package manoj.gatling.sample

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.core.session.Session
import io.gatling.http.response.Response
import io.gatling.http.request.HttpRequest
import io.gatling.http.request.ExtraInfo

trait SampleGatlingConfig extends Simulation {

  val httpConf: HttpProtocolBuilder = http
    .baseURL("http://www.thomas-bayer.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")
    .extraInfoExtractor { extraInfo => List(getExtraInfo(extraInfo)) }
  private def getExtraInfo(extraInfo: ExtraInfo): String = {
    ",\tURL:" + extraInfo.request.getUrl + // "\tparams\t" + extraInfo.request.getQueryParams.toArray().mkString("\t") +
      " Request: " + extraInfo.request.getStringData +
      " Response: " + extraInfo.response.body.string
  }
}
