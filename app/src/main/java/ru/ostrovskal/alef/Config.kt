package ru.ostrovskal.alef

import android.content.Context
import android.content.res.Configuration

object Config {
    /** Максимальный объем памяти, выделяемый приложению */
    @JvmField var maxMem		= 0

    /** Плотность экрана */
    @JvmField var dip			= 0

    /** Ширина экрана */
    @JvmField var screenWidth	= 0

    /** Высота экрана */
    @JvmField var screenHeight	= 0

    /** Минимальная ширина экрана */
    @JvmField var sw            = 0

    /** Плотность экрана в точных единицах */
    @JvmField var density		= 0f

    /** Плотность масштабирования шрифтов */
    @JvmField var scaledDensity	= 0f

    /** Отношение стандартного размера экрана к текущему */
    @JvmField var mulSW         = 0f

    /** Признак портретной ориентации */
    @JvmField var isPortrait	= false

    /** Запрос конфигурации системы */
    fun query(context: Context) {
        val cfg = context.resources.configuration
        val dMetrics = context.resources.displayMetrics
        sw = cfg.smallestScreenWidthDp
        mulSW = sw / 320f
        maxMem = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        dip = dMetrics.densityDpi
        density = dMetrics.density
        scaledDensity = dMetrics.scaledDensity
        screenHeight = dMetrics.heightPixels
        screenWidth = dMetrics.widthPixels
        isPortrait = cfg.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    /** Строковое представление объекта */
    override fun toString(): String {
        return "Config(dip=$dip, mem=$maxMem, sw=$sw, width=$screenWidth, height=$screenHeight)"
    }
}