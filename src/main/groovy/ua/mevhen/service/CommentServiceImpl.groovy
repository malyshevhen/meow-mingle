package ua.mevhen.service

import groovy.util.logging.Slf4j
import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.mevhen.domain.model.Comment
import ua.mevhen.dto.CommentRequest
import ua.mevhen.dto.CommentResponse
import ua.mevhen.exceptions.CommentNotFoundException
import ua.mevhen.exceptions.PermissionDeniedException
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
    CommentResponse save(String username, String postId, CommentRequest request) {
        def post = postService.findById(postId)
        def user = userService.findByUsername(username)
        def comment = commentMapper.toComment(request)
        comment.author = user
        comment.post = post
        post.addComment(comment)

        def savedComment = commentRepository.save(comment)

        postService.update(post)

        return commentMapper.toResponse(savedComment)
    }

    @Override
    @Transactional
    CommentResponse update(String username, String commentId, CommentRequest request) {
        def commentToUpdate = findById(commentId)
        if (commentToUpdate.author.username != username) {
            def message = "Comment ID: ${ commentToUpdate.id } not canged. Permission denied"
            log.error(message)
            throw new PermissionDeniedException(message)
        }
        commentToUpdate.content = request.content
        def updatedComment = commentRepository.save(commentToUpdate)

        return commentMapper.toResponse(updatedComment)
    }

    @Override
    @Transactional
    void delete(String username, String commentId) {
        def commentToDelete = findById(commentId)
        if (commentToDelete.author.username != username) {
            def message = "Comment ID: ${ commentToDelete.id } not canged. Permission denied"
            log.error(message)
            throw new PermissionDeniedException(message)
        }
        commentRepository.delete(commentToDelete)
    }

    @Override
    Page<CommentResponse> getByPostId(String postId, Pageable pageable) {
        return commentRepository
            .findByPostId(new ObjectId(postId), pageable)
            .map(commentMapper::toResponse)
    }

    private Comment findById(String commentId) {
        commentRepository.findById(new ObjectId(commentId))
            .orElseThrow { ->
                def message = "Comment with ID: $commentId not found."
                log.error(message)
                new CommentNotFoundException(message)
            }
    }

}
