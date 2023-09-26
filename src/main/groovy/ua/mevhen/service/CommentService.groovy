package ua.mevhen.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ua.mevhen.domain.dto.CommentRequest
import ua.mevhen.domain.dto.CommentResponse

interface CommentService {

    CommentResponse save(String username, String postId, CommentRequest request)

    CommentResponse update(String username, String commentId, CommentRequest request)

    void delete(String username, String commentId)

    Page<CommentResponse> getByPostId(String postId, Pageable pageable)

}