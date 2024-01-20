package ua.mevhen.domain.model

import java.util.function.Function

interface Mappable<T> {

    default <M> M map(Function<T, M> mapFunction) {
        return mapFunction.apply(this as T)
    }

}