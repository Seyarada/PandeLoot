package net.seyarada.pandeloot.utils;

import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.seyarada.pandeloot.Boosts;
import net.seyarada.pandeloot.damage.DamageUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Random;
import java.util.UUID;

public class DropUtil {


    public static Vector getVelocity(double expoffset, double expheight) {
        Random random = new Random();
        return new Vector(
                Math.cos(random.nextDouble() * Math.PI * 2.0D) * expoffset, expheight,
                Math.sin(random.nextDouble() * Math.PI * 2.0D) * expoffset);
    }

    public static double parseMath(String string, Player player, DamageUtil damageUtil) {
        string = parsePlaceholders(string, player, damageUtil);

        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        ScriptEngine engine = factory.getScriptEngine();
        try {
            return Double.parseDouble(String.valueOf(engine.eval(string)));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static String parsePlaceholders(String string, Player player, DamageUtil damageUtil) {
        String i = string;

        i = i.replace("%nl%", "\n");
        i = i.replace("%co%", ":");
        i = i.replace("%co%", ":");
        i = i.replace("<pc>", "%");
        i = i.replace("<§csp>", " ");

        if(player!=null && damageUtil!= null) {

            if(damageUtil.getRankedPlayers().size()>=1) {
                i = i.replace("%1.name%", damageUtil.getPlayer(0).getName());
                i = i.replace("%1.dmg%", String.valueOf(damageUtil.getDamage(0)));
            }

            if(damageUtil.getRankedPlayers().size()>=2) {
                i = i.replace("%2.name%", damageUtil.getPlayer(1).getName());
                i = i.replace("%2.dmg%", String.valueOf(damageUtil.getDamage(1)));
            } else if(i.contains("%2.name%") || i.contains("%2.dmg%")) {
                return null;
            }

            if(damageUtil.getRankedPlayers().size()>=3) {
                i = i.replace("%3.name%", damageUtil.getPlayer(2).getName());
                i = i.replace("%3.dmg%", String.valueOf(damageUtil.getDamage(2)));
            } else if(i.contains("%3.name%") || i.contains("%3.dmg%")) {
                return null;
            }

            if(i.contains("%player%"))
                i = i.replace("%player%", player.getName());

            if(i.contains("%player.dmg%"))
                i = i.replace("%player.dmg%", String.valueOf(damageUtil.getPlayerDamage(player)));

            if(i.contains("%player.pdmg%"))
                i = i.replace("%player.pdmg%", String.valueOf(damageUtil.getPercentageDamage(player)));

            if(i.contains("%player.pdmg100%"))
                i = i.replace("%player.pdmg100%", String.valueOf(damageUtil.getPercentageDamage(player)*100));

            if(i.contains("%player.rank%"))
                i = i.replace("%player.rank%", String.valueOf(damageUtil.getPlayerRank(player)));

            if(i.contains("%mob.name%"))
                i = i.replace("%mob.name%", damageUtil.getEntityName());

            if(i.contains("%mob.hp%"))
                i = i.replace("%mob.hp%", String.valueOf(damageUtil.getEntityHealth()));

            i = i.replace("%boost%", String.valueOf(Boosts.getPlayerBoost(player.getName())));


        }

        if(i.contains("%boost%"))
            i = i.replace("%boost%", String.valueOf(Boosts.getGlobalBoost()));
        i = ChatColor.translateAlternateColorCodes('&', i);

        if(i.contains("%math%")) {
            i = i.replace("%math%", "");
            i = String.valueOf(parseMath(i, player, damageUtil));
        }

        return i;
    }

    public static void sendDiscordEmbed(Player player, DamageUtil damageUtil, String title, String message,
                                        String channel, String color, String link, boolean avatar) {

        if(title!=null&&message!=null&&channel!=null) {

            if(damageUtil!=null) {
                title = parsePlaceholders(title, player, damageUtil);
                message = parsePlaceholders(message, player, damageUtil);
            } else {
                title = parsePlaceholders(title, player, null);
                message = parsePlaceholders(message, player, null);
            }

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle(title);
            builder.setColor(ColorUtil.getAWTColor(color));
            if(avatar&&player!=null)
                builder.setThumbnail("https://crafatar.com/avatars/"+player.getUniqueId());
            if(link!=null) {
                link = link.replace("<§da>", "-");
                builder.setThumbnail(link);
            }
            builder.setDescription(message);
            MessageEmbed embed = builder.build();
            DiscordUtil.getJda().getTextChannelById(channel).sendMessage(embed).queue();
        }
    }

}
