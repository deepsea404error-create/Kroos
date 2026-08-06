package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.CriticalPower;
import sts.kroos.powers.FlawPower;

/**
 * 穿云 - 1费, 造成 10 (强化 14) 点伤害, 施加 1 层破绽。
 *   - 寒芒: 本牌暴击时, 消耗 1 层寒芒, 本牌保留至下回合, 获得 1 费。
 *
 * 暴击判定: 玩家持有暴击层数 > 0 且有寒芒可消耗。
 */
public class PiercingCloud extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":PiercingCloud";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/piercing_cloud.png";

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int FLAW = 1;

    public PiercingCloud() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = FLAW;
        this.magicNumber = FLAW;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        boolean willCrit = p.hasPower(CriticalPower.POWER_ID)
                && p.getPower(CriticalPower.POWER_ID).amount > 0;
        addToBot(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AttackEffect.SLASH_DIAGONAL));
        addToBot(new ApplyPowerAction(m, p,
                new FlawPower(m, this.magicNumber), this.magicNumber));

        if (willCrit && canConsumeFrost(1)) {
            consumeFrost(1);
            // 暴击时：消耗1层寒芒，本牌回到手牌，获得1费
            this.returnToHand = true;
            addToBot(new GainEnergyAction(1));
        } else {
            this.returnToHand = false;
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new PiercingCloud(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
