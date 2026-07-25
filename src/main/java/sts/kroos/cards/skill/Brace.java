package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 蓄势待发 - 1费(强化: 保留)。
 *   - 让当前手牌与未消耗的能量保留至下回合 (简化为: 所有手牌+selfRetain 一次, +1 能量囤积)
 *   - 寒芒: 消耗 1 层寒芒, 下回合获得 1 (强化 2) 点能量
 *
 * 注: "保留当前手牌"在原版中需要 Runic Pyramid 或类似机制, 实现难度高。
 *      简化方案: 给所有手牌挂一次性 selfRetain (用一次失效). 当前用近似策略:
 *      把所有手牌移动到下回合的手牌区 — StS 无现成 API, 此处采取最小可行版本:
 *      标记手牌的 selfRetain=true (本回合内有效, 简化但近似)。
 *
 *      实际效果上, 玩家本回合手牌会全部 retain 到下回合 + 下回合 +1 能量;
 *      与"保留手牌和能量"语义接近。
 */
public class Brace extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Brace";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/brace.png";

    private static final int COST = 1;
    private static final int FROST_ENERGY = 1;
    private static final int FROST_ENERGY_UPG = 2;

    public Brace() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        // 手牌全部 retain（一次性，下回合清除，参考 Well Laid Plans）
        for (AbstractCard c : p.hand.group) {
            c.retain = true;
        }
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            int e = this.upgraded ? FROST_ENERGY_UPG : FROST_ENERGY;
            addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, e), e));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Brace(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.selfRetain = true;
            upgradeDescription();
        }
    }
}
