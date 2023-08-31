package net.frozenblock.configurableeverything.config.gui.api

// just a holder for color
// to allow for distinguishing between a color value and just an integer
data class Color(val color: Int) {

    override fun toString(): String = "Color[$color]"
}
