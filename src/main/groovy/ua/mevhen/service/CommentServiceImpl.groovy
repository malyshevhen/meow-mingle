package ua.mevhen.service

import groovy.util.logging.Slf4j
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.mevhen.domain.dto.CommentRequest
import ua.mevhen.domain.model.Post
import ua.mevhen.exceptions.CommentNotFoundException
import ua.mevhen.mapper.CommentMapper
import ua.mevhen.repository.CommentRepository

@Service
@Slf4j
class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository
    private final UserService userService
    private final PostService postService
    private final CommentMapper commentMapper

    CommentServiceImpl(
        CommentRepository commentRepository,
        UserService userService,
        PostService postService,
        CommentMapper commentMapper
    ) {
        this.commentRepository = commentRepository
        this.userService = userService
        this.postService = postService
        this.commentMapper = commentMapper
    }

    @Override
    @Transactional
    void save(String username, String postId, CommentRequest request) {
        changeComment(username, postId, request, { post, comment -> post.addComment(comment) })
    }

    @Override
    @Transactional
    void update(String username, String postId, CommentRequest request) {
        changeComment(username, postId, request, { post, comment -> post.updateComment(comment) })
    }

    @Override
    @Transactional
    void delete(String username, String postId, String commentId) {
        def existingComment = commentRepository.findById(new ObjectId(commentId))
            .orElseThrow { ->
                new CommentNotFoundException("Comment with ID: $commentId not found.")
            }

        def request = commentMapper.toRequest(existingComment)
        changeComment(username, postId, request, { post, comment -> post.removeComment(comment) })
    }

    private void changeComment(
        String username,
        String postId,
        CommentRequest request,
        Closure<Post> postClosure
    ) {
        def post = postService.findById(postId)
        def user = userService.findByUsername(username)
        def comment = commentMapper.toComment(request)
        comment.author = user

        def savedComment = commentRepository.save(comment)

        postClosure(post, savedComment)

        postService.updateComments(post)
    }

}
