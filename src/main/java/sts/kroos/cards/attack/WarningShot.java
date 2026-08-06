package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FlawPower;

/**
 * 警戒射击(梦击) - 1费, 造成 8 伤害, 施加 1 层破绽。
 *   - 寒芒: 消耗 1 层寒芒, 给予 1 层易伤
 *   - 强化: 11 伤, 2 层破绽; 寒芒给予 2 易伤
 */
public class WarningShot extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":WarningShot";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/warning_shot.png";

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int FLAW = 1;
    private static final int UPGRADE_FLAW = 1;
    private static final int FROST_VULN = 1;
    private static final int FROST_VULN_UPG = 2;

    public WarningShot() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = FLAW;
        this.magicNumber = FLAW;
        this.isDreamStrike = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AttackEffect.SLASH_DIAGONAL));
        addToBot(new ApplyPowerAction(m, p,
                new FlawPower(m, this.magicNumber), this.magicNumber));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            int vuln = this.upgraded ? FROST_VULN_UPG : FROST_VULN;
            addToBot(new ApplyPowerAction(m, p,
                    new VulnerablePower(m, vuln, false), vuln));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new WarningShot(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_FLAW);
            upgradeDescription();
        }
    }
}
