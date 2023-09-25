package ua.mevhen.service

import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.dto.PostResponse

interface PostService {

    PostResponse save(String username, PostRequest request)

    PostResponse update(String id, PostRequest request, String userId)

    void delete(String id, String username)

    void addLike(String username, String postId)

    void removeLike(String username, String postId)

}