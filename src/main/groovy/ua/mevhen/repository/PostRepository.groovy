package ua.mevhen.repository

import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import ua.mevhen.domain.model.Post

interface PostRepository extends MongoRepository<Post, ObjectId> {

    Page<Post> findAllByAuthorUsername(String username, Pageable pageable)

}