package nic.tess.menus

import nic.tess.menus.base.MenuHandler
import nic.tess.menus.charcreation.CharName
import org.javacord.api.entity.channel.TextChannel
import org.javacord.api.entity.message.Message
import org.javacord.api.entity.message.Reaction
import org.javacord.api.entity.server.Server
import org.javacord.api.entity.user.User
import java.awt.Color

class MainMenu(user: User, channel: TextChannel) : MenuHandler.Menu(user, null, channel, Color(125, 0, 155), "What would you like to do?", MenuHandler.Field("\uD83E\uDDCD", "Create a Character", true), MenuHandler.Field("⏩", "Exit", true)) {

    override fun onReaction(server: Server, user: User, reaction: Reaction) {
        if (reaction.emoji.isUnicodeEmoji) {
            if(reaction.emoji.asUnicodeEmoji().get() == "⏩") {
                reaction.message.delete()
            }
            if(reaction.emoji.asUnicodeEmoji().get() == "\uD83E\uDDCD") {
                CharName(this.user, this.message!!, this.message!!.channel)
            }
        }
    }

    override fun onMessage(server: Server, user: User, message: Message) {}


}