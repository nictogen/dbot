package nic.tess

import nic.tess.data.Character
import nic.tess.data.Location
import nic.tess.menus.MainMenu
import nic.tess.menus.base.MenuHandler
import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import nic.tess.util.ISaveable

fun main() {
    Tess.init()
}

object Tess {
    private val api: DiscordApi = DiscordApiBuilder().setToken(PrivateTokens.TOKEN).login().join()
    private const val CHARACTER_FOLDER = "tessData/$/characters"
    private const val LOCATION_FOLDER = "tessData/$/locations"

    fun init(){
        api.servers.forEach {
            Location.locations[it.id] = ISaveable.loadData(getLocationFolder(it.id), ArrayList())
            Character.characters[it.id] = ISaveable.loadData(getCharacterFolder(it.id), ArrayList())
        }
        api.addMessageCreateListener(MenuHandler)
        api.addMessageDeleteListener(MenuHandler)
        api.addReactionAddListener(MenuHandler)
    }

    fun getCharacterFolder(server : Long) = CHARACTER_FOLDER.replace("$", server.toString())
    fun getLocationFolder(server : Long) = LOCATION_FOLDER.replace("$", server.toString())
}