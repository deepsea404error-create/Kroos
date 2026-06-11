package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.CriticalPower;
import sts.kroos.powers.FlawPower;

/**
 * 追猎 - 1费, 造成 7 伤害。
 *   - 本牌暴击时, 额外造成 1 次伤害, 施加 1 层破绽
 *     (暴击判定 = 打出时玩家持有[暴击]power 且层数>0)
 *   - 寒芒: 消耗 1 层寒芒, 伤害+2 (强化+3)
 *
 * 暴击消耗逻辑由 CriticalPower.onUseCard 自处理, 本牌不直接 -1 暴击。
 */
public class Hunt extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Hunt";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/hunt.png";

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int FROST_BONUS = 2;
    private static final int FROST_BONUS_UPG = 3;

    public Hunt() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        boolean willCrit = hasCritical(p);
        int bonus = 0;
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            bonus = this.upgraded ? FROST_BONUS_UPG : FROST_BONUS;
        }
        int dmg = this.damage + bonus;

        addToBot(new DamageAction(m,
                new DamageInfo(p, dmg, this.damageTypeForTurn),
                AttackEffect.SLASH_DIAGONAL));
        if (willCrit) {
            addToBot(new DamageAction(m,
                    new DamageInfo(p, dmg, this.damageTypeForTurn),
                    AttackEffect.SLASH_HEAVY));
            addToBot(new ApplyPowerAction(m, p,
                    new FlawPower(m, 1), 1));
        }
    }

    private static boolean hasCritical(AbstractPlayer p) {
        return p.hasPower(CriticalPower.POWER_ID)
                && p.getPower(CriticalPower.POWER_ID).amount > 0;
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Hunt(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
