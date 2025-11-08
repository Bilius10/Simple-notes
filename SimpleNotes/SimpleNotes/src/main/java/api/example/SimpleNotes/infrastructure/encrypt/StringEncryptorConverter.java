package api.example.SimpleNotes.infrastructure.encrypt;

import api.example.SimpleNotes.infrastructure.exception.ExceptionMessages;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Converter
@Component
@RequiredArgsConstructor
public class StringEncryptorConverter implements AttributeConverter<String, String> {

    private final EncryptionService encryptionService;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            return encryptionService.encrypt(attribute);
        } catch (Exception e) {
            throw new ServiceException(ExceptionMessages.INTERNAL_SERVER_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return encryptionService.decrypt(dbData);
        } catch (Exception e) {
            throw new ServiceException(ExceptionMessages.INTERNAL_SERVER_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
