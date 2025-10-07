package se.sundsvall.accessmapper;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

@WireMockAppTestSuite(files = "classpath:/AccessIT/", classes = Application.class)
@Sql({
	"/db/scripts/truncate.sql",
	"/db/scripts/testdata-it.sql"
})
class AccessIT extends AbstractAppTest {

	private static final String NAMESPACE = "NAMESPACE-1";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String AD_ID = "joe01doe";
	private static final String PATH = "/" + MUNICIPALITY_ID + "/" + NAMESPACE + "/access/ad/" + AD_ID;
	private static final String RESPONSE_FILE = "response.json";

	@Test
	void test01_getAccessDetails() {
		setupCall()
			.withServicePath(PATH)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(APPLICATION_JSON_VALUE))
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
