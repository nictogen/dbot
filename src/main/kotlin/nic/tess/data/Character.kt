package nic.tess.data

import nic.tess.Tess
import org.javacord.api.entity.channel.ServerTextChannelBuilder
import org.javacord.api.entity.message.Message
import nic.tess.util.ISaveable
import org.javacord.api.entity.channel.ServerTextChannel
import org.javacord.api.entity.channel.TextChannel

class Character : ISaveable {

    companion object {
        val characters = HashMap<Long, ArrayList<Character>>()

        fun createCharacter(message: Message) : Character{
            val server = message.server.get()
            val character = Character()
            character.serverID = server.id
            character.channelID = ServerTextChannelBuilder(server).setCategory(server.channelCategories.first { it.name.toLowerCase().contains("rp") }).setName("rp-${message.content.toLowerCase().replace(" ", "-")}").create().get().id
            character.rpName = message.content

            characters[message.server.get().id]?.add(character)
            character.saveData()
            return character
        }

        fun getCharacter(channel : ServerTextChannel) : Character? = characters[channel.server.id]?.firstOrNull { it.channelID == channel.id }

    }

    var channelID : Long = 0
    var serverID : Long = 0
    var location : String = ""
    var rpName = ""

    fun getLocation() : Location? = Location.locations[serverID]?.firstOrNull { it.uuid == location }

    override fun getFolderPath() = Tess.getCharacterFolder(serverID)
    override fun getFileName() = channelID.toString()


}