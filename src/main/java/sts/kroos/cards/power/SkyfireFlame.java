package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.SkyfireFlamePower;

/**
 * 天坠之火 - 1费, [中的]效果造成伤害 +4 (强化 +6) 点。
 */
public class SkyfireFlame extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":SkyfireFlame";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/skyfire_flame.png";

    private static final int COST = 1;
    private static final int BONUS = 4;
    private static final int BONUS_UPG = 6;

    public SkyfireFlame() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = BONUS;
        this.magicNumber = BONUS;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new SkyfireFlamePower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new SkyfireFlame(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(BONUS_UPG - BONUS);
            upgradeDescription();
        }
    }
}
