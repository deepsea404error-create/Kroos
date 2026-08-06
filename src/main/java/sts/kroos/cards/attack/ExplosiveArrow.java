package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 爆炸箭(箭矢) - 1费, 造成 6 (强化 8) 点伤害, 对所有敌人造成 5 (强化 6) 点伤害。
 *   - 寒芒: 消耗 1 层寒芒, 对所有敌人额外造成 2 (强化 4) 点伤害
 *
 * 主目标承受 主伤 + AOE 伤; 其余敌人仅承受 AOE 伤。
 */
public class ExplosiveArrow extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":ExplosiveArrow";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/explosive_arrow.png";

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int AOE = 5;
    private static final int UPGRADE_AOE = 1;
    private static final int FROST_AOE = 2;
    private static final int FROST_AOE_UPG = 4;

    public ExplosiveArrow() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = AOE;
        this.magicNumber = AOE;
        this.isArrow = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        // 主目标
        addToBot(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AttackEffect.SLASH_HEAVY));

        // AOE 部分: this.magicNumber + (寒芒触发) 加成
        int aoe = this.magicNumber;
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            aoe += this.upgraded ? FROST_AOE_UPG : FROST_AOE;
        }
        int n = AbstractDungeon.getCurrRoom().monsters.monsters.size();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = aoe;
        addToBot(new DamageAllEnemiesAction(p, arr,
                this.damageTypeForTurn, AttackEffect.FIRE, true));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new ExplosiveArrow(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_AOE);
            upgradeDescription();
        }
    }
}
