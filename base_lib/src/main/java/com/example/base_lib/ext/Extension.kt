package com.example.base_lib.ext

import android.view.LayoutInflater
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.core.qualifier.TypeQualifier
import org.koin.core.scope.Scope
import org.koin.ext.getFullName


fun <T> getViewBinding(inflater: LayoutInflater, clazz: Class<T>) =
    clazz.getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, inflater)
        .convert<T>()

@Suppress("UNCHECKED_CAST")
fun <T> Any.convert() = this as T


fun <T : Any> T.getScopeName() = TypeQualifier(this::class)
fun <T : Any> T.getScopeId() = this::class.getFullName() + "@" + System.identityHashCode(this)

val <T : Any> T.scope: Scope
    get() = getOrCreateScope()

fun <T : Any> T.getOrCreateScope(): Scope {
    val koin = GlobalContext.get()
    return getScopeOrNull(koin) ?: createScope(koin)
}

fun <T : Any> T.getOrCreateScope(koin: Koin): Scope {
    val scopeId = getScopeId()
    return koin.getScopeOrNull(scopeId) ?: koin.createScope(scopeId, getScopeName(), this)
}

private fun <T : Any> T.getScopeOrNull(koin: Koin = GlobalContext.get()): Scope? {
    val scopeId = getScopeId()
    return koin.getScopeOrNull(scopeId)
}

private fun <T : Any> T.createScope(koin: Koin): Scope {
    return koin.createScope(getScopeId(), getScopeName(), this)
}