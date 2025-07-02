package org.darthacheron.fitbe

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform