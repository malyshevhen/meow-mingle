package ua.mevhen.service


import ua.mevhen.domain.model.Post

interface PostService {

    Post findById(def id)

    Post save(String username, String content)

    Post update(def id, String content, String username)

    Post update(Post post)

    void delete(def id, String username)

    void addLike(String username, def postId)

    void removeLike(String username, def postId)

}