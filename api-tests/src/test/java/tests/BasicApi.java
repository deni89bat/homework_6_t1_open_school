package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.APIConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import utils.RestApiBuilder;

public class BasicApi {

    public static APIConfig config;
    protected static String token;

    @BeforeAll
    static void setup() {
        config = ConfigFactory.create(APIConfig.class, System.getProperties());
        RestAssured.baseURI = config.baseURI();
        AuthApi.registerNewUser("bat666888", "pass8888");
        token = getAuthToken();
        // Токен теперь статически инициализируется
    }

    static String getAuthToken() {
        String access_token = AuthApi.loginUser("bat666888", "pass8888").jsonPath()
            .getString("access_token");
        System.out.println("access_token: " + access_token);
        return access_token;
    }

    protected static String toJSON(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected static <T> T toDTOObject(Response response, Class<T> t) {
        try {
            return new ObjectMapper().readValue(response.getBody().prettyPrint(), t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public RequestSpecification getBuilder() {
        return new RestApiBuilder(config.baseURI())
            .addAuth(token)
            .build().accept("application/json");
    }

    public RequestSpecification getBuilderWithoutAuth() {
        return new RestApiBuilder("http://9b142cdd34e.vps.myjino.ru:49268")
            .build();
    }
}