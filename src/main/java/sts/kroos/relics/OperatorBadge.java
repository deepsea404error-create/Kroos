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
 * 干员晋升证章 — BOSS。
 * 替换克洛丝证章。
 * 战斗开始时获得 3 层寒芒, 每回合获得 1 层寒芒。
 *
 * onEquip 时若已持有 KroosBadge, 自动移除原始基础遗物。
 */
public class OperatorBadge extends CustomRelic {
    public static final String ID = KroosMod.MOD_ID + ":OperatorBadge";
    private static final String IMG = KroosMod.RES_ROOT + "relics/operator_badge.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/operator_badge_outline.png";

    public static final int BATTLE_START_FROST = 3;
    public static final int PER_TURN_FROST = 1;

    public OperatorBadge() {
        super(ID, (Texture) null, RelicTier.BOSS, LandingSound.SOLID);
        this.img = TextureLoader.getTexture(IMG);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public void onEquip() {
        if (AbstractDungeon.player == null) return;
        if (AbstractDungeon.player.hasRelic(KroosBadge.ID)) {
            AbstractDungeon.player.loseRelic(KroosBadge.ID);
        }
    }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player,
                new FrostPower(AbstractDungeon.player, BATTLE_START_FROST), BATTLE_START_FROST));
    }

    @Override
    public void atTurnStart() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player,
                new FrostPower(AbstractDungeon.player, PER_TURN_FROST), PER_TURN_FROST));
    }

    @Override
    public AbstractRelic makeCopy() { return new OperatorBadge(); }
}
