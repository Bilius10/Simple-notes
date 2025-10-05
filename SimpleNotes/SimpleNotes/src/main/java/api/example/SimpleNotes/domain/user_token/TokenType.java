package api.example.SimpleNotes.domain.user_token;

import lombok.Getter;

@Getter
public enum TokenType {

    EMAIL_VERIFICATION_TOKEN("Email Verification Token"),
    PASSWORD_RESET_TOKEN("Password Reset Token");

    private String tokenType;

    TokenType(String tokenType) {
         this.tokenType = tokenType;
     }


}
