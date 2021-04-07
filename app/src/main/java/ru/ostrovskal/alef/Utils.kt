@file:Suppress("DEPRECATION", "NOTHING_TO_INLINE")

package ru.ostrovskal.alef

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.util.LruCache
import ru.ostrovskal.alef.Common.*
import android.text.format.Time
import kotlin.math.roundToInt

/** Форматирование времени */
fun fmtTime(v: Long, pattern: String): String {
    val time = Time()
    time.set(v)
    return time.format(pattern)
}

/** Возвращает дату и время */
inline val Long.datetime    get() = fmtTime(this, "%d.%m.%Y %H:%M:%S")

/** Вывод отладочной информации об объекте в лог */
inline fun <T> T.debug()    { if(isDebug) Log.d(logTag, toString()) }

/** Вывод информации о объекте в лог */
inline fun <T> T.info()     = Log.i(logTag, toString())

/** Преобразование целых экранных координат в пиксели */
inline val Int.dp           get()   = (this * Config.density * Config.mulSW).roundToInt()

/**
 * Запуск логирования
 *
 * @param context       Контекст
 * @param tag           Тэг лога
 * @param appVersion    Версия программы
 * @param buildConfig   Признак отладки
 */
fun startLog(context: Context, tag: String, appVersion: String, buildConfig: Boolean) {
    folderCache = context.cacheDir.path
    folderFiles = context.filesDir.path
    folderData = context.getDatabasePath(tag).path
    namePackage = context.packageName
    logTag      = tag
    isDebug     = buildConfig
    Config.query(context)

    "--------------------------------------------------------".info()
    "Executed $logTag $appVersion - ${System.currentTimeMillis().datetime}".info()
    Config.debug()
    "--------------------------------------------------------".info()
    "".info()
}

