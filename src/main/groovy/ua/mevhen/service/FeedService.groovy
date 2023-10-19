package ua.mevhen.service


import org.springframework.data.domain.Pageable
import ua.mevhen.dto.PagePost

interface FeedService {

    PagePost getFeed(String username, Pageable pageable)

}