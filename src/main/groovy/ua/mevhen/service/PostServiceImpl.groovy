package ua.mevhen.service

import groovy.util.logging.Slf4j
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.dto.PostResponse
import ua.mevhen.domain.model.Post
import ua.mevhen.exceptions.PermissionDeniedException
import ua.mevhen.exceptions.PostNotFoundException
import ua.mevhen.mapper.PostMapper
import ua.mevhen.repository.PostRepository

@Service
@Slf4j
class PostServiceImpl implements PostService {

    private final PostRepository postRepository
    private final UserService userService
    private final PostMapper postMapper

    PostServiceImpl(
        PostRepository postRepository,
        UserService userService,
        PostMapper postMapper
    ) {
        this.postRepository = postRepository
        this.userService = userService
        this.postMapper = postMapper
    }

    @Override
    @Transactional
    PostResponse save(String username, PostRequest request) {
        def user = userService.findByUsername(username)
        def post = postMapper.toPost(request)
        post.author = user
        def savedPost = postRepository.save(post)
        log.info("Saved post with ID: ${ savedPost.id } by user: $username")
        return postMapper.toResponse(savedPost)
    }

    @Override
    @Transactional
    PostResponse update(String id, PostRequest request, String username) {
        def post = findById(id)

        if (post.author.username == username) {
            post.content = request.content
            def updatedPost = postRepository.save(post)
            log.info("Updated post with ID: ${ updatedPost.id } by user: $username")
            return postMapper.toResponse(updatedPost)
        } else {
            final message = "User $username can't change the post with ID: $id. Permission denied."
            log.error(message)
            throw new PermissionDeniedException(message)
        }
    }

    @Override
    @Transactional
    void delete(String id, String username) {
        def post = findById(id)

        if (post.author.username == username) {
            postRepository.delete(post)
            log.info("Deleted post with ID: $id by user: $username")
        } else {
            final message = "User $username can't delete the post with ID: $id. Permission denied."
            log.error(message)
            throw new PermissionDeniedException(message)
        }
    }

    @Override
    @Transactional
    void addLike(String username, String postId) {
        log.info("Process adding like of User: $username to post with ID: $postId ...")
        updateReaction(username, postId, { post, user -> post.addLike(user) })
        log.info("Adding like of User: $username to post with ID: $postId completed")
    }

    @Override
    @Transactional
    void removeLike(String username, String postId) {
        log.info("Process removing like of User: $username from post with ID: $postId ...")
        updateReaction(username, postId, { post, user -> post.removeLike(user) })
        log.info("REmoving like of User: $username from post with ID: $postId completed")
    }

    private updateReaction(
        String username,
        String postId,
        Closure<Post> reactionAction
    ) {
        def user = userService.findByUsername(username)
        def post = postRepository.findById(new ObjectId(postId))
            .orElseThrow { ->
                def message = "Post with ID: '$postId' not found"
                log.error(message)
                new PostNotFoundException(message)
            }

        reactionAction(post, user)

        postRepository.save(post)
    }

    private Post findById(String id) {
        postRepository.findById(new ObjectId(id))
            .orElseThrow { ->
                final message = "Post with ID: $id not found."
                log.error(message)
                new PostNotFoundException(message)
            }
    }

}
