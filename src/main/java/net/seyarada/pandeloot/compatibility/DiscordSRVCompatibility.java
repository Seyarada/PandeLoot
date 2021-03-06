package net.seyarada.pandeloot.compatibility;

import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.seyarada.pandeloot.utils.ColorUtil;
import org.bukkit.entity.Player;

public class DiscordSRVCompatibility {

    private static final String url = "https://crafatar.com/avatars/";

    public static void embed(Player player, String title, String message,
                                        String channel, String color, String link, boolean avatar) {

        if(title!=null&&message!=null&&channel!=null) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle(title);
            builder.setColor(ColorUtil.getAWTColor(color));
            if(avatar&&player!=null)
                builder.setThumbnail(url+player.getUniqueId());
            if(link!=null) {
                //link = link.replace("<§da>", "-");
                builder.setThumbnail(link);
            }
            builder.setDescription(message);
            MessageEmbed embed = builder.build();
            DiscordUtil.getJda().getTextChannelById(channel).sendMessage(embed).queue();
        }
    }

}
