package api.example.SimpleNotes.infrastructure.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null) {
            return true;
        }

        List<String> errors = getStrings(password);


        if (!errors.isEmpty()) {

            String message = "A senha é inválida: " + String.join(", ", errors) + ".";

            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();

            return false;
        }

        return true;
    }

    private static List<String> getStrings(String password) {
        List<String> errors = new ArrayList<>();

        if (!password.matches(".*[a-z].*")) {
            errors.add("deve conter pelo menos uma letra minúscula");
        }

        if (!password.matches(".*[A-Z].*")) {
            errors.add("deve conter pelo menos uma letra maiúscula");
        }

        if (!password.matches(".*\\d.*")) {
            errors.add("deve conter pelo menos um número");
        }

        if (!password.matches(".*[./?#%&*@!$^-].*")) {
            errors.add("deve conter pelo menos um caractere especial (ex: ./#@&*)");
        }

        return errors;
    }
}
