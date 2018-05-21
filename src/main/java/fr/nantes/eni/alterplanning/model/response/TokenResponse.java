package fr.nantes.eni.alterplanning.model.response;


public class TokenResponse {

    private String access_token;

    private String token_type;

    private Long expires_in;

    public TokenResponse(String access_token, String token_type) {
        this.access_token = access_token;
        this.token_type = token_type;
    }

    public TokenResponse(String access_token, String token_type, Long expires_in) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }
}
