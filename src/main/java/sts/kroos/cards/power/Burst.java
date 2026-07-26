package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.BurstPower;
import sts.kroos.powers.FocusPower;

/**
 * 爆发 - 1费，获得爆发层数；寒芒: 消耗寒芒, 获得专注
 */
public class Burst extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Burst";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/burst.png";
    private static final int COST = 1;
    private static final int BURST_STACKS = 1;
    private static final int FOCUS = 1;
    private static final int UPG_FOCUS = 2;

    public Burst() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = FOCUS;
        this.magicNumber = FOCUS;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int stacks = this.upgraded ? 2 : BURST_STACKS;
        addToBot(new ApplyPowerAction(p, p, new BurstPower(p, stacks), stacks));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new ApplyPowerAction(p, p, new FocusPower(p, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Burst(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPG_FOCUS - FOCUS);
            upgradeDescription();
        }
    }
}
