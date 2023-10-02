package ua.mevhen.mapper

import org.springframework.stereotype.Component
import ua.mevhen.dto.CommentRequest
import ua.mevhen.dto.CommentResponse
import ua.mevhen.domain.model.Comment

@Component
class CommentMapper {

    CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
            id: comment.id.toString(),
            authorId: comment.author.id.toString(),
            content: comment.content,
            created: comment.created,
            updated: comment?.updated
        )
    }

    Comment toComment(CommentRequest request) {
        return new Comment(content: request.content)
    }

    CommentRequest toRequest(Comment comment) {
        return new CommentRequest(content: comment.content)
    }

}
