package net.seyarada.pandeloot.rewards;

import net.seyarada.pandeloot.DefaultOptions;
import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.damage.DamageUtil;
import net.seyarada.pandeloot.utils.PlaceholderUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RewardOptions {

    public String origin;
    public String item;
    public String line;

    public ItemStack itemStack;

    public String chance = "1";
    public int amount = 1;
    public String damage = DefaultOptions.damage;

    public String message = DefaultOptions.message;
    public String color = DefaultOptions.color;
    public String title = DefaultOptions.title;
    public String subtitle = DefaultOptions.subtitle;
    public String sound = DefaultOptions.sound;
    public String broadcast = DefaultOptions.broadcast;
    public String actionbar = DefaultOptions.actionbar;
    public String parent = DefaultOptions.parent;
    public String explodeType = DefaultOptions.explodeType;
    public String hologram = DefaultOptions.hologram;
    public String top = DefaultOptions.top;
    public String permission = DefaultOptions.permission;
    public boolean glow = DefaultOptions.glow;
    public boolean explode = DefaultOptions.explode;
    public boolean stackable = DefaultOptions.stackable;
    public boolean preventpickup = DefaultOptions.preventpickup;
    public boolean playonpickup = DefaultOptions.playonpickup;
    public boolean asloottable = DefaultOptions.asloottable;
    public boolean lasthit = DefaultOptions.lasthit;
    public int titleDuration = DefaultOptions.titleDuration;
    public int titleFade = DefaultOptions.titleFade;
    public int delay = DefaultOptions.delay;
    public int abandonTime = DefaultOptions.abandonTime;
    public double expheight = DefaultOptions.expheight;
    public double expoffset = DefaultOptions.expoffset;
    public double beam = DefaultOptions.beam;
    public double explodeRadius = DefaultOptions.explodeRadius;
    public int mmocoreExp = DefaultOptions.mmocoreExp;

    public boolean stop = DefaultOptions.stop;
    public boolean shared = DefaultOptions.shared;
    public int skip = DefaultOptions.skip;

    public String command = DefaultOptions.command;
    public boolean toInv = DefaultOptions.toInv;
    public int experience = DefaultOptions.experience;
    public double money = DefaultOptions.money;

    // DiscordSRV
    public String dTitle = DefaultOptions.dTitle;
    public String dMessage = DefaultOptions.dMessage;
    public String dChannel = DefaultOptions.dChannel;
    public String dColor = DefaultOptions.dColor;
    public String dLink = DefaultOptions.dLink;
    public boolean dAvatar = DefaultOptions.dAvatar;

    // HoloBroadcast
    public String holoBroadcast = DefaultOptions.holoBroadcast;

    // MMOItems
    public String type = DefaultOptions.type;
    public String unidentified = DefaultOptions.unidentified;

    // Internal Options
    public boolean skipConditions;
    public Map<Double, Integer> radialDrop;
    public int radialOrder;

    public void optionsSwitch(String option, String i) {
        switch(option.toLowerCase()) {
            case "message":
            case "msg":
                if(DefaultOptions.message!=null && !message.equals(DefaultOptions.message)) return;
                message = i; break;
            case "color":
            case "colour":
                if(DefaultOptions.color!=null && !color.equals(DefaultOptions.color)) return;
                color = i; break;
            case "title":
                if(DefaultOptions.title!=null && !title.equals(DefaultOptions.title)) return;
                title = i; break;
            case "subtitle":
            case "st":
                if(DefaultOptions.subtitle!=null && !subtitle.equals(DefaultOptions.subtitle)) return;
                subtitle = i; break;
            case "sound":
                if(DefaultOptions.sound!=null && !sound.equals(DefaultOptions.sound)) return;
                sound = i; break;
            case "broadcast":
                if(DefaultOptions.broadcast!=null && !broadcast.equals(DefaultOptions.broadcast)) return;
                broadcast = i; break;
            case "actionbar":
                if(DefaultOptions.actionbar!=null && !actionbar.equals(DefaultOptions.actionbar)) return;
                actionbar = i; break;
            case "parent":
                if(DefaultOptions.parent!=null && !parent.equals(DefaultOptions.parent)) return;
                parent = i; break;
            case "explodetype":
                if(DefaultOptions.explodeType!=null && !explodeType.equals(DefaultOptions.explodeType)) return;
                explodeType = i; break;
            case "hologram":
                if(DefaultOptions.hologram!=null && !hologram.equals(DefaultOptions.hologram)) return;
                hologram = i; break;
            case "command":
            case "cmd":
                if(DefaultOptions.command!=null && !command.equals(DefaultOptions.command)) return;
                command = i; break;
            case "holobroadcast":
            case "hb":
                if(DefaultOptions.holoBroadcast!=null && !holoBroadcast.equals(DefaultOptions.holoBroadcast)) return;
                holoBroadcast = i; break;
            case "top":
                if(DefaultOptions.top!=null && !top.equals(DefaultOptions.top)) return;
                top = i; break;
            case "permission":
                if(DefaultOptions.permission!=null && !permission.equals(DefaultOptions.permission)) return;
                permission = i; break;
            case "type":
                if(DefaultOptions.type!=null && !type.equals(DefaultOptions.type)) return;
                type = i; break;
            case "unidentified":
                if(DefaultOptions.unidentified!=null && !unidentified.equals(DefaultOptions.unidentified)) return;
                unidentified = i; break;
            case "damage":
                if(DefaultOptions.damage!=null && !damage.equals(DefaultOptions.damage)) return;
                damage = i; break;

            case "stackable":
            case "stack":
                if(!DefaultOptions.stackable && stackable) return;
                stackable = Boolean.parseBoolean(i); break;
            case "glow":
                if(!DefaultOptions.glow && glow) return;
                glow = Boolean.parseBoolean(i); break;
            case "explode":
                if(!DefaultOptions.explode && explode) return;
                explode = Boolean.parseBoolean(i); break;
            case "preventpickup":
            case "pickup":
            case "pickable":
                if(!DefaultOptions.preventpickup && preventpickup) return;
                preventpickup = Boolean.parseBoolean(i); break;
            case "playonpickup":
            case "onpickup":
                if(!DefaultOptions.playonpickup && playonpickup) return;
                playonpickup = Boolean.parseBoolean(i); break;
            case "asloottable":
            case "aslt":
                if(!DefaultOptions.asloottable && asloottable) return;
                asloottable = Boolean.parseBoolean(i); break;
            case "lasthit":
                if(!DefaultOptions.lasthit && lasthit) return;
                lasthit = Boolean.parseBoolean(i); break;
            case "stop":
                if(!DefaultOptions.stop && stop) return;
                stop = Boolean.parseBoolean(i); break;
            case "shared":
                if(!DefaultOptions.shared && shared) return;
                shared = Boolean.parseBoolean(i); break;
            case "toinv":
                if(!DefaultOptions.toInv && toInv) return;
                toInv = Boolean.parseBoolean(i); break;

            case "titleduration":
            case "td":
                if(DefaultOptions.titleDuration>0 && titleDuration != DefaultOptions.titleDuration) return;
                titleDuration = (int) Double.parseDouble(i); break;
            case "titlefade":
            case "tf":
                if(DefaultOptions.titleFade>0 && titleFade != DefaultOptions.titleFade) return;
                titleFade = (int) Double.parseDouble(i); break;
            case "delay":
                if(DefaultOptions.delay>0 && delay != DefaultOptions.delay) return;
                delay = (int) Double.parseDouble(i); break;
            case "experience":
            case "exp":
                if(DefaultOptions.experience>0 && experience != DefaultOptions.experience) return;
                experience = (int) Double.parseDouble(i); break;
            case "skip":
                if(DefaultOptions.skip>0 && skip != DefaultOptions.skip) return;
                skip = (int) Double.parseDouble(i); break;
            case "money":
                if(DefaultOptions.money>0 && money != DefaultOptions.money) return;
                money = (int) Double.parseDouble(i); break;
            case "amount":
                if(DefaultOptions.amount>0 && amount != DefaultOptions.amount) return;
                amount = (int) Double.parseDouble(i); break;
            case "abandontime":
                if(DefaultOptions.abandonTime>0 && abandonTime != DefaultOptions.abandonTime) return;
                abandonTime = (int) Double.parseDouble(i); break;
            case "mmocoreexp":
                if(DefaultOptions.mmocoreExp>0 && mmocoreExp != DefaultOptions.mmocoreExp) return;
                mmocoreExp = (int) Double.parseDouble(i); break;

            case "expheight":
            case "exph":
                if(DefaultOptions.expheight>0 && expheight != DefaultOptions.expheight) return;
                expheight = Double.parseDouble(i); break;
            case "expoffset":
            case "expo":
                if(DefaultOptions.expoffset>0 && expoffset != DefaultOptions.expoffset) return;
                expoffset = Double.parseDouble(i); break;
            case "beam":
                if(DefaultOptions.beam>0 && beam != DefaultOptions.beam) return;
                beam = Double.parseDouble(i); break;
            case "exploderadius":
            case "expr":
                if(DefaultOptions.explodeRadius>0 && explodeRadius != DefaultOptions.explodeRadius) return;
                explodeRadius = Double.parseDouble(i); break;

            // DiscordSRV
            case "dtitle":
            case "discordtitle":
                if(DefaultOptions.dTitle!=null && !dTitle.equals(DefaultOptions.dTitle)) return;
                dTitle = i; break;
            case "dmessage":
            case "discordmessage":
                if(DefaultOptions.dMessage!=null && !dMessage.equals(DefaultOptions.dMessage)) return;
                dMessage = i; break;
            case "dchannel":
            case "discordchannel":
                if(DefaultOptions.dChannel!=null && !dChannel.equals(DefaultOptions.dChannel)) return;
                dChannel = i; break;
            case "dcolor":
            case "discordcolor":
                if(DefaultOptions.dColor!=null && !dColor.equals(DefaultOptions.dColor)) return;
                dColor = i; break;
            case "dlink":
            case "discordlink":
                if(DefaultOptions.dLink!=null && !dLink.equals(DefaultOptions.dLink)) return;
                dLink = i; break;
            case "dvatar":
            case "discordavatar":
                if(!DefaultOptions.dAvatar && dAvatar) return;
                dAvatar = Boolean.parseBoolean(i); break;

            case "rewards":
            case "guaranteed":
            case "minitems":
            case "maxitems":
            case "totalitems":
            case "model":
            case "material":
            case "options":
            case "display":
                break;

            default:
                StringLib.badOption(option);
        }
    }

    public double getChance(DamageUtil u, Player p) {
        chance = PlaceholderUtil.parse(chance, u, p);
        return PlaceholderUtil.parseMath(chance);
    }

    public static Map<String, String> getOptions(String i) {
        HashMap<String, String> options = new HashMap<>();
        int lBracket = i.indexOf("{");
        int rBracket = i.indexOf("}");
        if(lBracket==rBracket) return null;

        for(String option : i.substring(lBracket+1, rBracket).split(";")) {
            String[] pair = option.split("=");
            options.put(pair[0], pair[1]);
        }
        return options;
    }

}
