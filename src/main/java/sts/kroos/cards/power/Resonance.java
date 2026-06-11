package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.ResonancePower;

/**
 * 共鸣 - 1费, 暴击时获得 1 层寒芒。
 *   - 强化: 固有
 */
public class Resonance extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Resonance";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/resonance.png";

    private static final int COST = 1;
    private static final int FROST = 1;

    public Resonance() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = FROST;
        this.magicNumber = FROST;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new ResonancePower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Resonance(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isInnate = true;
            upgradeDescription();
        }
    }
}
