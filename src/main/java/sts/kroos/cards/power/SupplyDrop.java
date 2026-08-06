package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.ExtraDrawPower;

/**
 * 物资补给 - 1费 (强化 0费 固有), 每回合额外抽 1 张牌。
 */
public class SupplyDrop extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":SupplyDrop";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/supply_drop.png";

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int AMOUNT = 1;

    public SupplyDrop() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = AMOUNT;
        this.magicNumber = AMOUNT;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new ExtraDrawPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new SupplyDrop(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            this.isInnate = true;
            upgradeDescription();
        }
    }
}
