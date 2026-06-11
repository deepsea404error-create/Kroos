package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.DozePower;

/**
 * 打哈欠(梦击) - 1费, 造成 5 伤害。
 *   - 若敌人意图不为[攻击], 进入浅眠状态
 *   - 寒芒: 消耗 1 层寒芒, 伤害+4 (强化+6)
 *   - 强化: 8 伤
 */
public class Yawn extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Yawn";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/yawn.png";

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int FROST_BONUS = 4;
    private static final int FROST_BONUS_UPG = 6;

    public Yawn() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.isDreamStrike = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int bonus = 0;
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            bonus = this.upgraded ? FROST_BONUS_UPG : FROST_BONUS;
        }
        int dmg = this.damage + bonus;
        addToBot(new DamageAction(m,
                new DamageInfo(p, dmg, this.damageTypeForTurn),
                AttackEffect.BLUNT_LIGHT));

        if (m != null && !isAttackIntent(m.intent) && !p.hasPower(DozePower.POWER_ID)) {
            addToBot(new ApplyPowerAction(p, p, new DozePower(p)));
        }
    }

    private static boolean isAttackIntent(Intent intent) {
        if (intent == null) return false;
        switch (intent) {
            case ATTACK:
            case ATTACK_BUFF:
            case ATTACK_DEBUFF:
            case ATTACK_DEFEND:
                return true;
            default:
                return false;
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Yawn(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
