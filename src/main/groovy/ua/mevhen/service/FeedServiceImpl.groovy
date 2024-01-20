package ua.mevhen.service


import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ua.mevhen.domain.dto.PostResponse
import ua.mevhen.domain.model.Post
import ua.mevhen.exceptions.UserNotFoundException
import ua.mevhen.mapper.PostMapper
import ua.mevhen.repository.PostRepository

@Service
class FeedServiceImpl implements FeedService {

    final PostRepository postRepository
    final UserService userService

    FeedServiceImpl(PostRepository postRepository,
                    UserService userService) {
        this.postRepository = postRepository
        this.userService = userService
    }

    @Override
    Page<Post> getFeed(String username, Pageable pageable) {
        if (!userService.ifExists(username)) {
            throw new UserNotFoundException("User with username: '$username' not found.")
        }
        def subscriptionIds = userService.findByUsername(username)
                .subscriptions
                .collect { it.id }
        return postRepository.findAllByAuthorIdIn(subscriptionIds, pageable)
    }

}
