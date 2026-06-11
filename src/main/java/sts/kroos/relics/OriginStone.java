package sts.kroos.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sts.kroos.KroosMod;
import sts.kroos.powers.FrostPower;
import sts.kroos.util.TextureLoader;

/**
 * 源石 — BOSS。
 * 每回合失去 2 点生命。单数回合获得 1 点能量, 双数回合获得 1 点寒芒。
 *
 * 回合计数: 战斗内 turnCount (1, 2, 3 ...) — 战斗开始重置。
 */
public class OriginStone extends CustomRelic {
    public static final String ID = KroosMod.MOD_ID + ":OriginStone";
    private static final String IMG = KroosMod.RES_ROOT + "relics/origin_stone.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/origin_stone_outline.png";

    public static final int HP_COST = 2;
    private int turnCount = 0;

    public OriginStone() {
        super(ID, (Texture) null, RelicTier.BOSS, LandingSound.HEAVY);
        this.img = TextureLoader.getTexture(IMG);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public void atBattleStart() { turnCount = 0; }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.player == null) return;
        turnCount++;
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(
                AbstractDungeon.player, AbstractDungeon.player,
                HP_COST, AbstractGameAction.AttackEffect.NONE));
        if (turnCount % 2 == 1) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player,
                    new FrostPower(AbstractDungeon.player, 1), 1));
        }
    }

    @Override
    public AbstractRelic makeCopy() { return new OriginStone(); }
}
