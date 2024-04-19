package root.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageDTO<T> {
    private int totalPage;
    private long totalElements;
    private T data;
}
