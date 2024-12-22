package skin.handler

import org.bukkit.event.Listener
import skin.listener.SkinListener
import skin.system.Cardinal

object ListenerHandler {
    private val listenerList = listOf(
        SkinListener()
    )

    fun add(listener: Listener) {
        Cardinal.addListener(listener)
    }

    fun all() {
        listenerList.forEach(::add)
    }
}