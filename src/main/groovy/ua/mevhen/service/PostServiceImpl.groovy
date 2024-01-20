package ua.mevhen.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.mevhen.domain.model.Post
import ua.mevhen.exceptions.PermissionDeniedException
import ua.mevhen.exceptions.PostNotFoundException
import ua.mevhen.mapper.PostMapper
import ua.mevhen.repository.PostRepository

@Service
@Transactional
class PostServiceImpl implements PostService {

    private final PostRepository postRepository
    private final UserService userService

    PostServiceImpl(PostRepository postRepository,
                    UserService userService) {
        this.postRepository = postRepository
        this.userService = userService
    }

    @Override
    Post findById(def id) {
        if (id instanceof String) {
            id = new ObjectId(id)
        }
        postRepository.findById(id)
                .orElseThrow { new PostNotFoundException("Post with ID: $id not found.") }
    }

    @Override
    @Transactional
    Post save(String username, String content) {
        def author = userService.findByUsername(username)
        def post = PostMapper.toPost(content, author)
        return postRepository.save(post)
    }

    @Override
    @Transactional
    Post update(def id, String content, String username) {
        if (id instanceof String) {
            id = new ObjectId(id)
        }
        def post = findById(id)

        if (post.author.username != username) {
            final message = "User $username can't change the post with ID: $id. Permission denied."
            throw new PermissionDeniedException(message)
        }
        post.content = content
        return postRepository.save(post)
    }

    @Override
    @Transactional
    Post update(Post post) {
        if (post.id == null) throw new IllegalArgumentException("Missing unique identifier.")
        return postRepository.save(post)
    }

    @Override
    @Transactional
    void delete(def id, String username) {
        if (id instanceof String) {
            id = new ObjectId(id)
        }
        def post = findById(id)

        if (post.author.username != username) {
            final message = "User $username can't delete the post with ID: $id. Permission denied."
            throw new PermissionDeniedException(message)
        }
        postRepository.delete(post)
    }

    @Override
    @Transactional
    void addLike(String username, def postId) {
        if (postId instanceof String) {
            postId = new ObjectId(postId)
        }
        updateReaction(username, postId, { post, user -> post.addLike(user) })
    }

    @Override
    @Transactional
    void removeLike(String username, def postId) {
        if (postId instanceof String) {
            postId = new ObjectId(postId)
        }
        updateReaction(username, postId, { post, user -> post.removeLike(user) })
    }

    private updateReaction(String username, ObjectId postId, Closure<Post> reactionAction) {
        def user = userService.findByUsername(username)
        def post = postRepository.findById(postId)
                .orElseThrow { new PostNotFoundException("Post with ID: '${postId.toString()}' not found") }
        reactionAction(post, user)
        postRepository.save(post)
    }
}
