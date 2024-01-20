package ua.mevhen.service


import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ua.mevhen.domain.dto.CommentRequest
import ua.mevhen.domain.dto.CommentResponse

interface CommentService {

    CommentResponse save(String username, def postId, CommentRequest request)

    CommentResponse update(String username, def commentId, CommentRequest request)

    void delete(String username, def commentId)

    Page<CommentResponse> getByPostId(def postId, Pageable pageable)

}