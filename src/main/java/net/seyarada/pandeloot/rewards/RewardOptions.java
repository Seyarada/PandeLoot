package net.seyarada.pandeloot.rewards;

public class RewardOptions {

    public String message;
    public String color;
    public String title;
    public String subtitle;
    public String sound;
    public String broadcast;
    public String actionbar;
    public String parent;
    public boolean glow;
    public boolean explode;
    public boolean stackable;
    public boolean preventpickup;
    public boolean playonpickup;
    public boolean dropcontents;
    public int titleDuration;
    public int titleFade;
    public int delay;
    public double expheight;
    public double expoffset;
    public double beam;

    public String damage;
    public int top;
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
        this.message = reward.getOption(null, "message","msg");
        this.message = reward.getOption(null, "message","msg");
        this.color = reward.getOption("display", "color");
        this.title = reward.getOption("", "title");
        this.subtitle = reward.getOption("", "subtitle");
        this.sound = reward.getOption(null, "sound");
        this.broadcast = reward.getOption(null, "broadcast");
        this.actionbar = reward.getOption(null, "actionbar");
        this.parent = reward.getOption(null, "parent");

        this.stackable = Boolean.parseBoolean(reward.getOption("true", "stackable","stacks"));
        this.glow = Boolean.parseBoolean(reward.getOption("true", "glow"));
        this.explode = Boolean.parseBoolean(reward.getOption("true", "explode"));
        this.preventpickup = Boolean.parseBoolean(reward.getOption("false", "preventpickup","pickup"));
        this.playonpickup = Boolean.parseBoolean(reward.getOption("false", "playonpickup","onpickup"));
        this.dropcontents = Boolean.parseBoolean(reward.getOption("false", "dropcontents"));

        this.titleDuration = Integer.parseInt(reward.getOption("20", "titleduration","td"));
        this.titleFade = Integer.parseInt(reward.getOption("20", "titlefade","tf"));
        this.delay = Integer.parseInt(reward.getOption("0", "delay"));

        this.expheight = Double.parseDouble(reward.getOption("0.6", "expheight"));
        this.expoffset = Double.parseDouble(reward.getOption("0.2", "expoffset"));
        this.beam = Double.parseDouble(reward.getOption("0", "beam"));

        // DiscordSRV things
        this.dTitle = reward.getOption(null, "dtitle");
        this.dMessage = reward.getOption(null, "dmessage");
        this.dChannel = reward.getOption(null, "dchannel");
        this.dColor = reward.getOption("GREEN", "dcolor");
        this.dLink = reward.getOption(null, "dlink");
        this.dAvatar = Boolean.parseBoolean(reward.getOption("true", "davatar"));

        // These aren't "effects" but I don't have a better
        // place where to place them at the moment
        this.command = reward.getOption(null, "command");
        this.experience = Integer.parseInt(reward.getOption("0", "experience", "xp"));
        this.skip = Integer.parseInt(reward.getOption("0", "skip"));
        this.money = Double.parseDouble(reward.getOption("0", "money"));
        this.stop = Boolean.parseBoolean(reward.getOption("false", "stop"));

    }

    public void parseConditions() {
        this.damage = reward.getOption(null, "damage");
        this.top = Integer.parseInt(reward.getOption("0", "top"));
        this.shared = Boolean.parseBoolean(reward.getOption("false", "shared"));
        this.multiplier = Double.parseDouble(reward.getOption("0", "multiplier"));
    }

    public void parseItemData() {
        this.amount = Integer.parseInt(reward.getOption("1", "amount"));
        this.toInv = Boolean.parseBoolean(reward.getOption("false", "toInv"));
    }

    public String getParent() {
        return this.parent = reward.getOption(null, "parent");
    }

}
