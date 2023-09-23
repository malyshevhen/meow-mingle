package ua.mevhen.mapper

import org.springframework.stereotype.Component
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.dto.PostResponse
import ua.mevhen.domain.model.Post

@Component
class PostMapper {

    private final CommentMapper commentMapper
    private final UserMapper userMapper

    PostMapper(
        CommentMapper commentMapper,
        UserMapper userMapper
    ) {
        this.commentMapper = commentMapper
        this.userMapper = userMapper
    }

    PostResponse toResponse(Post post) {
        def likes = post.likes
            .collect { userMapper.toUserInfo(it) }
        def comments = post.comments
            .collect { commentMapper.toResponse(it) }

        return new PostResponse(
            id: post.id,
            authorId: post.author.id,
            content: post.content,
            likes: likes,
            comments: comments,
            created: post.created,
            updated: post?.updated
        )
    }

    Post toPost(PostRequest request) {
        return new Post(
            content: request.content
        )
    }

}
