package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.ExtraDrawPower;

/**
 * 急速转移 - 2费, 消耗(强化: 保留+消耗)。
 *   - 选择消耗 3 (强化 2) 张手牌
 *   - 额外获得 1 回合 (参考腾跃 Vault: SkipEnemiesTurn + PressEndTurn)
 *   - 寒芒: 消耗 3 (强化 2) 层寒芒, 下回合额外抽 2 张牌, 获得 1 点能量
 */
public class RapidShift extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":RapidShift";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/rapid_shift.png";

    private static final int COST = 2;
    private static final int EXHAUST_COUNT = 3;
    private static final int UPGRADE_EXHAUST_DELTA = -1;
    private static final int FROST_COST = 3;
    private static final int FROST_COST_UPG = 2;
    private static final int NEXT_TURN_DRAW = 2;
    private static final int NEXT_TURN_ENERGY = 1;

    public RapidShift() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        this.baseMagicNumber = EXHAUST_COUNT;
        this.magicNumber = EXHAUST_COUNT;
        this.exhaust = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        // 1. 选择消耗手牌（玩家自选，非随机）
        addToBot(new ExhaustAction(p, p, this.magicNumber, false, false, false));

        // 2. 额外获得1回合（参考腾跃 Vault）
        addToBot(new SkipEnemiesTurnAction());
        addToBot(new PressEndTurnButtonAction());

        // 3. 寒芒：消耗寒芒，下回合额外抽2张 + 1点能量
        int frostCost = this.upgraded ? FROST_COST_UPG : FROST_COST;
        if (canConsumeFrost(frostCost)) {
            consumeFrost(frostCost);
            addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, NEXT_TURN_ENERGY), NEXT_TURN_ENERGY));
            addToBot(new ApplyPowerAction(p, p, new ExtraDrawPower(p, NEXT_TURN_DRAW), NEXT_TURN_DRAW));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new RapidShift(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_EXHAUST_DELTA);
            this.selfRetain = true;
            // 升级后仍保留消耗词条
            upgradeDescription();
        }
    }
}
