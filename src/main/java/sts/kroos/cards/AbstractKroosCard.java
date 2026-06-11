package sts.kroos.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.patches.KroosEnum;
import sts.kroos.powers.FrostPower;

/**
 * 寒芒克洛丝所有卡牌的公共抽象基类。
 *
 * 提供:
 *   - 特殊牌类型 tag: 箭矢牌 / 梦击牌 / A1小队牌
 *   - 寒芒消耗 hook: consumeFrost(amount)
 *   - 升级前后高亮的统一描述切换辅助
 *
 * 子类约定:
 *   - 在静态字段写入 ID/NAME/DESCRIPTION/UPGRADE_DESCRIPTION
 *   - 在构造函数标记对应 tag(如 isArrow / isDreamStrike / isA1)
 */
public abstract class AbstractKroosCard extends CustomCard {

    // ===== 特殊牌类型标记 (在子类构造函数中设置) =====
    /** 箭矢牌: 打出后获得其消耗能量数等量的专注层数, 与多个 power 联动 */
    public boolean isArrow = false;
    /** 梦击牌: 在浅眠状态下不会被降低伤害 */
    public boolean isDreamStrike = false;
    /** A1小队牌: 均为虚无、消耗类型无色卡牌 */
    public boolean isA1Squad = false;

    public AbstractKroosCard(final String id,
                             final String img,
                             final int cost,
                             final CardType type,
                             final CardRarity rarity,
                             final CardTarget target) {
        this(id, img, cost, type, KroosEnum.KROOS_COLOR, rarity, target);
    }

    public AbstractKroosCard(final String id,
                             final String img,
                             final int cost,
                             final CardType type,
                             final CardColor color,
                             final CardRarity rarity,
                             final CardTarget target) {
        super(id, getName(id), img, cost, getDescription(id), type, color, rarity, target);
    }

    private static String getName(String id) {
        CardStrings s = CardCrawlGame.languagePack.getCardStrings(id);
        return s.NAME;
    }

    private static String getDescription(String id) {
        CardStrings s = CardCrawlGame.languagePack.getCardStrings(id);
        return s.DESCRIPTION;
    }

    /**
     * 同步检查玩家身上的[寒芒]层数是否足以支付 amount。
     * 不修改层数。各张卡牌应先用此方法走分支, 再调用 consumeFrost 进入 action 队列。
     */
    public static boolean canConsumeFrost(int amount) {
        AbstractPower p = currentFrost();
        return p != null && p.amount >= amount;
    }

    /**
     * 消耗 amount 层寒芒(进入 action 队列)。
     * 返回值: 实际尝试消耗的层数。若玩家寒芒不足, 返回 0 且不入队。
     *
     * 注: 心之痕的减免逻辑应该在 [HeartScar power] 内自行 hook ReducePowerAction
     * 触发时进行抵扣, 而非耦合到本方法。
     */
    public int consumeFrost(int amount) {
        if (!canConsumeFrost(amount)) return 0;
        AbstractPower p = currentFrost();
        addToBot(new ReducePowerAction(p.owner, p.owner, p.ID, amount));
        return amount;
    }

    private static AbstractPower currentFrost() {
        if (AbstractDungeon.player == null) return null;
        return AbstractDungeon.player.getPower(FrostPower.POWER_ID);
    }

    /**
     * 升级时切换到升级描述。子类应在 upgrade() 中调用。
     */
    protected void upgradeDescription() {
        CardStrings s = CardCrawlGame.languagePack.getCardStrings(this.cardID);
        this.rawDescription = s.UPGRADE_DESCRIPTION;
        this.initializeDescription();
    }
}
