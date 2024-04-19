package root.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;
import root.enums.SortOrderEnum;

@Data
@AllArgsConstructor
@Builder
public class SearchDTO {
    private String keyword;
    private Integer currentPage;
    private Integer size;
    private String sortBy;
    private SortOrderEnum sortOrder;

    public SearchDTO() {
        keyword = "";
        currentPage = 0;
        size = 10;
        sortBy = "id";
        sortOrder = SortOrderEnum.DESC;
    }
}
