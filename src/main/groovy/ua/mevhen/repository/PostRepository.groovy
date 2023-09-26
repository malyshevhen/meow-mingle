package ua.mevhen.repository

import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import ua.mevhen.domain.model.Post

interface PostRepository extends MongoRepository<Post, ObjectId> {

    @Query(value = "{'author.id': ?0}")
    Page<Post> findByAuthorId(ObjectId id, Pageable pageable)

}