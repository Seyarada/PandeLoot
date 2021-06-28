package net.seyarada.pandeloot.options.mechanics;

import net.seyarada.pandeloot.StringLib;
import net.seyarada.pandeloot.options.MechanicEvent;
import net.seyarada.pandeloot.rewards.Reward;

import java.util.Map;

public class TitleMechanic implements MechanicEvent {
    @Override
    public void onCall(Reward reward, String value) {
        final Map<String, String> options = reward.options;
        final String title = options.get("title");
        final String subtitle = options.get("subtitle");
        final int titleFade = Integer.parseInt(options.get("titlefade"));
        int titleDuration = Integer.parseInt(options.get("titleduration"));
        if(titleDuration==0) titleDuration=20;

        if( ( title!=null && !title.isEmpty() ) || ( subtitle!=null && !subtitle.isEmpty() ) ) {
            StringLib.warn("++++++ Applying title effect with value "+value);
            reward.player.sendTitle(title, subtitle, titleFade, titleDuration, titleFade);
        }
    }
}
