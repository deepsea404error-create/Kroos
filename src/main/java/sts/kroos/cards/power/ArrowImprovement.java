package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.ArrowImprovementPower;

/**
 * 箭矢改良 - 1费, [箭矢]牌额外造成 3 (强化 4 固有) 点伤害。
 */
public class ArrowImprovement extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":ArrowImprovement";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/arrow_improvement.png";

    private static final int COST = 1;
    private static final int BONUS = 3;
    private static final int BONUS_UPG = 4;

    public ArrowImprovement() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = BONUS;
        this.magicNumber = BONUS;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new ArrowImprovementPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new ArrowImprovement(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(BONUS_UPG - BONUS);
            this.isInnate = true;
            upgradeDescription();
        }
    }
}
