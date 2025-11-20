package api.example.SimpleNotes.infrastructure.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageDTO<T>(
        List<T> data,
        int page,
        int size,
        long totalElements,
        long totalPages
) {
    public PageDTO(Page<T> responsePage) {
        this(responsePage.getContent(),
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages()
        );
    }
}
