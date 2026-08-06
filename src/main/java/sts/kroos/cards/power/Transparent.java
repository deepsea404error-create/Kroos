package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.TransparentPower;

/**
 * 通明 - 2费(强化 1费), 每消耗 5 层寒芒, 获得 1 点能量。
 */
public class Transparent extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Transparent";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/transparent.png";

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int AMOUNT = 1;

    public Transparent() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = AMOUNT;
        this.magicNumber = AMOUNT;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new TransparentPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Transparent(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            upgradeDescription();
        }
    }
}
