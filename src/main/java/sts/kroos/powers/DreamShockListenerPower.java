package sts.kroos.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;

import java.util.ArrayList;
import java.util.List;

/**
 * 惊梦监听 — 内部辅助 power, 不进入本地化系统。
 *
 * 含义: 监听[浅眠]退出事件。每次退出浅眠时, 将列表中的所有"惊梦"实例从弃牌堆
 *       取回手牌, 并使其耗能 -1; 处理完成后自移除。
 *
 * 不可堆叠 — 多次打出"惊梦"通过 addPending 把新实例加入同一 listener 的待回收列表。
 */
public class DreamShockListenerPower extends AbstractPower implements IDozeExitListener {

    public static final String POWER_ID = KroosMod.MOD_ID + ":DreamShockListener";
    public static final String NAME = "惊梦";

    private final List<AbstractCard> pending = new ArrayList<>();

    public DreamShockListenerPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        updateDescription();
    }

    // 内部不可见 power, 无图标; 阻止 flash() 时 NPE
    @Override
    public void flash() {}

    public void addPending(AbstractCard c) {
        if (c != null) {
            pending.add(c);
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        this.description = "下次退出[#b0c4de]浅眠[]时, 将 " + pending.size() + " 张[惊梦]从弃牌堆取回手牌, 耗能 -1。";
    }

    @Override
    public void onDozeExited() {
        if (owner == null || pending.isEmpty()) {
            scheduleRemoval();
            return;
        }
        this.flash();
        for (AbstractCard c : pending) {
            if (!AbstractDungeon.player.discardPile.contains(c)) continue;
            AbstractDungeon.player.discardPile.removeCard(c);
            c.cost = Math.max(0, c.cost - 1);
            c.costForTurn = c.cost;
            c.isCostModified = true;
            if (AbstractDungeon.player.hand.size() < 10) {
                AbstractDungeon.player.hand.addToHand(c);
                c.lighten(false);
            } else {
                AbstractDungeon.player.discardPile.addToTop(c);
            }
            c.applyPowers();
        }
        pending.clear();
        AbstractDungeon.player.hand.refreshHandLayout();
        scheduleRemoval();
    }

    private void scheduleRemoval() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
                owner, owner, POWER_ID));
    }
}
