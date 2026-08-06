package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 侦察 - 0费消耗, 抽 2 张牌。
 *   - 寒芒: 消耗 1 层寒芒, 额外抽 1 张牌
 *   - 强化: 抽 3 张牌
 */
public class Scout extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Scout";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/scout.png";

    private static final int COST = 0;
    private static final int DRAW = 2;
    private static final int UPGRADE_DRAW = 1;

    public Scout() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = DRAW;
        this.magicNumber = DRAW;
        this.exhaust = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int draw = this.magicNumber;
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            draw += 1;
        }
        addToBot(new DrawCardAction(p, draw));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Scout(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DRAW);
            upgradeDescription();
        }
    }
}
