package ua.mevhen.controller

import groovy.util.logging.Slf4j
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.domain.dto.PostResponse
import ua.mevhen.service.FeedService

import java.security.Principal

@Slf4j
@RestController
@RequestMapping('/api/feed')
class FeedController {

    private final FeedService feedService

    FeedController(FeedService feedService) {
        this.feedService = feedService
    }

    @GetMapping
    Page<PostResponse> ownerFeed(
        Principal principal,
        @RequestParam('size') @NotNull Integer size,
        @RequestParam('page') @NotNull Integer page
    ) {
        log.info("Request to receive the owner's feed.")
        def username = principal.getName()
        def pageable = Pageable.ofSize(size).withPage(page)
        def feedPage = feedService.getFeed(username, pageable)
        log.info("Page $page of size $size of Posts for user: $username is retrieved.")
        return feedPage
    }

    @GetMapping('/{username}')
    Page<PostResponse> userFeed(
        @PathVariable('username') String username,
        @RequestParam('size') Integer size,
        @RequestParam('page') Integer page
    ) {
        log.info("Request to receive '$username' user's feed with")
        def pageable = Pageable.ofSize(size).withPage(page)
        def feedPage = feedService.getFeed(username, pageable)
        log.info("Page $page of size $size of Posts for user: $username is retrieved.")
        return feedPage
    }

}
