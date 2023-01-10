package xyz.holocons.mc.holoa9t.adv;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.HiddenVisibility;
import xyz.holocons.mc.holoa9t.baseadv.CuboidEnterAdv;
import xyz.holocons.mc.holoa9t.icon.ZombieIcon;

public class SpawnYagooFindAdv extends CuboidEnterAdv implements HiddenVisibility {
    public SpawnYagooFindAdv(String namespace, String key, String title, String description,
                             float x, float y, Advancement parent) {
        super(namespace, key,
                new AdvancementDisplay(ZombieIcon.getIcon(), title, AdvancementFrameType.TASK,
                        true, true,
                        x, y, description),
                parent);
    }
}
