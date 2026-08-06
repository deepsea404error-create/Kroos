package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FlawPower;
import sts.kroos.powers.HitPower;

/**
 * 裂击 - 1费, 造成 4 (强化 6) 点伤害 2 次。
 *   - 若目标拥有[中的], 每次伤害 +2 (强化 +3)
 *   - 寒芒: 消耗 1 层寒芒, 施加 1 层破绽
 */
public class RiftStrike extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":RiftStrike";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/rift_strike.png";

    private static final int COST = 1;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int HIT_BONUS = 2;
    private static final int HIT_BONUS_UPG = 3;
    private static final int FLAW = 1;

    public RiftStrike() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int dmg = this.damage;
        if (m != null && m.hasPower(HitPower.POWER_ID)) {
            dmg += this.upgraded ? HIT_BONUS_UPG : HIT_BONUS;
        }
        for (int i = 0; i < 2; i++) {
            addToBot(new DamageAction(m,
                    new DamageInfo(p, dmg, this.damageTypeForTurn),
                    AttackEffect.SLASH_DIAGONAL));
        }
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            addToBot(new ApplyPowerAction(m, p, new FlawPower(m, FLAW), FLAW));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new RiftStrike(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
