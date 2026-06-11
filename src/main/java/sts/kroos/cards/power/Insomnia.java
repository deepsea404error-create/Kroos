package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.InsomniaPower;

/**
 * 无法入眠 - 2费, 每回合开始时获得 2 层寒芒。
 *   - 寒芒: 消耗 1 层寒芒, 回复 2 (强化 4) HP
 *   - 强化: 1 费
 */
public class Insomnia extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Insomnia";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/insomnia.png";

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int FROST_PER_TURN = 2;
    private static final int HEAL = 2;
    private static final int HEAL_UPG = 4;

    public Insomnia() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = FROST_PER_TURN;
        this.magicNumber = FROST_PER_TURN;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new InsomniaPower(p, this.magicNumber), this.magicNumber));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new HealAction(p, p, this.upgraded ? HEAL_UPG : HEAL));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Insomnia(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            upgradeDescription();
        }
    }
}
