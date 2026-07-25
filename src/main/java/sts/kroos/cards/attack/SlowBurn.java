package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.DozePower;

/**
 * 慢悠悠(保留 梦击) - 1费, 造成 5 (强化 7) 点伤害。
 *   - 每保留 1 回合, 伤害 +2 (强化 +4)
 *   - 寒芒: 若在浅眠状态, 消耗 1 层寒芒, 伤害 +5 (强化 +8)
 *
 * 保留增加伤害使用原版 onRetained() 回调，参考 WindmillStrike。
 */
public class SlowBurn extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":SlowBurn";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/slow_burn.png";

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int RETAIN_BONUS = 2;
    private static final int RETAIN_BONUS_UPG = 4;
    private static final int FROST_BONUS = 5;
    private static final int FROST_BONUS_UPG = 8;

    public SlowBurn() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = RETAIN_BONUS;
        this.magicNumber = RETAIN_BONUS;
        this.selfRetain = true;
        this.isDreamStrike = true;
    }

    @Override
    public void onRetained() {
        this.upgradeDamage(this.magicNumber);
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int dmg = this.damage;
        if (p.hasPower(DozePower.POWER_ID) && canConsumeFrost(1)) {
            consumeFrost(1);
            dmg += this.upgraded ? FROST_BONUS_UPG : FROST_BONUS;
        }
        addToBot(new DamageAction(m,
                new DamageInfo(p, dmg, this.damageTypeForTurn),
                AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new SlowBurn(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeMagicNumber(RETAIN_BONUS_UPG - RETAIN_BONUS);
            upgradeDescription();
        }
    }
}
