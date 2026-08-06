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
import sts.kroos.KroosMod;
import sts.kroos.patches.KroosEnum;
import sts.kroos.powers.A1SquadBondPower;
import sts.kroos.powers.ArrowImprovementPower;
import sts.kroos.powers.DozePower;
import sts.kroos.powers.FocusPower;
import sts.kroos.powers.FrostPower;
import sts.kroos.powers.HeartScarPower;
import sts.kroos.powers.ScatterPower;
import sts.kroos.powers.TransparentPower;

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

    // ===================================================================
    // 悬停 tooltip 强绑定: 描述提到组内任一关键字时, 自动补充同组其他关键字的 tooltip
    //   只在 keywords 列表中补充, 不修改卡面显示文本
    // ===================================================================

    /** 强绑定关键字组: 专注↔暴击, 破绽↔中的, 浅眠↔蓄势, 箭矢→专注, 梦击→浅眠 */
    private static final String[][] LINKED_KEYWORD_GROUPS = {
            {KroosMod.MOD_ID + ":专注", KroosMod.MOD_ID + ":暴击"},
            {KroosMod.MOD_ID + ":破绽", KroosMod.MOD_ID + ":中的"},
            {KroosMod.MOD_ID + ":浅眠", KroosMod.MOD_ID + ":蓄势"},
            {KroosMod.MOD_ID + ":箭矢", KroosMod.MOD_ID + ":专注"},
            {KroosMod.MOD_ID + ":梦击", KroosMod.MOD_ID + ":浅眠"},
    };

    @Override
    public void initializeDescription() {
        super.initializeDescription();
        addLinkedKeywordTips();
    }

    private void addLinkedKeywordTips() {
        if (this.keywords == null) return;
        for (String[] group : LINKED_KEYWORD_GROUPS) {
            boolean hasAny = false;
            for (String kw : group) {
                if (this.keywords.contains(kw)) {
                    hasAny = true;
                    break;
                }
            }
            if (hasAny) {
                for (String kw : group) {
                    if (!this.keywords.contains(kw)) {
                        this.keywords.add(kw);
                    }
                }
            }
        }
    }

    /** 通用后置 hook: 当前仅处理箭矢自动获取专注; 其他全局 hook 可在此扩展。
     *  专注层数 = 本回合实际消耗的能量 (costForTurn)。
     *  若牌被免费打出 (freeToPlay() 返回 true), 则消耗能量视为 0, 不获得专注。*/
    private void afterUseHook(AbstractPlayer p, AbstractMonster m) {
        if (isArrow) {
            int focus;
            if (this.freeToPlay()) {
                focus = 0;
            } else {
                focus = Math.max(0, this.costForTurn);
            }
            if (focus > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                        p, p, new FocusPower(p, focus), focus));
            }
        }
    }

    // ===================================================================
    // 伤害修饰: 箭矢改良(+X) 与 浅眠减伤(*0.8) 由卡侧处理
    //   原因: 两者都依赖"是否箭矢/梦击"标签, power 端拿不到当前卡的可靠引用,
    //         因此把基于卡标签的修饰统一放在卡的 applyPowers/calculateCardDamage。
    // ===================================================================

    @Override
    public void applyPowers() {
        super.applyPowers();
        applyKroosDamageModifiers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        applyKroosDamageModifiers();
    }

    private void applyKroosDamageModifiers() {
        if (this.baseDamage < 0) return;
        if (AbstractDungeon.player == null) return;

        // 1) 箭矢改良: 仅对箭矢牌生效, 加性修饰
        if (isArrow) {
            AbstractPower aip = AbstractDungeon.player.getPower(ArrowImprovementPower.POWER_ID);
            if (aip != null && aip.amount > 0) {
                this.damage += aip.amount;
            }
        }
        // 2) 浅眠减伤: 攻击牌且非梦击, 乘性修饰(向下取整)
        if (this.type == CardType.ATTACK && !isDreamStrike
                && AbstractDungeon.player.hasPower(DozePower.POWER_ID)) {
            this.damage = (int) Math.floor(this.damage * DozePower.NON_DREAM_STRIKE_MULTIPLIER);
        }
        this.isDamageModified = this.damage != this.baseDamage;
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
     * 消耗 amount 层寒芒(进入 action 队列)。返回 amount(逻辑消耗量, 即触发用)。
     *
     * 内部流程:
     *   1) [心之痕] 询问 tryDiscount(amount), 决定本次实际扣的层数 (可能为 0)
     *   2) 若 actual > 0, 入队 ReducePowerAction(actual)
     *   3) [通明] 通知本次触发以"逻辑消耗量"累计计数 — 心之痕的减免不影响通明计数
     */
    public int consumeFrost(int amount) {
        if (!canConsumeFrost(amount)) return 0;
        AbstractPower fp = currentFrost();
        int actual = amount;

        // 心之痕减免
        AbstractPower hs = AbstractDungeon.player.getPower(HeartScarPower.POWER_ID);
        if (hs instanceof HeartScarPower) {
            actual = ((HeartScarPower) hs).tryDiscount(amount);
        }

        if (actual > 0) {
            addToBot(new ReducePowerAction(fp.owner, fp.owner, fp.ID, actual));
        }

        // 通明累计 (按逻辑消耗)
        AbstractPower tp = AbstractDungeon.player.getPower(TransparentPower.POWER_ID);
        if (tp instanceof TransparentPower) {
            ((TransparentPower) tp).notifyConsumed(amount);
        }

        // A1 小队的羁绊累计 (按逻辑消耗)
        AbstractPower bond = AbstractDungeon.player.getPower(A1SquadBondPower.POWER_ID);
        if (bond instanceof A1SquadBondPower) {
            ((A1SquadBondPower) bond).notifyConsumed(amount);
        }

        // 遗物监听 (成长的证明 / 其他响应寒芒消耗的遗物)
        for (com.megacrit.cardcrawl.relics.AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof sts.kroos.relics.IFrostConsumeListener) {
                ((sts.kroos.relics.IFrostConsumeListener) r).onFrostConsumed(amount);
            }
        }

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
