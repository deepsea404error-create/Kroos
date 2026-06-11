package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FlawPower;

/**
 * 虚损箭(箭矢) - 1费, 造成 9 伤害, 施加 1 层破绽。
 *   - 寒芒: 消耗 1 层寒芒, 伤害+3, 给予 1 层虚弱
 *
 * 箭矢自动获取专注(=energyOnUse) 由 AbstractKroosCard.afterUseHook 处理, 本牌不显式处理。
 * 散射也由 scatterIfArrow 助手统一调用。
 */
public class FlawShot extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":FlawShot";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/flaw_shot.png";

    private static final int COST = 1;
    private static final int DAMAGE = 9;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int FROST_BONUS = 3;
    private static final int FROST_BONUS_UPG = 5;
    private static final int FLAW = 1;
    private static final int UPGRADE_FLAW = 1;

    public FlawShot() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = FLAW;
        this.magicNumber = FLAW;
        this.isArrow = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int bonus = 0;
        boolean frostTriggered = false;
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            bonus = this.upgraded ? FROST_BONUS_UPG : FROST_BONUS;
            frostTriggered = true;
        }
        int dmg = this.damage + bonus;
        addToBot(new DamageAction(m,
                new DamageInfo(p, dmg, this.damageTypeForTurn),
                AttackEffect.SLASH_DIAGONAL));
        scatterIfArrow(p, m, dmg);
        addToBot(new ApplyPowerAction(m, p, new FlawPower(m, this.magicNumber), this.magicNumber));
        if (frostTriggered) {
            addToBot(new ApplyPowerAction(m, p, new WeakPower(m, 1, false), 1));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new FlawShot(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_FLAW);
            upgradeDescription();
        }
    }
}
