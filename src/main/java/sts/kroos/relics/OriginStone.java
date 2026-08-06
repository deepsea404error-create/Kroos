package sts.kroos.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sts.kroos.KroosMod;
import sts.kroos.powers.FrostPower;
import sts.kroos.util.TextureLoader;

/**
 * 源石 — BOSS。
 * 每回合失去 1 点生命。每 2 回合交替获得 1 点能量 / 1 点寒芒。
 *
 * 计数模式: 参考 Happy Flower, 使用 this.counter 跨战斗持久计数。
 * counter 到 2 触发后归 0, 奇数(1)→能量, 偶数(2)→寒芒。
 */
public class OriginStone extends CustomRelic {
    public static final String ID = KroosMod.MOD_ID + ":OriginStone";
    private static final String IMG = KroosMod.RES_ROOT + "relics/origin_stone.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/origin_stone_outline.png";

    public static final int HP_COST = 1;
    public static final int TRIGGER_EVERY = 2;

    public OriginStone() {
        super(ID, TextureLoader.getTexture(IMG), RelicTier.BOSS, LandingSound.HEAVY);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public void onEquip() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.player == null) return;

        if (this.counter == -1) {
            this.counter += 2;
        } else {
            this.counter++;
        }

        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(
                AbstractDungeon.player, AbstractDungeon.player,
                HP_COST, AbstractGameAction.AttackEffect.NONE));

        // counter=1 → 能量, counter=2 → 寒芒并归0
        if (this.counter % 2 == 1) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player,
                    new FrostPower(AbstractDungeon.player, 1), 1));
            this.counter = 0;
        }
    }

    @Override
    public AbstractRelic makeCopy() { return new OriginStone(); }
}
