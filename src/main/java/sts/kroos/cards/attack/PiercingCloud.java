package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.actions.RetainSelfToHandAction;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.CriticalPower;
import sts.kroos.powers.FlawPower;

/**
 * 穿云 - 1费, 造成 10 (强化 14) 点伤害, 施加 1 层破绽。
 *   - 寒芒: 本牌暴击时, 消耗 1 层寒芒, 保留此牌, 其耗能变为 0。
 *
 * 暴击判定方式与"追猎"一致 — 打出时玩家持 CriticalPower 且 amount > 0 即视为本牌暴击。
 * 保留: 用 RetainSelfToHandAction 把本牌从弃牌堆取回手牌并 cost=0。
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
            addToBot(new RetainSelfToHandAction(this));
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
