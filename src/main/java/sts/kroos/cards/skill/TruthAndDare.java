package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.CriticalPower;
import sts.kroos.powers.FrostPower;

/**
 * 真心的话和想冒的险 - 1费, 消耗。
 *   - 获得 4 (强化 6) 层寒芒和 1 (强化 2) 层暴击
 */
public class TruthAndDare extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":TruthAndDare";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/truth_and_dare.png";

    private static final int COST = 1;
    private static final int FROST = 4;
    private static final int UPGRADE_FROST = 2;
    private static final int CRIT = 1;
    private static final int UPGRADE_CRIT = 1;

    public TruthAndDare() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = FROST;
        this.magicNumber = FROST;
        this.exhaust = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new FrostPower(p, this.magicNumber), this.magicNumber));
        int crit = this.upgraded ? (CRIT + UPGRADE_CRIT) : CRIT;
        addToBot(new ApplyPowerAction(p, p,
                new CriticalPower(p, crit), crit));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new TruthAndDare(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FROST);
            upgradeDescription();
        }
    }
}
