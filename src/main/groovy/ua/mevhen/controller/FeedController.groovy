package ua.mevhen.controller


import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ua.mevhen.mapper.PostMapper
import ua.mevhen.service.FeedService

import java.security.Principal

@RestController
@RequestMapping('/api/feed')
class FeedController {

    private final FeedService feedService

    FeedController(FeedService feedService) {
        this.feedService = feedService
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    def ownerFeed(Principal principal, @RequestParam Integer size, @RequestParam Integer page) {
        def username = principal.getName()
        def pageable = Pageable.ofSize(size).withPage(page)
        return feedService.getFeed(username, pageable).map(PostMapper::toResponse)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping('/{username}')
    def userFeed(@PathVariable String username, @RequestParam Integer size, @RequestParam Integer page) {
        def pageable = Pageable.ofSize(size).withPage(page)
        return feedService.getFeed(username, pageable).map(PostMapper::toResponse)
    }
}
