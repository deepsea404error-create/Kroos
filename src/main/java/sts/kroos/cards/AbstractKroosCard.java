package sts.kroos.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import sts.kroos.KroosMod;
import sts.kroos.patches.KroosEnum;

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
     * 消耗寒芒。寒芒 power 实现完成前留作 hook。
     * @param amount 期望消耗的层数 (尚未考虑心之痕减免)
     * @return 实际消耗的层数(若不足, 返回 0)
     */
    public int consumeFrost(int amount) {
        // TODO: 寒芒 power 实现后接入实际消耗逻辑
        return 0;
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
