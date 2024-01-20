package ua.mevhen.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ua.mevhen.domain.dto.PostResponse
import ua.mevhen.domain.model.Post

interface FeedService {

    Page<Post> getFeed(String username, Pageable pageable)

}