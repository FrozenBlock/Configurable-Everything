package net.frozenblock.configurableeverything.scripting.util

/**
 * Import other script(s)
 */
@Target(AnnotationTarget.FILE)
@Repeatable
@Retention(AnnotationRetention.RUNTIME)
annotation class Import(vararg val paths: String)

/**
 * Compiler options that will be applied on script compilation
 *
 * @see [kotlin.script.experimental.api.compilerOptions]
 */
@Target(AnnotationTarget.FILE)
@Repeatable
@Retention(AnnotationRetention.SOURCE)
annotation class CompilerOptions(vararg val options: String)
