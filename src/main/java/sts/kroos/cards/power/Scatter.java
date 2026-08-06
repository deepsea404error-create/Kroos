package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.cards.colorless.NormalArrow;
import sts.kroos.powers.ScatterPower;

/**
 * 散射 - 2费(强化不变), 获得 散射 Power。添加 4 张 普通箭矢 至手牌。
 *   - 强化: 改为添加 4 张升级后的普通箭矢。
 */
public class Scatter extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Scatter";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/scatter.png";

    private static final int COST = 2;
    private static final int ARROWS = 4;

    public Scatter() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ScatterPower(p)));
        NormalArrow arrow = new NormalArrow();
        if (this.upgraded) arrow.upgrade();
        addToBot(new MakeTempCardInHandAction(arrow, ARROWS));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Scatter(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeDescription();
        }
    }
}
