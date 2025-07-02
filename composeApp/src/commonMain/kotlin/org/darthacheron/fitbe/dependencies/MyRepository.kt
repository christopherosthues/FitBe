package org.darthacheron.fitbe.dependencies

interface MyRepository {
    fun helloWorld(): String
}

class MyRepositoryImp(private val dbClient: DbClient): MyRepository {
    override fun helloWorld(): String {
        return "Hello World!"
    }
}