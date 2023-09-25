package ua.mevhen.service.reaction

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service
import ua.mevhen.domain.events.Reaction
import ua.mevhen.service.PostService

import static ua.mevhen.domain.events.ReactionOperation.LIKE
import static ua.mevhen.domain.events.ReactionOperation.UNLIKE

@Service
@Slf4j
class ReactionServiceImpl implements ReactionService {

    private final PostService postService

    ReactionServiceImpl(PostService postService) {
        this.postService = postService
    }

    @Override
    void manageReaction(Reaction reaction) {
        switch (reaction.operation()) {
            case LIKE -> {
                log.info("Add like from User: ${ reaction.username } to post: ${ reaction.postId }...")
                postService.addLike(reaction.username, reaction.postId)
                log.info("Like from User: '${ reaction.username } to post: ${ reaction.postId } added.")
            }
            case UNLIKE -> {
                log.info("Removing Users: '${ reaction.username }' like from post: ${ reaction.postId }...")
                postService.removeLike(reaction.username, reaction.postId)
                log.info("Like from User: '${ reaction.username } to post: ${ reaction.postId } removed.")
            }
        }
    }

}
