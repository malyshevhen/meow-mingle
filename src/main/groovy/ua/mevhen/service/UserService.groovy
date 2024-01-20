package ua.mevhen.service


import ua.mevhen.domain.model.User

interface UserService {

    User findByUsername(String username)

    User save(User userToSave)

    User updateUsername(def id, String username)

    void deleteById(def id)

    void subscribe(String username, def subId)

    void unsubscribe(String username, def subId)

    boolean ifExists(String username)

}