package nextstep.subway.common;

import io.restassured.RestAssured;
import nextstep.subway.common.DatabaseCleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

public class AcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup dataBaseCleanUp;

    public void setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port;
            dataBaseCleanUp.afterPropertiesSet();
        }
        dataBaseCleanUp.execute();
    }
}
