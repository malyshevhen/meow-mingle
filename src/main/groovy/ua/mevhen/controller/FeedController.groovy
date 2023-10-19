package ua.mevhen.controller

import groovy.util.logging.Slf4j
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.api.FeedApi
import ua.mevhen.dto.PagePost
import ua.mevhen.mapper.PagePostMapper
import ua.mevhen.service.FeedService

@Slf4j
@RestController
@RequestMapping('/api')
class FeedController implements FeedApi {

    private final FeedService feedService
    private final PagePostMapper pagePostMapper

    FeedController(FeedService feedService, PagePostMapper pagePostMapper) {
        this.feedService = feedService
        this.pagePostMapper = pagePostMapper
    }

    @Override
    ResponseEntity<PagePost> getOwnFeed(Integer size, Integer page) {
        log.info("Request to receive the owner's feed.")

        def username = SecurityContextHolder.context.authentication.name
        def pageable = Pageable.ofSize(size).withPage(page)
        def feedPage = pagePostMapper.toPagePost(feedService.getFeed(username, pageable))

        log.info("Page $page of size $size of Posts for user: $username is retrieved.")

        return new ResponseEntity<>(feedPage, HttpStatus.OK)
    }

    @Override
    ResponseEntity<PagePost> getUserFeed(String username, Integer size, Integer page) {
        log.info("Request to receive '$username' user's feed with")

        def pageable = Pageable.ofSize(size).withPage(page)
        def feedPage = pagePostMapper.toPagePost(feedService.getFeed(username, pageable))

        log.info("Page $page of size $size of Posts for user: $username is retrieved.")

        return new ResponseEntity<>(feedPage, HttpStatus.OK)
    }
}
