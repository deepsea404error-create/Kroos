package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VigorPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.CriticalPower;
import sts.kroos.powers.FocusPower;

/**
 * 上弦 - 1费, 获得 2 层专注。你的下次攻击造成 4 点额外伤害。
 *   - 强化: 获得 1 层暴击, 下次攻击 +6 点
 *
 * 下次攻击额外伤害复用原版 VigorPower。
 */
public class WindUp extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":WindUp";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/wind_up.png";

    private static final int COST = 1;
    private static final int FOCUS = 2;
    private static final int VIGOR = 4;
    private static final int VIGOR_UPG = 6;

    public WindUp() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = VIGOR;
        this.magicNumber = VIGOR;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        if (this.upgraded) {
            addToBot(new ApplyPowerAction(p, p, new CriticalPower(p, 1), 1));
        } else {
            addToBot(new ApplyPowerAction(p, p, new FocusPower(p, FOCUS), FOCUS));
        }
        addToBot(new ApplyPowerAction(p, p,
                new VigorPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new WindUp(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(VIGOR_UPG - VIGOR);
            upgradeDescription();
        }
    }
}
