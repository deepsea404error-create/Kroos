package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * 强化箭(箭矢 消耗) - 1费, 造成 4 点伤害。
 *   - 每打出一次，这张牌在本局游戏中的伤害值永久性增加 2 (强化 +3) (上限 22, 强化 44)
 *   - 寒芒: 若未达上限, 消耗 1 层寒芒, 本牌伤害额外 +2
 *
 * 逻辑参考原版遗传算法 (GeneticAlgorithm):
 *   - 使用 misc 字段存储永久增长的基础伤害 (跨战斗/跨存档保留)
 *   - 使用 IncreaseMiscAction 同步更新所有同 UUID 实例 (主牌组 + 战斗中副本)
 */
public class ReinforcedArrow extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":ReinforcedArrow";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/reinforced_arrow.png";

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 4;
    private static final int PER_USE_BONUS = 2;
    private static final int PER_USE_BONUS_UPG = 3;
    private static final int CAP = 22;
    private static final int CAP_UPG = 44;
    private static final int FROST_EXTRA = 2;

    public ReinforcedArrow() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.misc = BASE_DAMAGE;
        this.baseDamage = this.misc;
        this.exhaust = true;
        this.isArrow = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int cap = this.upgraded ? CAP_UPG : CAP;
        int dmg = Math.min(cap, this.misc);

        boolean atCap = dmg >= cap;
        if (!atCap && canConsumeFrost(1)) {
            consumeFrost(1);
            dmg = Math.min(cap, dmg + FROST_EXTRA);
        }

        addToBot(new DamageAction(m,
                new DamageInfo(p, dmg, this.damageTypeForTurn),
                AttackEffect.SLASH_DIAGONAL));
        scatterIfArrow(p, m, dmg);

        // 打出后永久增加基础伤害 (参考遗传算法: IncreaseMiscAction 同步所有同 UUID 实例)
        int per = this.upgraded ? PER_USE_BONUS_UPG : PER_USE_BONUS;
        addToBot(new IncreaseMiscAction(this.uuid, this.misc, per));
    }

    /** 显示伤害按 misc 同步, 与遗传算法一致 */
    @Override
    public void applyPowers() {
        this.baseDamage = Math.min(this.upgraded ? CAP_UPG : CAP, this.misc);
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.baseDamage = Math.min(this.upgraded ? CAP_UPG : CAP, this.misc);
        super.calculateCardDamage(mo);
    }

    @Override
    public AbstractKroosCard makeCopy() { return new ReinforcedArrow(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeDescription();
        }
    }
}
