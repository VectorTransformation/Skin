package skin.system

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListSet

enum class ReloadType {
    SKIN
}

object ReloadSystem {
    private val state = ConcurrentSkipListSet<ReloadType>()

    fun operation(reloadType: ReloadType, failed: () -> Unit = {}, successful: () -> Unit) {
        if (!ready(reloadType)) {
            failed()
            return
        }
        start(reloadType)
        successful()
        end(reloadType)
    }

    fun ready(reloadType: ReloadType): Boolean {
        return !state.contains(reloadType)
    }

    fun start(reloadType: ReloadType) {
        state.add(reloadType)
    }

    fun end(reloadType: ReloadType) {
        state.remove(reloadType)
    }
}