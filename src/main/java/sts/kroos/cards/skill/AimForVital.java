package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FlawPower;
import sts.kroos.powers.FrostPower;

/**
 * 要害瞄准 - 1费, 对敌人施加 1 层破绽; 获得 2 点临时力量, 1 层寒芒, 抽 1 张牌。
 *   - 强化: 2 层破绽, 2 层寒芒, 抽 2 张牌 (力量不变 1)
 */
public class AimForVital extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":AimForVital";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/aim_for_vital.png";

    private static final int COST = 1;
    private static final int FLAW = 1;
    private static final int UPGRADE_FLAW = 1;
    private static final int FROST = 1;
    private static final int UPGRADE_FROST = 1;
    private static final int DRAW = 1;
    private static final int UPGRADE_DRAW = 1;
    private static final int STRENGTH = 2;

    public AimForVital() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseMagicNumber = FLAW;
        this.magicNumber = FLAW;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new FlawPower(m, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, STRENGTH), STRENGTH));
        addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, STRENGTH), STRENGTH));
        int frost = this.upgraded ? (FROST + UPGRADE_FROST) : FROST;
        addToBot(new ApplyPowerAction(p, p, new FrostPower(p, frost), frost));
        int draw = this.upgraded ? (DRAW + UPGRADE_DRAW) : DRAW;
        addToBot(new DrawCardAction(p, draw));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new AimForVital(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FLAW);
            upgradeDescription();
        }
    }
}
