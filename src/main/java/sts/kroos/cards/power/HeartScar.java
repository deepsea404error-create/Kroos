package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FrostPower;
import sts.kroos.powers.HeartScarPower;

/**
 * 心之痕 - 2费, 每回合前 X 次触发寒芒效果时, 消耗的寒芒-1。获得 X 层寒芒。
 *   - 基础 X=1, 强化 X=2
 */
public class HeartScar extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":HeartScar";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/heart_scar.png";

    private static final int COST = 2;
    private static final int TRIGGERS = 1;
    private static final int UPGRADE_TRIGGERS = 1;

    public HeartScar() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = TRIGGERS;
        this.magicNumber = TRIGGERS;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new HeartScarPower(p, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p,
                new FrostPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new HeartScar(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_TRIGGERS);
            upgradeDescription();
        }
    }
}
