package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.ChargePower;
import sts.kroos.powers.DozePower;

/**
 * 扰梦(梦击) - 1费, 造成 10 (强化 13) 点伤害。
 *   - 若处于浅眠状态, 获得 1 层蓄势
 *   - 寒芒: 消耗 1 层寒芒, 伤害 +6 (强化 +8)
 */
public class DreamDisturb extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":DreamDisturb";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/dream_disturb.png";

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int CHARGE = 1;
    private static final int FROST_BONUS = 4;
    private static final int FROST_BONUS_UPG = 6;

    public DreamDisturb() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.isDreamStrike = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int dmg = this.damage;
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            dmg += this.upgraded ? FROST_BONUS_UPG : FROST_BONUS;
        }
        addToBot(new DamageAction(m,
                new DamageInfo(p, dmg, this.damageTypeForTurn),
                AttackEffect.SLASH_HORIZONTAL));
        if (p.hasPower(DozePower.POWER_ID)) {
            addToBot(new ApplyPowerAction(p, p, new ChargePower(p, CHARGE), CHARGE));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new DreamDisturb(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
