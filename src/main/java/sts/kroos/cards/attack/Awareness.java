package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.DozePower;
import sts.kroos.powers.FrostPower;

/**
 * 警觉(梦击) - 1费, 造成 10 (强化 14) 点伤害。
 *   - 若处于浅眠, 退出浅眠状态
 *   - 获得 2 (强化 3) 层寒芒
 */
public class Awareness extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Awareness";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/awareness.png";

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int FROST = 2;
    private static final int UPGRADE_FROST = 1;

    public Awareness() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = FROST;
        this.magicNumber = FROST;
        this.isDreamStrike = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AttackEffect.SLASH_DIAGONAL));
        if (p.hasPower(DozePower.POWER_ID)) {
            addToBot(new RemoveSpecificPowerAction(p, p, DozePower.POWER_ID));
        }
        addToBot(new ApplyPowerAction(p, p,
                new FrostPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Awareness(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_FROST);
            upgradeDescription();
        }
    }
}
