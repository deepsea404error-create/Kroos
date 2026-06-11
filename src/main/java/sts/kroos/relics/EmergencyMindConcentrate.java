package sts.kroos.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 应急理智浓缩液 — 稀有。战斗结束时恢复 6 点生命。
 */
public class EmergencyMindConcentrate extends CustomRelic {
    public static final String ID = KroosMod.MOD_ID + ":EmergencyMindConcentrate";
    private static final String IMG = KroosMod.RES_ROOT + "relics/emergency_mind_concentrate.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/emergency_mind_concentrate_outline.png";

    public static final int HEAL = 6;

    public EmergencyMindConcentrate() {
        super(ID, (Texture) null, RelicTier.RARE, LandingSound.FLAT);
        this.img = TextureLoader.getTexture(IMG);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public void onVictory() {
        if (AbstractDungeon.player != null) {
            this.flash();
            AbstractDungeon.player.heal(HEAL);
        }
    }

    @Override
    public AbstractRelic makeCopy() { return new EmergencyMindConcentrate(); }
}
