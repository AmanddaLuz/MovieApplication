package com.amandaluz.hawk.movierepository

import com.amandaluz.hawk.ModuleHawk

class MovieCacheRepositoryImpl(private val hawk: ModuleHawk) : MovieCacheRepository {
    override fun <T> add(key: String, obj: T) {
        hawk.put(key, obj)
    }

    override fun delete(key: String) {
        if (hawk.contains(key))
            hawk.delete(key)
    }

    override fun <T> get(key: String): T = hawk.get(key)

}