package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineAcceptanceTest.지하철_노선을_생성한다;
import static nextstep.subway.acceptance.StationAcceptanceTest.지하철역을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.common.AcceptanceTest;
import nextstep.subway.common.RestAssuredTemplate;
import nextstep.subway.dto.SectionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("acceptance")
public class SectionAcceptanceTest extends AcceptanceTest {
    @BeforeEach
    public void setUp() {
        super.setUp();

        지하철역을_생성한다("강남역");
        지하철역을_생성한다("잠실역");
        지하철역을_생성한다("삼성역");
        지하철역을_생성한다("사당역");
        지하철역을_생성한다("건대입구역");
        지하철_노선을_생성한다("2호선", "green lighten-3");
    }

    /**
     * When 지하철역 사이에 새로운 역을 등록
     * Then 생성한 지하철 노선의 지하철역 정보를 응답받을 수 있다.
     */
    @DisplayName("강남역과 잠실역 사이에 유효한 강남역-삼성역 구간을 등록한다")
    @Test
    void addMidSection_upStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_등록_요청(1, 3, 5);

        // then
        지하철_노선에_지하철_구간_등록됨(response);
        지하철_노선에_지하철_구간_조회됨(new String[]{"강남역", "삼성역", "잠실역"});
    }

    /**
     * When 지하철역 사이에 기존 역 사이 길이보다 크거나 같은 구간을 등록한다
     * Then 등록에 실패했다는 응답이 반환된다.
     */
    @DisplayName("강남역과 잠실역 사이에 유효하지 않은 강남역-삼성역 구간을 등록한다")
    @Test
    void addMidSection_upStation_error() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_등록_요청(1, 3, 10);

        // then
        지하철_노선에_지하철_구간_등록안됨(response);
    }

    /**
     * When 지하철역 사이에 새로운 역을 등록
     * Then 생성한 지하철 노선의 지하철역 정보를 응답받을 수 있다.
     */
    @DisplayName("강남역과 잠실역 사이에 유효한 삼성역-잠실역 구간을 등록한다")
    @Test
    void addMidSection_downStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_등록_요청(3, 2, 5);

        // then
        지하철_노선에_지하철_구간_등록됨(response);
        지하철_노선에_지하철_구간_조회됨(new String[]{"강남역", "삼성역", "잠실역"});
    }

    /**
     * When 지하철역 사이에 새로운 역을 등록
     * Then 등록에 실패했다는 응답이 반환된다.
     */
    @DisplayName("강남역과 잠실역 사이에 유효하지 않은 삼성역-잠실역 구간을 등록한다")
    @Test
    void addMidSection_downStation_error() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_등록_요청(3, 2, 10);

        // then
        지하철_노선에_지하철_구간_등록안됨(response);
    }

    /**
     * When 새로운 역을 상행 종점으로 등록
     * Then 생성한 지하철 노선의 지하철역 정보를 응답받을 수 있다.
     */
    @DisplayName("강남역과 연결되는 사당역-강남역 구간을 등록한다")
    @Test
    void addSection_upStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_등록_요청(4, 1, 5);

        // then
        지하철_노선에_지하철_구간_등록됨(response);
        지하철_노선에_지하철_구간_조회됨(new String[]{"사당역", "강남역", "잠실역"});
    }

    /**
     * When 새로운 역을 하행 종점으로 등록
     * Then 생성한 지하철 노선의 지하철역 정보를 응답받을 수 있다.
     */
    @DisplayName("잠실역과 연결되는 잠실역-건대입구역 구간을 등록한다")
    @Test
    void addSection_downStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_등록_요청(2, 5, 5);

        // then
        지하철_노선에_지하철_구간_등록됨(response);
        지하철_노선에_지하철_구간_조회됨(new String[]{"강남역", "잠실역", "건대입구역"});
    }

    /**
     * When 이미 노선에 모두 등록되어 있는 상행역과 하행역을 등록하면
     * Then 등록에 실패했다는 응답이 반환된다.
     */
    @DisplayName("노선에 이미 등록되어 있는 강남역-잠실역 구간을 등록한다")
    @Test
    void addSection_alreadyExistSection() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_등록_요청(1, 2, 5);

        // then
        지하철_노선에_지하철_구간_등록안됨(response);
    }

    /**
     * When 노선에 등록되지 않은 상행역과 하행역을 등록하면
     * Then 등록에 실패했다는 응답이 반환된다.
     */
    @DisplayName("노선에 등록되어 있지 않은 사당역-건대입구역 구간을 등록한다")
    @Test
    void addSection_nonRegisterSection() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_등록_요청(4, 5, 5);

        // then
        지하철_노선에_지하철_구간_등록안됨(response);
    }

    /**
     * When 노선의 구간중 시작역을 삭제하면
     * Then 재배치된 지하철 노선의 지하철역 정보를 응답받을 수 있다.
     */
    @DisplayName("강남역-삼성역-잠실역 구간에서 강남역을 삭제한다")
    @Test
    void deleteFirstStation() {
        // when
        지하철_노선에_지하철_구간_등록_요청(1, 3, 5);
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_삭제_요청(1, 1);

        // then
        지하철_노선에_지하철_구간_삭제됨(response);
        지하철_노선에_지하철_구간_조회됨(new String[]{"삼성역", "잠실역"});
    }

    /**
     * When 노선의 구간중 종점역을 삭제하면
     * Then 재배치된 지하철 노선의 지하철역 정보를 응답받을 수 있다.
     */
    @DisplayName("강남역-삼성역-잠실역 구간에서 잠실역을 삭제한다")
    @Test
    void deleteLastStation() {
        // when
        지하철_노선에_지하철_구간_등록_요청(1, 3, 5);
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_삭제_요청(1, 2);

        // then
        지하철_노선에_지하철_구간_삭제됨(response);
        지하철_노선에_지하철_구간_조회됨(new String[]{"강남역", "삼성역"});
    }

    /**
     * When 노선의 구간중 중간역을 삭제하면
     * Then 재배치된 지하철 노선의 지하철역 정보를 응답받을 수 있다.
     */
    @DisplayName("강남역-삼성역-잠실역 구간에서 삼성역을 삭제한다")
    @Test
    void deleteMiddleStation() {
        // when
        지하철_노선에_지하철_구간_등록_요청(1, 3, 5);
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_삭제_요청(1, 3);

        // then
        지하철_노선에_지하철_구간_삭제됨(response);
        지하철_노선에_지하철_구간_조회됨(new String[]{"강남역", "잠실역"});
    }

    /**
     * When 노선의 구간중 존재하지 않는 역을 삭제하면
     * Then 삭제에 실패했다는 응답이 반환된다.
     */
    @DisplayName("강남역-삼성역-잠실역 구간에서 건대입구역을 삭제한다")
    @Test
    void deleteNonExistStation() {
        // when
        지하철_노선에_지하철_구간_등록_요청(1, 3, 5);
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_삭제_요청(1, 5);

        // then
        지하철_노선에_지하철_구간_삭제안됨(response);
    }

    /**
     * When 노선의 구간중 마지막 구간을 삭제하면
     * Then 삭제에 실패했다는 응답이 반환된다.
     */
    @DisplayName("강남역-삼성역 구간에서 강남역을 삭제한다")
    @Test
    void deleteLastUpStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_삭제_요청(1, 1);

        // then
        지하철_노선에_지하철_구간_삭제안됨(response);
    }

    /**
     * When 노선의 구간중 마지막 구간을 삭제하면
     * Then 삭제에 실패했다는 응답이 반환된다.
     */
    @DisplayName("강남역-삼성역 구간에서 삼성역을 삭제한다")
    @Test
    void deleteLastDownStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_삭제_요청(1, 2);

        // then
        지하철_노선에_지하철_구간_삭제안됨(response);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_등록_요청(long upStationId, long downStationId,
                                                                     long distance) {
        return RestAssuredTemplate.post("/lines/{lineId}/sections", 1,
                new SectionRequest(upStationId, downStationId, distance));
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_삭제_요청(long lineId, long deleteStationId) {
        return RestAssuredTemplate.delete("/lines/{lineId}/sections?stationId=" + deleteStationId, lineId);
    }

    public static List<String> 지하철_노선의_구간을_조회한다(long lineId) {
        return RestAssuredTemplate.get("/lines/" + lineId).jsonPath().getList("stations.name");
    }

    void 지하철_노선에_지하철_구간_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    void 지하철_노선에_지하철_구간_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    void 지하철_노선에_지하철_구간_등록안됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    void 지하철_노선에_지하철_구간_삭제안됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    void 지하철_노선에_지하철_구간_조회됨(String[] expected) {
        List<String> actual = 지하철_노선의_구간을_조회한다(1);
        assertThat(actual).containsExactly(expected);
    }
}
