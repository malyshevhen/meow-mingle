package ua.mevhen.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ua.mevhen.domain.dto.PostResponse

interface FeedService {

    Page<PostResponse> getFeed(String username, Pageable pageable)

}