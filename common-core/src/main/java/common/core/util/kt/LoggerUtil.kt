package common.core.util.kt

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LoggerUtil

inline fun <reified R : Any> R.logger(): Logger =
    LoggerFactory.getLogger(this::class.java.name.substringBefore("\$Companion"))