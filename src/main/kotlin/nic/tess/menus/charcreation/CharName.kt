package nic.tess.menus.charcreation

import nic.tess.data.Character
import nic.tess.menus.base.MenuHandler
import org.javacord.api.entity.channel.TextChannel
import org.javacord.api.entity.message.Message
import org.javacord.api.entity.message.Reaction
import org.javacord.api.entity.server.Server
import org.javacord.api.entity.user.User
import java.awt.Color

class CharName(user: User, message: Message, channel: TextChannel) : MenuHandler.Menu(user, message, channel, Color(125, 0, 155), "What should your character's name be?")  {

    override fun onReaction(server: Server, user: User, reaction: Reaction) {}

    override fun onMessage(server: Server, user: User, message: Message) {
        val c = Character.createCharacter(message)
        MenuHandler.Notify(this.user, this.message, this.message!!.channel, "Your character, ${c.rpName}, was created!")
    }
}