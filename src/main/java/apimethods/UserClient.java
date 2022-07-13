package apimethods;

import com.google.gson.JsonObject;
import model.Token;
import model.User;
import model.UserCredentials;

public class UserClient extends RestAssuredBurger {
    private final String REGISTRATION_URL = "/auth/register";
    private final String USER_URL = "/auth/user";
    private final String LOGIN_URL = "/auth/login";
    private final String LOGOUT_URL = "/auth/logout";

    public Token create(User user) {
        var response = reqSpec
                .body(user)
                .when()
                .post(REGISTRATION_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract();
        return new Token(response.path("accessToken"), response.path("refreshToken"));
    }

    public void delete(Token token) {
        reqSpec
                .header("authorization", token.getAccessToken())
                .when()
                .delete(USER_URL)
                .then()
                .assertThat()
                .statusCode(202);
    }

    public Token login(UserCredentials creds) {
        var response = reqSpec
                .body(creds)
                .when()
                .post(LOGIN_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract();
        return new Token(response.path("accessToken"), response.path("refreshToken"));
    }

    public void logout(Token token) {
        var json = new JsonObject();
        json.addProperty("token", token.getRefreshToken());
        reqSpec
                .body(json.toString())
                .when()
                .post(LOGOUT_URL)
                .then()
                .assertThat()
                .statusCode(200);
    }
}
