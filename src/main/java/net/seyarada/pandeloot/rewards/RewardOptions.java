package net.seyarada.pandeloot.rewards;

import net.seyarada.pandeloot.Config;

public class RewardOptions {

    public String message;
    public String color;
    public String title;
    public String subtitle;
    public String sound;
    public String broadcast;
    public String actionbar;
    public String parent;
    public String explodeType;
    public String hologram;
    public boolean glow;
    public boolean explode;
    public boolean stackable;
    public boolean preventpickup;
    public boolean playonpickup;
    public int titleDuration;
    public int titleFade;
    public int delay;
    public double expheight;
    public double expoffset;
    public double beam;
    public double explodeRadius;

    public String damage;
    public boolean stop;
    public boolean shared;
    public int skip;
    public double multiplier;

    public String command;
    public boolean toInv;
    public int amount;
    public int experience;
    public double money;


    public String dTitle;
    public String dMessage;
    public String dChannel;
    public String dColor;
    public String dLink;
    public boolean dAvatar;

    //public String holobroadcast;
    //private DropTable dropTable;

    public final RewardLine reward;

    public RewardOptions(RewardLine line) {
        reward = line;
    }

    public void parseEffects() {
        this.message = reward.getOption(Config.getDefault("Message"), "message","msg");
        this.color = reward.getOption(Config.getDefault("Color"), "color");
        this.title = reward.getOption(Config.getDefault("Title"), "title");
        this.subtitle = reward.getOption(Config.getDefault("Subtitle"), "subtitle");
        this.sound = reward.getOption(Config.getDefault("Sound"), "sound");
        this.broadcast = reward.getOption(Config.getDefault("Broadcast"), "broadcast");
        this.actionbar = reward.getOption(Config.getDefault("Actionbar"), "actionbar");
        this.parent = reward.getOption(Config.getDefault("Parent"), "parent");
        this.explodeType = reward.getOption(Config.getDefault("ExplodeType"), "explodeType");
        this.hologram = reward.getOption(Config.getDefault("Hologram"), "hologram");

        this.stackable = Boolean.parseBoolean(reward.getOption(Config.getDefault("Stackable"), "stackable","stacks"));
        this.glow = Boolean.parseBoolean(reward.getOption(Config.getDefault("Glow"), "glow"));
        this.explode = Boolean.parseBoolean(reward.getOption(Config.getDefault("Explode"), "explode"));
        this.preventpickup = Boolean.parseBoolean(reward.getOption(Config.getDefault("PreventPickup"), "preventpickup","pickup"));
        this.playonpickup = Boolean.parseBoolean(reward.getOption(Config.getDefault("PlayOnPickup"), "playonpickup","onpickup"));

        this.titleDuration = Integer.parseInt(reward.getOption(Config.getDefault("TitleDuration"), "titleduration","td"));
        this.titleFade = Integer.parseInt(reward.getOption(Config.getDefault("TitleFade"), "titlefade","tf"));
        this.delay = Integer.parseInt(reward.getOption(Config.getDefault("Delay"), "delay"));

        this.expheight = Double.parseDouble(reward.getOption(Config.getDefault("ExpHeight"), "expheight"));
        this.expoffset = Double.parseDouble(reward.getOption(Config.getDefault("ExpOffset"), "expoffset"));
        this.beam = Double.parseDouble(reward.getOption(Config.getDefault("Beam"), "beam"));
        this.explodeRadius = Double.parseDouble(reward.getOption(Config.getDefault("ExplodeRadius"), "explodeRadius"));

        // DiscordSRV things
        this.dTitle = reward.getOption(Config.getDefault("DTitle"), "dtitle");
        this.dMessage = reward.getOption(Config.getDefault("DMessage"), "dmessage");
        this.dChannel = reward.getOption(Config.getDefault("DChannel"), "dchannel");
        this.dColor = reward.getOption("GREEN", "dcolor");
        this.dLink = reward.getOption(Config.getDefault("DLink"), "dlink");
        this.dAvatar = Boolean.parseBoolean(reward.getOption(Config.getDefault("DAvatar"), "davatar"));

        // These aren't "effects" but I don't have a better
        // place where to place them at the moment
        this.command = reward.getOption(Config.getDefault("Command"), "command");
        this.experience = Integer.parseInt(reward.getOption(Config.getDefault("Experience"), "experience", "xp"));
        this.skip = Integer.parseInt(reward.getOption(Config.getDefault("Skip"), "skip"));
        this.money = Double.parseDouble(reward.getOption(Config.getDefault("Money"), "money"));
        this.stop = Boolean.parseBoolean(reward.getOption(Config.getDefault("Stop"), "stop"));

    }

    public void parseConditions() {
        this.shared = Boolean.parseBoolean(reward.getOption(Config.getDefault("Shared"), "shared"));
        this.multiplier = Double.parseDouble(reward.getOption("0", "multiplier"));
    }

    public void parseItemData() {
        this.amount = Integer.parseInt(reward.getOption("1", "amount"));
        this.toInv = Boolean.parseBoolean(reward.getOption(Config.getDefault("ToInventory"), "toInventory"));
    }

    public String getParent() {
        return this.parent = reward.getOption(null, "parent");
    }

}
