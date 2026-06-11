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
 * 克洛丝证章(初始遗物)
 * 战斗开始时获得2层寒芒。每回合获得1层寒芒。
 */
public class KroosBadge extends CustomRelic {

    public static final String ID = KroosMod.MOD_ID + ":KroosBadge";
    private static final String IMG = KroosMod.RES_ROOT + "relics/kroos_badge.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/kroos_badge_outline.png";

    public static final int BATTLE_START_FROST = 2;
    public static final int PER_TURN_FROST = 1;

    public KroosBadge() {
        super(ID, (Texture) null, RelicTier.STARTER, LandingSound.FLAT);
        this.img = TextureLoader.getTexture(IMG);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        AbstractDungeon.player, AbstractDungeon.player,
                        new FrostPower(AbstractDungeon.player, BATTLE_START_FROST),
                        BATTLE_START_FROST));
    }

    @Override
    public void atTurnStart() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        AbstractDungeon.player, AbstractDungeon.player,
                        new FrostPower(AbstractDungeon.player, PER_TURN_FROST),
                        PER_TURN_FROST));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new KroosBadge();
    }
}

