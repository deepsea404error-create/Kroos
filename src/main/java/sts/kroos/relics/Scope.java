package sts.kroos.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 瞄准镜 — 罕见。
 * 暴击造成的伤害 +50% (即 1.5x → 2.0x)。
 *
 * 本遗物本身不直接修改伤害, 而是被 CriticalPower 主动查询:
 * 若玩家持有本遗物, CriticalPower.atDamageGive 用 2.0F 替代 1.5F。
 */
public class Scope extends CustomRelic {
    public static final String ID = KroosMod.MOD_ID + ":Scope";
    private static final String IMG = KroosMod.RES_ROOT + "relics/scope.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/scope_outline.png";

    public Scope() {
        super(ID, (Texture) null, RelicTier.UNCOMMON, LandingSound.FLAT);
        this.img = TextureLoader.getTexture(IMG);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public AbstractRelic makeCopy() { return new Scope(); }
}
