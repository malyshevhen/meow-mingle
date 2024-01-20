package ua.mevhen.mapper


import ua.mevhen.domain.dto.CommentRequest
import ua.mevhen.domain.dto.CommentResponse
import ua.mevhen.domain.model.Comment
import ua.mevhen.domain.model.Post
import ua.mevhen.domain.model.User

abstract class CommentMapper {

    static CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
            id: comment.id.toString(),
            authorId: comment.author.id.toString(),
            content: comment.content,
            created: comment.created,
            updated: comment?.updated
        )
    }

    static Comment toComment(CommentRequest request) {
        return new Comment(content: request.content())
    }

    static Comment toComment(CommentRequest request, User author, Post post) {
        return new Comment(content: request.content(), author: author, post: post)
    }

    static CommentRequest toRequest(Comment comment) {
        return new CommentRequest(content: comment.content)
    }

}
