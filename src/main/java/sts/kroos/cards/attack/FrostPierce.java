package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FrostPower;

/**
 * 寒光贯穿 - 1费, 造成 10 伤害 + (当前寒芒层数 × 1.5) 点额外伤害。
 *   - 强化: 额外伤害变为寒芒层数 × 2.0
 *
 * 额外伤害单独以一次 DamageAction 发出, 避免污染 baseDamage 显示。
 */
public class FrostPierce extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":FrostPierce";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/frost_pierce.png";

    private static final int COST = 1;
    private static final int DAMAGE = 8;

    public FrostPierce() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AttackEffect.BLUNT_HEAVY));

        int extra = frostScalingDamage(p);
        if (extra > 0) {
            addToBot(new DamageAction(m,
                    new DamageInfo(p, extra, this.damageTypeForTurn),
                    AttackEffect.SLASH_DIAGONAL));
        }
    }

    /** 额外伤害 = 当前[寒芒]层数 ×1.5 (强化后 ×2.0, 向下取整) */
    private int frostScalingDamage(AbstractPlayer p) {
        AbstractPower fp = p.getPower(FrostPower.POWER_ID);
        if (fp == null) return 0;
        int n = fp.amount;
        return this.upgraded ? (int)(n * 2.0F) : (int)(n * 1.5F);
    }

    @Override
    public AbstractKroosCard makeCopy() { return new FrostPierce(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeDescription();
        }
    }
}
