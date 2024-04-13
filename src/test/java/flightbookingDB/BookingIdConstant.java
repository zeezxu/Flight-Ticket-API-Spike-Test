package flightbookingDB;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class BookingIdConstant extends Simulation {

  private HttpProtocolBuilder httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("en-US,en;q=0.9")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");

  private ScenarioBuilder constantLoadScenario = scenario("FlightBooking_ConstantLoad")
    .exec(
            http("request_0")
                    .get("/bookings/FB-10")
    );
  private ScenarioBuilder spikeLoadScenario = scenario("FlightBooking_SpikeLoad")
          .exec(
                  http("request_0")
                          .get("/bookings/FB-10")
          );


  public BookingIdConstant() {
    setUp(
            constantLoadScenario.injectOpen(
                    constantUsersPerSec(50).during(1200)),
            // This will generate a constant load of 50 users/sec during 20 minutes (1200 seconds)

            spikeLoadScenario.injectOpen(
                    nothingFor(30), // waits for 30 seconds
                    atOnceUsers(500), // then injects 500 users at once - simulating the 1st spike
                    nothingFor(240), // waits for 4 minute
                    atOnceUsers(500), // then injects 500 users at once - simulating the 2nd spike
                    nothingFor(240), // waits for 4 minute
                    atOnceUsers(500), // then injects 500 users at once - simulating the 3rd spike
                    nothingFor(240), // waits for 4 minute
                    atOnceUsers(500), // then injects 500 users at once - simulating the 4th spike
                    nothingFor(240), // waits for 4 minute
                    atOnceUsers(500)) // then injects 500 users at once - simulating the 5th spike
    ).protocols(httpProtocol);
  }
}
