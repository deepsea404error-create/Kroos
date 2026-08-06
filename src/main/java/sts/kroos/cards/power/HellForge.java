package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.HellForgePower;

/**
 * 狱淬 - 2费, 攻击带有"中的"的敌人，额外造成 !M! 点伤害。
 */
public class HellForge extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":HellForge";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/hellforge.png";

    private static final int COST = 2;
    private static final int BONUS = 10;
    private static final int BONUS_UPG = 15;

    public HellForge() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = BONUS;
        this.magicNumber = BONUS;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new HellForgePower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new HellForge(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(BONUS_UPG - BONUS);
            upgradeDescription();
        }
    }
}
