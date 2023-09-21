package ua.mevhen.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration
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
    @Transactional
    UserInfo save(UserRegistration regForm) {
        def userToSave = userMapper.toUser(regForm)
        def savedUser = userRepository.save(userToSave)
        return userMapper.toUserInfo(savedUser)
    }
}
