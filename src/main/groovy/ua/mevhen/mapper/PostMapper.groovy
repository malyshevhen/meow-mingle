package ua.mevhen.mapper


import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.dto.PostResponse
import ua.mevhen.domain.model.Post
import ua.mevhen.domain.model.User

abstract class PostMapper {

    static PostResponse toResponse(Post post) {
        def likes = post.likes.collect { UserMapper.toUserInfo(it as User) }
        def comments = post.comments.collect { CommentMapper.toResponse(it) }

        return new PostResponse(
            id: post.id.toString(),
            authorId: post.author.id.toString(),
            content: post.content,
            likes: likes,
            comments: comments,
            created: post.created,
            updated: post?.updated
        )
    }

    static Post toPost(String content) {
        return new Post(
            content: content
        )
    }

    static Post toPost(String content, User author) {
        return new Post(
                content: content,
                author: author
        )
    }

}
