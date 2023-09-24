package ua.mevhen.service.impl

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.dto.PostResponse
import ua.mevhen.domain.model.Post
import ua.mevhen.exceptions.PermissionDeniedException
import ua.mevhen.exceptions.PostNotFoundException
import ua.mevhen.mapper.PostMapper
import ua.mevhen.repository.PostRepository
import ua.mevhen.service.PostService
import ua.mevhen.service.UserService

@Service
class PostServiceImpl implements PostService {

    private final PostRepository postRepository
    private final UserService userService
    private final PostMapper postMapper

    PostServiceImpl(
        PostRepository postRepository,
        UserService userService,
        PostMapper postMapper
    ) {
        this.postRepository = postRepository
        this.userService = userService
        this.postMapper = postMapper
    }

    @Override
    @Transactional
    PostResponse save(String username, PostRequest request) {
        def user = userService.findByUsername(username)
        def post = postMapper.toPost(request)
        post.author = user
        def savedPost = postRepository.save(post)

        return postMapper.toResponse(savedPost)
    }

    @Override
    @Transactional
    PostResponse update(String id, PostRequest request, String username) {
        def post = findById(id)

        if (post.author.username == username) {
            post.content = request.content
            def updatedPost = postRepository.save(post)

            return postMapper.toResponse(updatedPost)
        } else {
            throw new PermissionDeniedException("User $username can`t change the post." +
                " Permission denied.")
        }
    }

    @Override
    @Transactional
    void delete(String id, String username) {
        def post = findById(id)

        if (post.author.username == username) {
            postRepository.delete(post)
        } else {
            throw new PermissionDeniedException("User $username can`t delete the post." +
                " Permission denied.")
        }
    }

    private Post findById(String id) {
        postRepository.findById(new ObjectId(id))
            .orElseThrow { -> new PostNotFoundException("Post with ID: $id not exist.") }
    }

}
