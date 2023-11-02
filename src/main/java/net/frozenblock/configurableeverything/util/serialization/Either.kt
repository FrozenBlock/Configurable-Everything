package net.frozenblock.configurableeverything.util.serialization

import com.mojang.datafixers.kinds.App
import com.mojang.datafixers.kinds.K1
import kotlinx.serialization.Serializable
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import com.mojang.datafixers.util.Either as DFUEither

fun DFUEither<*, *>.isLeft(): Boolean {
    var left: Boolean = false
    ifLeft {
        left = true
    }
    return left
}

fun DFUEither<*, *>.isRight(): Boolean {
    var right: Boolean = false
    ifRight {
        right = true
    }
    return right
}

fun <L : Any, R : Any> Either<L, R>.toDFU(): DFUEither<L, R> {
    if (isLeft()) {
        return DFUEither.left(this.left().orElseThrow())
    }
    if (isRight()) {
        return DFUEither.right(this.right().orElseThrow())
    }
    throw IllegalStateException("Either isn't left or right??")
}

fun <L : Any, R : Any> DFUEither<L, R>.toKt(): Either<L, R> {
    if (isLeft()) {
        return Either.left(this.left().orElseThrow())
    }
    if (isRight()) {
        return Either.right(this.right().orElseThrow())
    }
    throw IllegalStateException("Either isn't left or right??")
}

@Serializable
sealed class Either<L : Any, R : Any> private constructor() : App<Either.Mu<R>, L> {
    companion object {
        fun <L : Any, R : Any> unbox(box: App<Mu<R>, L>): Either<L, R> {
            return box as Either<L, R>
        }

        fun <L : Any, R : Any> left(value: L): Either<L, R> {
            return Left(value)
        }

        fun <L : Any, R : Any> right(value: R): Either<L, R> {
            return Right(value)
        }
    }

    class Mu<R : Any> : K1

    @Serializable
    private class Left<L : Any, R : Any>(private val value: L) : Either<L, R>() {
        override fun <C : Any, D : Any> mapBoth(f1: Function<in L, out C>, f2: Function<in R, out D>): Either<C, D> {
            return Left(f1.apply(value))
        }

        override fun <T : Any> map(l: Function<in L, out T>, r: Function<in R, out T>): T {
            return l.apply(value)
        }

        override fun isLeft(): Boolean = true

        override fun isRight(): Boolean = false

        override fun ifLeft(consumer: Consumer<in L>?): Either<L, R> {
            consumer?.accept(value)
            return this
        }

        override fun ifRight(consumer: Consumer<in R>?): Either<L, R> {
            return this
        }

        override fun left(): Optional<out L> {
            return Optional.of(value)
        }

        override fun right(): Optional<R> {
            return Optional.empty()
        }

        override fun toString(): String {
            return "Left[$value]"
        }

        override fun equals(o: Any?): Boolean {
            if (this === o) {
                return true
            }
            if (o == null || javaClass != o.javaClass) {
                return false
            }
            val left = o as Left<*, *>
            return value == left.value
        }

        override fun hashCode(): Int {
            return value.hashCode()
        }
    }

    @Serializable
    private class Right<L : Any, R : Any>(private val value: R) : Either<L, R>() {
        override fun <C : Any, D : Any> mapBoth(f1: Function<in L, out C>, f2: Function<in R, out D>): Either<C, D>? {
            return Right(f2.apply(value))
        }

        override fun <T : Any> map(l: Function<in L, out T>, r: Function<in R, out T>): T {
            return r.apply(value)
        }

        override fun isLeft(): Boolean = false

        override fun isRight(): Boolean = true

        override fun ifLeft(consumer: Consumer<in L>?): Either<L, R> {
            return this
        }

        override fun ifRight(consumer: Consumer<in R>?): Either<L, R> {
            consumer?.accept(value)
            return this
        }

        override fun left(): Optional<out L> {
            return Optional.empty()
        }

        override fun right(): Optional<out R> {
            return Optional.of(value)
        }

        override fun toString(): String {
            return "Right[$value]"
        }

        override fun equals(o: Any?): Boolean {
            if (this === o) {
                return true
            }
            if (o == null || javaClass != o.javaClass) {
                return false
            }
            val right = o as Right<*, *>
            return value == right.value
        }

        override fun hashCode(): Int {
            return value.hashCode()
        }
    }

    abstract fun <C : Any, D : Any> mapBoth(f1: Function<in L, out C>, f2: Function<in R, out D>): Either<C, D>?
    abstract fun <T : Any> map(l: Function<in L, out T>, r: Function<in R, out T>): T
    abstract fun isLeft(): Boolean
    abstract fun isRight(): Boolean
    abstract fun ifLeft(consumer: Consumer<in L>?): Either<L, R>
    abstract fun ifRight(consumer: Consumer<in R>?): Either<L, R>
    abstract fun left(): Optional<out L>
    abstract fun right(): Optional<out R>
    fun <T : Any> mapLeft(l: Function<in L, out T>): Either<T, R> {
        return map(
            { t: L -> left(l.apply(t)) },
            { value: R -> right(value) }
        )
    }

    fun <T : Any> mapRight(l: Function<in R, out T>): Either<L, T> {
        return map(
            { value: L -> left(value) },
            { t: R -> right(l.apply(t)) })
    }

    fun orThrow(): L {
        return map({ l: L -> l }) { r: R ->
            if (r is Throwable) {
                throw RuntimeException(r as Throwable)
            }
            throw RuntimeException(r.toString())
        }
    }

    fun swap(): Either<R, L> {
        return map(
            { value: L -> right(value) }
        ) { value: R -> left(value) }
    }

    fun <L2 : Any> flatMap(function: Function<in L, out Either<L2, R>>): Either<L2, R> {
        return map(function) { value: R -> right(value) }
    }
}
