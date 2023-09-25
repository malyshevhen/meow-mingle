package ua.mevhen.service

import ua.mevhen.domain.dto.CommentRequest

interface CommentService {

    void save(String username, String postId, CommentRequest request)

    void update(String username, String postId, CommentRequest request)

    void delete(String username, String postId, String commentId)

}