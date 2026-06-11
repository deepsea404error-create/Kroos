package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.util.BattleCounters;

/**
 * 强化箭(箭矢 消耗) - 1费, 造成 4 点伤害。
 *   - 本局每使用过 1 次强化箭, 伤害 +2 (强化 +3) (上限 22, 强化 44)
 *   - 寒芒: 若未达上限, 消耗 1 层寒芒, 本牌伤害额外 +2
 *
 * 计数走 BattleCounters, 每场战斗自动清零。
 */
public class ReinforcedArrow extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":ReinforcedArrow";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/reinforced_arrow.png";
    private static final String COUNTER_KEY = "ReinforcedArrow:plays";

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 4;
    private static final int PER_USE_BONUS = 2;
    private static final int PER_USE_BONUS_UPG = 3;
    private static final int CAP = 22;
    private static final int CAP_UPG = 44;
    private static final int FROST_EXTRA = 2;

    public ReinforcedArrow() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.exhaust = true;
        this.isArrow = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int plays = BattleCounters.get(COUNTER_KEY);
        int per = this.upgraded ? PER_USE_BONUS_UPG : PER_USE_BONUS;
        int cap = this.upgraded ? CAP_UPG : CAP;
        int dmg = Math.min(cap, BASE_DAMAGE + plays * per);

        boolean atCap = dmg >= cap;
        if (!atCap && canConsumeFrost(1)) {
            consumeFrost(1);
            dmg = Math.min(cap, dmg + FROST_EXTRA);
        }

        addToBot(new DamageAction(m,
                new DamageInfo(p, dmg, this.damageTypeForTurn),
                AttackEffect.SLASH_DIAGONAL));
        scatterIfArrow(p, m, dmg);

        BattleCounters.inc(COUNTER_KEY);
    }

    /** 显示伤害也按计数器同步, 便于玩家直观判断 */
    @Override
    public void applyPowers() {
        int plays = BattleCounters.get(COUNTER_KEY);
        int per = this.upgraded ? PER_USE_BONUS_UPG : PER_USE_BONUS;
        int cap = this.upgraded ? CAP_UPG : CAP;
        this.baseDamage = Math.min(cap, BASE_DAMAGE + plays * per);
        super.applyPowers();
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
