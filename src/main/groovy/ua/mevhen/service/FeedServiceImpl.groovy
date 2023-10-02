package ua.mevhen.service

import groovy.util.logging.Slf4j
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ua.mevhen.dto.PostResponse
import ua.mevhen.domain.model.User
import ua.mevhen.exceptions.UserNotFoundException
import ua.mevhen.mapper.PostMapper
import ua.mevhen.repository.PostRepository

@Slf4j
@Service
class FeedServiceImpl implements FeedService {

    private final PostRepository postRepository
    private final UserService userService
    private final PostMapper postMapper

    FeedServiceImpl(
        PostRepository postRepository,
        UserService userService,
        PostMapper postMapper
    ) {
        this.postRepository = postRepository
        this.userService = userService
        this.postMapper = postMapper
    }

    @Override
    Page<PostResponse> getFeed(String username, Pageable pageable) {
        if (!userService.ifExists(username)) {
            def message = "User with username: '$username' not found."
            log.error(message)
            throw new UserNotFoundException(message)
        }
        def user = userService.findByUsername(username)
        def subscriptionIds = user.subscriptions.collect {it.id}
        def pageOfPosts = postRepository.findAllByAuthorsId(subscriptionIds, pageable)
        log.info("Retrieve feed for user: '$username'.")
        return pageOfPosts.map(postMapper::toResponse)
    }

}
