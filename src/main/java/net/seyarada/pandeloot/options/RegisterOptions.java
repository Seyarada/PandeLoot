package net.seyarada.pandeloot.options;

import net.seyarada.pandeloot.options.conditions.*;
import net.seyarada.pandeloot.options.mechanics.*;

public class RegisterOptions {

    public RegisterOptions() {
        registerConditions();
        registerMechanics();
    }

    private void registerConditions() {
        Conditions.registerOption(new ChanceCondition());
        Conditions.registerOption(new DamageCondition());
        Conditions.registerOption(new LastHitCondition());
        Conditions.registerOption(new PermissionBlacklistCondition(), "permissionblacklist", "pbl");
        Conditions.registerOption(new PermissionCondition(), "permission");
        Conditions.registerOption(new TopCondition(), "top");
    }

    private void registerMechanics() {
        Options.registerOption(new ActionbarMechanic(), OptionType.PLAYER, "actionbar");
        Options.registerOption(new BroadcastMechanic(), OptionType.GENERAL, "broadcast");
        Options.registerOption(new CommandMechanic(), OptionType.GENERAL, "command", "cmd");
        Options.registerOption(new DiscordMechanic(), OptionType.GENERAL, "dchannel");
        Options.registerOption(new ExperienceMechanic(), OptionType.PLAYER, "experience", "exp", "xp");
        Options.registerOption(new ExplodeMechanic(), OptionType.ITEM, "explode");
        Options.registerOption(new GlowMechanic(), OptionType.ITEM, "glow");
        Options.registerOption(new HolobroadcastMechanic(), OptionType.PLAYER, "holobroadcast", "hb");
        Options.registerOption(new HologramMechanic(), OptionType.ITEM, "hologram");
        Options.registerOption(new MessageMechanic(), OptionType.PLAYER, "message", "msg");
        Options.registerOption(new MMOCoreExperienceMechanic(), OptionType.PLAYER, "mmocexp", "coreexp");
        Options.registerOption(new MoneyMechanic(), OptionType.PLAYER, "money", "eco");
        Options.registerOption(new RemoveMechanic(), OptionType.GENERAL, "remove");
        Options.registerOption(new SoundMechanic(), OptionType.PLAYER, "sound");
        Options.registerOption(new TitleMechanic(), OptionType.PLAYER, "title", "subtitle");
        Options.registerOption(new ToastMechanic(), OptionType.PLAYER, "toast", "toasttitle");
        Options.registerOption(new TotemMechanic(), OptionType.PLAYER, "totemeffect", "playtotem", "totem");
        Options.registerOption(new VisibilityMechanic(), OptionType.ITEM, "visibility", "canview");
    }

}
