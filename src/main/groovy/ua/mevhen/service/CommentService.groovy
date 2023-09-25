package ua.mevhen.service

import ua.mevhen.domain.dto.CommentRequest
import ua.mevhen.domain.dto.CommentResponse

interface CommentService {

    CommentResponse save(String username, String postId, CommentRequest request)

    CommentResponse update(String username, String commentId, CommentRequest request)

    void delete(String username, String commentId)

}