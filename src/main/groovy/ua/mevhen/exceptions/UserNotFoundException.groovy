package ua.mevhen.exceptions

class UserNotFoundException extends RuntimeException {

    UserNotFoundException(String message) {
        super(message)
    }

}
