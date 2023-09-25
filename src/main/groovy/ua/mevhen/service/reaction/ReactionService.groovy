package ua.mevhen.service.reaction

import ua.mevhen.domain.events.Reaction

interface ReactionService {

    void manageReaction(Reaction reaction)

}