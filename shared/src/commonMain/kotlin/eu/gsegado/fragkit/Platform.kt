package eu.gsegado.fragkit

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform