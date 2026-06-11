package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 穿透箭(箭矢) - 1费, 对所有敌人造成 9 伤害。
 *   - 寒芒: 消耗 2 层寒芒, 伤害+2 (强化+3)
 *
 * 多目标牌不接散射逻辑 (本身已 AOE)。
 */
public class Volley extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":Volley";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/volley.png";

    private static final int COST = 1;
    private static final int DAMAGE = 9;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int FROST_BONUS = 2;
    private static final int FROST_BONUS_UPG = 3;

    public Volley() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = DAMAGE;
        this.isMultiDamage = true;
        this.isArrow = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int bonus = 0;
        if (canConsumeFrost(2)) {
            consumeFrost(2);
            bonus = this.upgraded ? FROST_BONUS_UPG : FROST_BONUS;
        }
        int dmg = effectiveDamage() + bonus;
        // 对所有敌人造成 dmg
        int n = AbstractDungeon.getCurrRoom().monsters.monsters.size();
        int[] dmgArr = new int[n];
        for (int i = 0; i < n; i++) dmgArr[i] = dmg;

        addToBot(new DamageAllEnemiesAction(p, dmgArr,
                this.damageTypeForTurn, AttackEffect.SLASH_DIAGONAL, true));
    }

    /** multiDamage 已由 calculateCardDamage 填充, 这里取主目标值即可 */
    private int effectiveDamage() {
        return this.damage;
    }

    @Override
    public AbstractKroosCard makeCopy() { return new Volley(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
