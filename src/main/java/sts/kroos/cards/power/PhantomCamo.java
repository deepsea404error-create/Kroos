package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.PhantomCamoPower;

/**
 * 幻影伪装 - 2费(强化 1费), 回合结束时若处于浅眠状态获得 1 层迷彩。
 */
public class PhantomCamo extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":PhantomCamo";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/phantom_camo.png";

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int AMOUNT = 1;

    public PhantomCamo() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = AMOUNT;
        this.magicNumber = AMOUNT;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new PhantomCamoPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new PhantomCamo(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            upgradeDescription();
        }
    }
}
