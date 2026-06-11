package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 百炼 - 1 (强化 0) 费, 消耗, 保留。
 *   - 寒芒: 消耗 5 层寒芒, 获得 3 (强化 4) 层力量
 */
public class HundredForge extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":HundredForge";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/hundred_forge.png";

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int STRENGTH = 3;
    private static final int UPGRADE_STRENGTH = 1;

    public HundredForge() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = STRENGTH;
        this.magicNumber = STRENGTH;
        this.exhaust = true;
        this.selfRetain = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        if (canConsumeFrost(5)) {
            consumeFrost(5);
            addToBot(new ApplyPowerAction(p, p,
                    new StrengthPower(p, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new HundredForge(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            this.upgradeMagicNumber(UPGRADE_STRENGTH);
            upgradeDescription();
        }
    }
}
