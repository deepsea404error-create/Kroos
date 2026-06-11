package sts.kroos.cards.colorless;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * A1 小队 - 芬。蓝卡, 0费, 消耗, 虚无, 技能。
 *   - 抽 2 (强化 3) 张牌
 *   - 寒芒: 消耗 1 层寒芒, 额外抽 1 张牌
 */
public class A1_Fen extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":A1_Fen";
    private static final String IMG = KroosMod.RES_ROOT + "cards/colorless/a1_fen.png";

    private static final int COST = 0;
    private static final int DRAW = 2;
    private static final int UPGRADE_DRAW = 1;

    public A1_Fen() {
        super(ID, IMG, COST, CardType.SKILL,
                AbstractCard.CardColor.COLORLESS,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = DRAW;
        this.magicNumber = DRAW;
        this.exhaust = true;
        this.isA1Squad = true;
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
    public AbstractKroosCard makeCopy() { return new A1_Fen(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DRAW);
            upgradeDescription();
        }
    }
}
