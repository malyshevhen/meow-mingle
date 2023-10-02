package ua.mevhen.utils

import ua.mevhen.dto.CommentRequest

class Validator {

    static void validateCommentRequest(CommentRequest request, String message) {
        if (request == null || request.content == null || request.content.length() <= 0) {
            throw new IllegalArgumentException(message)
        }
    }

    static void validatePageableArgs(Integer size, Integer page, String message) {
        if (size == null || page == null || size < 0 || page < 0) {
            throw new IllegalArgumentException(message)
        }
    }

    static void validateStringArg(String arg, String message) {
        if(arg == null || arg.length() <= 0) {
            throw new IllegalArgumentException(message)
        }
    }

    static void validateIntegerArg(Integer arg, String message) {
        if(arg == null || arg < 0) {
            throw new IllegalArgumentException(message)
        }
    }

}
