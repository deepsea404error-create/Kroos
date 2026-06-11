package sts.kroos.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FlawConfirmPower;

/**
 * 破绽确认 - 1费, 每回合前 1 张牌对敌人造成伤害时, 施加 1 层破绽。
 *   - 强化: 固有
 */
public class FlawConfirm extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":FlawConfirm";
    private static final String IMG = KroosMod.RES_ROOT + "cards/power/flaw_confirm.png";

    private static final int COST = 1;
    private static final int TRIGGERS = 1;

    public FlawConfirm() {
        super(ID, IMG, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = TRIGGERS;
        this.magicNumber = TRIGGERS;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new FlawConfirmPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new FlawConfirm(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isInnate = true;
            upgradeDescription();
        }
    }
}
