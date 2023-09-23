package ua.mevhen.service.impl

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.domain.model.User
import ua.mevhen.exceptions.UserNotFoundException
import ua.mevhen.mapper.UserMapper
import ua.mevhen.repository.UserRepository
import ua.mevhen.service.UserService

@Service
class UserServiceImpl implements UserService {

    private final UserRepository userRepository
    private final UserMapper userMapper

    UserServiceImpl(
        UserRepository userRepository,
        UserMapper userMapper
    ) {
        this.userRepository = userRepository
        this.userMapper = userMapper
    }

    @Override
    User findById(String id) {
        userRepository.findById(id)
            .orElseThrow { -> new UserNotFoundException("User with id: $id was not found.") }
    }

    @Override
    @Transactional
    UserInfo save(UserRegistration regForm) {
        def userToSave = userMapper.toUser(regForm)
        def savedUser = userRepository.save(userToSave)
        return userMapper.toUserInfo(savedUser)
    }

    @Override
    @Transactional
    UserInfo updateUsername(String id, String username) {
        def user = findById(id)
        user.username = username
        def updatedUser = userRepository.save(user)
        return userMapper.toUserInfo(updatedUser)
    }

    @Override
    @Transactional
    void deleteById(String id) {
        def user = findById(id)
        user.subscribers.each { unsubscribe(it, user.id) }
        userRepository.delete(user)
    }

    private subscribe(String userId, String subId) {
        def user = findById(userId)
        def subscription = findById(subId)

        user.subscribe(subscription)

        userRepository.saveAll { [user, subscription] }
    }

    private unsubscribe(ObjectId userId, ObjectId subscrId) {
        def user = findById(userId)
        def subscription = findById(subscrId)

        user.unsubscribe(subscription)

        userRepository.saveAll { [user, subscription] }
    }
}
