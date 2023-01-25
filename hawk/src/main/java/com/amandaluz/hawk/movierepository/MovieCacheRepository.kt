package com.amandaluz.hawk.movierepository

interface MovieCacheRepository {
    fun <T> add(key: String, obj: T)
    fun delete(key: String)
    fun <T> get(key: String): T
}