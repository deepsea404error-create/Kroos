package sts.kroos.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sts.kroos.KroosMod;
import sts.kroos.powers.FrostPower;
import sts.kroos.util.TextureLoader;

/**
 * 干员晋升证章 — BOSS。
 * 替换克洛丝证章 (参考 Eyjafjalla 的 CloudInPacket 替换 CloudInBottle 模式)。
 * 战斗开始时获得 3 层寒芒, 每回合获得 1 层寒芒。
 */
public class OperatorBadge extends CustomRelic {
    public static final String ID = KroosMod.MOD_ID + ":OperatorBadge";
    private static final String IMG = KroosMod.RES_ROOT + "relics/operator_badge.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/operator_badge_outline.png";

    public static final int BATTLE_START_FROST = 3;
    public static final int PER_TURN_FROST = 1;

    public OperatorBadge() {
        super(ID, TextureLoader.getTexture(IMG), RelicTier.BOSS, LandingSound.SOLID);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    /** 只有持有克洛丝证章时才会出现在Boss遗物池中 */
    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(KroosBadge.ID);
    }

    /** 覆写 obtain(): 若持有克洛丝证章, 用 instantObtain 在原位替换; 否则正常获取 */
    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(KroosBadge.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(KroosBadge.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    return;
                }
            }
        }
        super.obtain();
    }

    @Override
    public void atBattleStart() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player,
                new FrostPower(AbstractDungeon.player, BATTLE_START_FROST), BATTLE_START_FROST));
    }

    @Override
    public void atTurnStart() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player,
                new FrostPower(AbstractDungeon.player, PER_TURN_FROST), PER_TURN_FROST));
    }

    @Override
    public AbstractRelic makeCopy() { return new OperatorBadge(); }
}
