package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 二连射(初始) - 1费, 造成5点伤害2次。
 * 寒芒: 消耗1层寒芒, 每次伤害+2 (强化后+3)。
 */
public class DoubleShot extends AbstractKroosCard {

    public static final String ID = KroosMod.MOD_ID + ":DoubleShot";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/double_shot.png";

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int FROST_BONUS = 2;
    private static final int FROST_BONUS_UPG = 3;

    public DoubleShot() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.isMultiDamage = false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 寒芒触发: 同步判断分支, action 队列减层
        int bonus = 0;
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            bonus = this.upgraded ? FROST_BONUS_UPG : FROST_BONUS;
        }
        int dmg = this.damage + bonus;
        for (int i = 0; i < 2; i++) {
            addToBot(new DamageAction(m,
                    new DamageInfo(p, dmg, this.damageTypeForTurn),
                    AttackEffect.BLUNT_LIGHT));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() {
        return new DoubleShot();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
