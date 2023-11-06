package net.frozenblock.configurableeverything.util

import java.util.*

fun <T> T.optional(): Optional<T & Any> = Optional.ofNullable(this)
