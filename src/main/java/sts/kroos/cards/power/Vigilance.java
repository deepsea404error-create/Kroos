package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.VigilancePower;

/**
 * 警惕 - 1费, 每次退出浅眠状态获得 5 (强化 8) 点活力。
 */
public class Vigilance extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Vigilance";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/vigilance.png";

    private static final int COST = 1;
    private static final int VIT = 5;
    private static final int VIT_UPG = 8;

    public Vigilance() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = VIT;
        this.magicNumber = VIT;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new VigilancePower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Vigilance(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(VIT_UPG - VIT);
            upgradeDescription();
        }
    }
}
