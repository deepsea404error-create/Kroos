package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.ChargePower;

/**
 * 终幕(梦击) - 1费, 造成 10 (强化 14) 点伤害。
 *   - 额外造成"当前蓄势 × 4 (强化 × 6)"点伤害
 *   - 寒芒: 消耗 1 层寒芒, 获得 1 (强化 2) 层蓄势
 *
 * 不主动退出浅眠 — 仅读取当前蓄势层数, 配合"溃缩 / 警觉 / 奋起"等退出浅眠卡使用。
 */
public class FinalCurtain extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":FinalCurtain";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/final_curtain.png";

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int CHARGE_MULT = 4;
    private static final int CHARGE_MULT_UPG = 6;
    private static final int FROST_CHARGE = 1;
    private static final int FROST_CHARGE_UPG = 2;

    public FinalCurtain() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.isDreamStrike = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AttackEffect.SLASH_HEAVY));

        AbstractPower cp = p.getPower(ChargePower.POWER_ID);
        int charges = cp != null ? cp.amount : 0;
        if (charges > 0) {
            int mult = this.upgraded ? CHARGE_MULT_UPG : CHARGE_MULT;
            int extra = charges * mult;
            addToBot(new DamageAction(m,
                    new DamageInfo(p, extra, this.damageTypeForTurn),
                    AttackEffect.FIRE));
        }

        if (canConsumeFrost(1)) {
            consumeFrost(1);
            int add = this.upgraded ? FROST_CHARGE_UPG : FROST_CHARGE;
            addToBot(new ApplyPowerAction(p, p, new ChargePower(p, add), add));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new FinalCurtain(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
