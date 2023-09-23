package ua.mevhen.exceptions

class UserAlreadyExistsException extends RuntimeException {

    UserAlreadyExistsException(String message) {
        super(message)
    }

}
