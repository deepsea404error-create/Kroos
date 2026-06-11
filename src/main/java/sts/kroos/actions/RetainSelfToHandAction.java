package sts.kroos.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 将指定卡牌从弃牌堆取回手牌, 并将其本场战斗内耗能设为 0。
 *
 * 用于"穿云"暴击触发后保留此牌的实现:
 *   - 卡牌打出后被 UseCardAction 移到弃牌堆 (本 action 在 use() 内入队, 执行时已落弃)
 *   - 找到 target 实例 → 从 discardPile 移除 → 修改 costForTurn=0 → addToHand
 *
 * 若弃牌堆中找不到 target (例如其他效果先移走它), 直接放弃, 不抛错。
 */
public class RetainSelfToHandAction extends AbstractGameAction {

    private final AbstractCard target;

    public RetainSelfToHandAction(AbstractCard target) {
        this.target = target;
    }

    @Override
    public void update() {
        this.isDone = true;
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || target == null) return;
        if (!p.discardPile.contains(target)) return;
        if (p.hand.size() >= 10) return; // 满手放弃

        p.discardPile.removeCard(target);
        target.setCostForTurn(0);
        target.cost = 0;
        target.costForTurn = 0;
        target.isCostModified = true;
        p.hand.addToHand(target);
        target.lighten(false);
        target.applyPowers();
        p.hand.refreshHandLayout();
    }
}
