package sts.kroos.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sts.kroos.KroosMod;
import sts.kroos.powers.FrostPower;
import sts.kroos.util.TextureLoader;

/**
 * 残弩-典训 — 基础。
 * 战斗开始时获得 2 层寒芒。
 */
public class BrokenCrossbow extends CustomRelic {
    public static final String ID = KroosMod.MOD_ID + ":BrokenCrossbow";
    private static final String IMG = KroosMod.RES_ROOT + "relics/broken_crossbow.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/broken_crossbow_outline.png";

    public static final int BATTLE_START_FROST = 2;

    public BrokenCrossbow() {
        super(ID, (Texture) null, RelicTier.COMMON, LandingSound.FLAT);
        this.img = TextureLoader.getTexture(IMG);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player,
                new FrostPower(AbstractDungeon.player, BATTLE_START_FROST), BATTLE_START_FROST));
    }

    @Override
    public AbstractRelic makeCopy() { return new BrokenCrossbow(); }
}
