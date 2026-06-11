package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

import java.util.List;

/**
 * 连射 - 1费, 造成 6 伤害。
 *   - 若上一张牌是[攻击]牌, 本牌耗能变为 0
 *   - 寒芒: 消耗 1 层寒芒, 抽 1 张牌 (强化后并升级它)
 */
public class RapidFire extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":RapidFire";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/rapid_fire.png";

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 3;

    public RapidFire() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AttackEffect.BLUNT_LIGHT));
        if (canConsumeFrost(1)) {
            consumeFrost(1);
            DrawCardAction draw = new DrawCardAction(p, 1);
            addToBot(draw);
            // TODO: 强化后将抽到的牌升级 — 暂留简单抽 1, 升级需要 hand peek 后处理
        }
    }

    // ===== 动态费用: 上一张牌为攻击则 0 费 =====

    @Override
    public void applyPowers() {
        super.applyPowers();
        updateDynamicCost();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        updateDynamicCost();
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        updateDynamicCost();
    }

    private void updateDynamicCost() {
        boolean lastAttack = isLastPlayedAttack();
        int target = lastAttack ? 0 : this.cost;
        if (this.costForTurn != target) {
            this.costForTurn = target;
            this.isCostModified = this.costForTurn != this.cost;
        }
    }

    private boolean isLastPlayedAttack() {
        if (AbstractDungeon.actionManager == null) return false;
        List<AbstractCard> played = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        if (played == null || played.isEmpty()) return false;
        AbstractCard last = played.get(played.size() - 1);
        return last != null && last.type == CardType.ATTACK;
    }

    @Override
    public AbstractKroosCard makeCopy() { return new RapidFire(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
