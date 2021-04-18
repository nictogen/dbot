package nic.tess.data

import nic.tess.Tess
import nic.tess.util.ISaveable
import org.javacord.api.entity.message.Message
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Location : ISaveable {

    companion object {
        val locations = HashMap<Long, ArrayList<Location>>()

        fun createLocation(message: Message) {
            val server = message.server.get()
            val location = Location()

            val character = Character.getCharacter(message.channel.asServerTextChannel().get())
            if(character != null){
                if (character.getLocation() != null){
                    location.nearby.add(character.location)
                    character.getLocation()!!.nearby.add(location.uuid)
                }
                character.location = location.uuid
            }
            location.serverID = server.id
            location.name = message.content

            locations[server.id]?.add(location)
            location.saveData()
        }
    }

    var name: String = ""
    var unicodeEmoji = ""
    var customEmojiName = ""
    var uuid: String = UUID.randomUUID().toString()
    var serverID: Long = 0
    var nearby: ArrayList<String> = ArrayList()

    override fun getFolderPath() = Tess.getLocationFolder(serverID)
    override fun getFileName() = uuid
}