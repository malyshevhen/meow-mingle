package ua.mevhen.service


import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.mevhen.domain.dto.CommentRequest
import ua.mevhen.domain.dto.CommentResponse
import ua.mevhen.domain.model.Comment
import ua.mevhen.exceptions.CommentNotFoundException
import ua.mevhen.exceptions.PermissionDeniedException
import ua.mevhen.mapper.CommentMapper
import ua.mevhen.repository.CommentRepository

@Service
class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository
    private final UserService userService
    private final PostService postService

    CommentServiceImpl(CommentRepository commentRepository,
                       UserService userService,
                       PostService postService) {
        this.commentRepository = commentRepository
        this.userService = userService
        this.postService = postService
    }

    @Override
    @Transactional
    CommentResponse save(String username, def postId, CommentRequest request) {
        if (postId instanceof String) {
            postId = new ObjectId(postId)
        }
        def post = postService.findById(postId)
        def author = userService.findByUsername(username)
        def commentToSave = CommentMapper.toComment(request, author, post)

        def savedComment = commentRepository.save(commentToSave)

        post.addComment(savedComment)
        postService.update(post)

        return CommentMapper.toResponse(savedComment)
    }

    @Override
    @Transactional
    CommentResponse update(String username, def commentId, CommentRequest request) {
        if (commentId instanceof String) {
            commentId = new ObjectId(commentId)
        }
        def commentToUpdate = findById(commentId)
        if (commentToUpdate.author.username != username) {
            throw new PermissionDeniedException("Comment ID: ${commentToUpdate.id} not canged. Permission denied")
        }
        commentToUpdate.content = request.content()
        def updatedComment = commentRepository.save(commentToUpdate)

        return CommentMapper.toResponse(updatedComment)
    }

    @Override
    @Transactional
    void delete(String username, def commentId) {
        if (commentId instanceof String) {
            commentId = new ObjectId(commentId)
        }
        def commentToDelete = findById(commentId)
        if (commentToDelete.author.username != username) {
            throw new PermissionDeniedException("Comment ID: ${commentToDelete.id} not canged. Permission denied")
        }
        commentRepository.delete(commentToDelete)
    }

    @Override
    Page<CommentResponse> getByPostId(def postId, Pageable pageable) {
        if (postId instanceof String) {
            postId = new ObjectId(postId)
        }
        return commentRepository
                .findByPostId(postId, pageable)
                .map(CommentMapper::toResponse)
    }

    private Comment findById(ObjectId commentId) {
        commentRepository.findById(commentId)
                .orElseThrow { new CommentNotFoundException("Comment with ID: $commentId not found.") }
    }
}
