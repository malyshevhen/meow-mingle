package ua.mevhen.mapper

import org.springframework.stereotype.Component
import ua.mevhen.domain.dto.CommentRequest
import ua.mevhen.domain.dto.CommentResponse
import ua.mevhen.domain.model.Comment

@Component
class CommentMapper {

    CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
            id: comment.id,
            authorId: comment.author.id,
            content: comment.content,
            created: comment.created,
            updated: comment?.updated
        )
    }

    Comment toComment(CommentRequest request) {
        return new Comment(content: request.content)
    }

}
