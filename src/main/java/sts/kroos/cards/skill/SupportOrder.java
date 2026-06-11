package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.actions.SelectFromDrawPileToHandAction;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 支援号令 - 1 (强化 0) 费, 固有, 消耗。
 *   - 获得 1 点能量
 *   - 从抽牌堆中选 1 张牌加入手牌
 *   - 寒芒: 消耗 1 层寒芒, 额外获得 1 点能量
 */
public class SupportOrder extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":SupportOrder";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/support_order.png";

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public SupportOrder() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
        this.isInnate = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(1));
        addToBot(new SelectFromDrawPileToHandAction("选择 1 张牌加入手牌"));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new SupportOrder(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            upgradeDescription();
        }
    }
}
