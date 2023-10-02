package ua.mevhen.mapper

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ua.mevhen.dto.PagePost

@Component
class PagePostMapper {

    PagePost toPagePost(Page page) {
        return new PagePost(
            totalPages: page.totalPages,
            totalElements: page.totalElements,
            first: page.first,
            last: page.last,
            sort: page.sort,
            size: page.size,
            content: page.content,
            number: page.number,
            numberOfElements: page.numberOfElements,
            empty: page.empty,
            pageable: page.pageable
        )
    }
}
