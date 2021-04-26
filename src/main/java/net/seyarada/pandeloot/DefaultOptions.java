package net.seyarada.pandeloot;

public class DefaultOptions {

    public final static String message = Config.config.getString("DefaultValues.Message");
    public final static String color = Config.config.getString("DefaultValues.Color");
    public final static String title = Config.config.getString("DefaultValues.Title");
    public final static String subtitle = Config.config.getString("DefaultValues.Subtitle");
    public final static String sound = Config.config.getString("DefaultValues.Sound");
    public final static String broadcast = Config.config.getString("DefaultValues.Broadcast");
    public final static String actionbar = Config.config.getString("DefaultValues.Actionbar");
    public final static String parent = Config.config.getString("DefaultValues.Parent");
    public final static String explodeType = Config.config.getString("DefaultValues.ExplodeType");
    public final static String hologram = Config.config.getString("DefaultValues.Hologram");
    public final static String top = Config.config.getString("DefaultValues.Top");
    public final static String permission = Config.config.getString("DefaultValues.Permission");
    public final static String holoBroadcast = Config.config.getString("DefaultValues.HoloBroadcast");
    public final static String type = Config.config.getString("DefaultValues.Type");
    public final static String unidentified = Config.config.getString("DefaultValues.Unidentified");
    public final static boolean glow = Config.config.getBoolean("DefaultValues.Glow");
    public final static boolean explode = Config.config.getBoolean("DefaultValues.Explode");
    public final static boolean stackable = Config.config.getBoolean("DefaultValues.Stackable");
    public final static boolean preventpickup = Config.config.getBoolean("DefaultValues.PreventPickup");
    public final static boolean playonpickup = Config.config.getBoolean("DefaultValues.PlayOnPickup");
    public final static boolean asloottable = Config.config.getBoolean("DefaultValues.AsLoottable");
    public final static boolean lasthit = Config.config.getBoolean("DefaultValues.LastHit");
    public final static int titleDuration = Config.config.getInt("DefaultValues.TitleDuration");
    public final static int titleFade = Config.config.getInt("DefaultValues.TitleFade");
    public final static int delay = Config.config.getInt("DefaultValues.Delay");
    public final static int abandonTime = Config.config.getInt("DefaultValues.AbandonTime");
    public final static int amount = Config.config.getInt("DefaultValues.Amount");
    public final static int mmocoreExp = Config.config.getInt("DefaultValues.MMOCoreExp");
    public final static double expheight = Config.config.getDouble("DefaultValues.ExpHeight");
    public final static double expoffset = Config.config.getDouble("DefaultValues.ExpOffset");
    public final static double beam = Config.config.getDouble("DefaultValues.Beam");
    public final static double explodeRadius = Config.config.getDouble("DefaultValues.ExplodeRadius");
    public final static String damage = Config.config.getString("DefaultValues.Damage");
    public final static String canView = Config.config.getString("DefaultValues.CanView");

    public final static boolean stop = Config.config.getBoolean("DefaultValues.Stop");
    public final static boolean shared = Config.config.getBoolean("DefaultValues.Shared");
    public final static int skip = Config.config.getInt("DefaultValues.Skip");

    public final static String command = Config.config.getString("DefaultValues.Command");
    public final static boolean toInv = Config.config.getBoolean("DefaultValues.ToInv");
    public final static int experience = Config.config.getInt("DefaultValues.Experience");
    public final static double money = Config.config.getDouble("DefaultValues.Money");

    // DiscordSRV
    public final static String dTitle = Config.config.getString("DefaultValues.DTitle");
    public final static String dMessage = Config.config.getString("DefaultValues.DMessage");
    public final static String dChannel = Config.config.getString("DefaultValues.DChannel");
    public final static String dColor = Config.config.getString("DefaultValues.DColor");
    public final static String dLink = Config.config.getString("DefaultValues.DLink");
    public final static boolean dAvatar = Config.config.getBoolean("DefaultValues.DAvatar");

}
