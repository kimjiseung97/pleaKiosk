package kiosk.pleaKiosk.domain.dto.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;

public class PageRequeust {

    @Min(1)
    private int page = 1;

    @Min(1)
    private int size = 10;

    private String sort = "id";

    // getters and setters

    public Pageable toPageable() {
        return PageRequest.of(page - 1, size, Sort.by(sort));
    }
}
