package sts.kroos.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.patches.KroosEnum;
import sts.kroos.powers.ArrowImprovementPower;
import sts.kroos.powers.FocusPower;
import sts.kroos.powers.FrostPower;
import sts.kroos.powers.ScatterPower;

/**
 * 寒芒克洛丝所有卡牌的公共抽象基类。
 *
 * 提供:
 *   - 特殊牌类型 tag: 箭矢牌 / 梦击牌 / A1小队牌
 *   - 寒芒消耗 hook: consumeFrost(amount)
 *   - 升级前后描述切换辅助
 *   - 模板方法: use() 不可重写, 子类实现 useImpl(), 父类负责箭矢/通用 hook
 *   - 箭矢牌伤害修饰 (箭矢改良 power, applyPowers 中处理)
 *   - 散射工具 (scatterIfArrow), 由箭矢牌在 useImpl 中调用
 *
 * 子类约定:
 *   - 静态字段写入 ID/NAME/DESCRIPTION/UPGRADE_DESCRIPTION
 *   - 构造函数标记对应 tag (isArrow / isDreamStrike / isA1Squad)
 *   - 覆写 useImpl(p, m) 实现自身效果
 */
public abstract class AbstractKroosCard extends CustomCard {

    // ===== 特殊牌类型标记 (在子类构造函数中设置) =====
    /** 箭矢牌: 打出后获得"消耗能量"数量的专注层数, 与多个 power 联动 */
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

    // ===================================================================
    // 模板方法: use 不可重写, 子类实现 useImpl
    // ===================================================================

    /** 子类实现自身效果。注意: 不要再覆写 use()。 */
    public abstract void useImpl(AbstractPlayer p, AbstractMonster m);

    @Override
    public final void use(AbstractPlayer p, AbstractMonster m) {
        useImpl(p, m);
        afterUseHook(p, m);
    }

    /** 通用后置 hook: 当前仅处理箭矢自动获取专注; 其他全局 hook 可在此扩展。 */
    private void afterUseHook(AbstractPlayer p, AbstractMonster m) {
        if (isArrow) {
            int focus = Math.max(0, this.energyOnUse);
            if (focus > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        p, p, new FocusPower(p, focus), focus));
            }
        }
    }

    // ===================================================================
    // 伤害修饰: 箭矢改良在卡牌 applyPowers 阶段加成
    // ===================================================================

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (!isArrow || this.baseDamage < 0) return;
        AbstractPower aip = AbstractDungeon.player != null
                ? AbstractDungeon.player.getPower(ArrowImprovementPower.POWER_ID)
                : null;
        if (aip != null && aip.amount > 0) {
            this.damage += aip.amount;
            this.isDamageModified = this.damage != this.baseDamage;
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if (!isArrow || this.baseDamage < 0) return;
        AbstractPower aip = AbstractDungeon.player != null
                ? AbstractDungeon.player.getPower(ArrowImprovementPower.POWER_ID)
                : null;
        if (aip != null && aip.amount > 0) {
            this.damage += aip.amount;
            this.isDamageModified = this.damage != this.baseDamage;
        }
    }

    // ===================================================================
    // 散射: 箭矢牌可调用; 对非主目标敌人造成 50% 主目标伤害
    // ===================================================================

    /**
     * 若玩家具有[散射]且本牌为箭矢, 对场上其他存活敌人造成 floor(damage * 0.5) 点伤害。
     * 主目标 m 的伤害由各卡自行处理。本方法不修改主目标。
     */
    protected void scatterIfArrow(AbstractPlayer p, AbstractMonster main, int damage) {
        if (!isArrow || main == null) return;
        AbstractPower scatter = p.getPower(ScatterPower.POWER_ID);
        if (scatter == null) return;
        int splash = damage / 2;
        if (splash <= 0) return;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo == main || mo == null || mo.isDeadOrEscaped()) continue;
            AbstractDungeon.actionManager.addToBottom(new DamageAction(
                    mo, new DamageInfo(p, splash, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }

    // ===================================================================
    // 寒芒消耗
    // ===================================================================

    /**
     * 同步检查玩家身上的[寒芒]层数是否足以支付 amount。
     * 不修改层数。各张卡牌应先用此方法走分支, 再调用 consumeFrost 入队。
     */
    public static boolean canConsumeFrost(int amount) {
        AbstractPower p = currentFrost();
        return p != null && p.amount >= amount;
    }

    /**
     * 消耗 amount 层寒芒(进入 action 队列)。返回实际尝试消耗的层数。
     * 注: 心之痕的减免逻辑应该在 HeartScar power 内自行 hook ReducePowerAction, 不耦合本方法。
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

    /** 升级时切换到升级描述。子类应在 upgrade() 中调用。 */
    protected void upgradeDescription() {
        CardStrings s = CardCrawlGame.languagePack.getCardStrings(this.cardID);
        this.rawDescription = s.UPGRADE_DESCRIPTION;
        this.initializeDescription();
    }
}
