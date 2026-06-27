package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 浅眠 Power (不可堆叠, 仅作为状态标记)。
 *
 * 含义:
 *   - 非[梦击]牌伤害 ×80% (由 AbstractKroosCard.applyPowers 处理, 卡侧)
 *   - 回合结束: 获得 6 点格挡, 恢复 2 点生命
 *   - 进入浅眠时获得 1 层蓄势
 *   - 回合开始时获得 1 层蓄势
 *   - 退出浅眠时通过 RemoveSpecificPowerAction(ChargePower) 触发蓄势放电
 *
 * 与其他 power 的边界:
 *   - 蓄势放电由 ChargePower.onRemove 自管, 本 power 不触碰其内部
 *   - 梦影/幻影伪装通过监听本 power 的 onApplyPower / 检查 owner.hasPower 实现, 本 power 不主动通知
 */
public class DozePower extends AbstractPower {

    public static final String POWER_ID = KroosMod.MOD_ID + ":Doze";
    private static final PowerStrings STRINGS =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = STRINGS.NAME;
    public static final String[] DESCRIPTIONS = STRINGS.DESCRIPTIONS;

    public static final int END_OF_TURN_BLOCK = 6;
    public static final int END_OF_TURN_HEAL  = 2;
    public static final int ON_GAIN_CHARGE    = 1;
    public static final int ON_TURN_CHARGE    = 1;
    public static final float NON_DREAM_STRIKE_MULTIPLIER = 0.8F;

    private static final String ICON_LARGE = KroosMod.RES_ROOT + "powers/doze_large.png";
    private static final String ICON_SMALL = KroosMod.RES_ROOT + "powers/doze_small.png";

    public DozePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1; // 不可堆叠
        this.type = PowerType.BUFF;
        loadIcons();
        updateDescription();
    }

    private void loadIcons() {
        Texture large = TextureLoader.getTexture(ICON_LARGE);
        Texture small = TextureLoader.getTexture(ICON_SMALL);
        if (large != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(large, 0, 0, 128, 128);
        if (small != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(small, 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    /**
     * 检测自身被施加。利用 AbstractPower.onApplyPower 在 owner.powers 全员上的广播,
     * 当 power == this 时即表示"我刚被加进来"。
     */
    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power == this && target == this.owner) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    owner, owner, new ChargePower(owner, ON_GAIN_CHARGE), ON_GAIN_CHARGE));
        }
    }

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                owner, owner, new ChargePower(owner, ON_TURN_CHARGE), ON_TURN_CHARGE));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) return;
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(owner, owner, END_OF_TURN_BLOCK));
        AbstractDungeon.actionManager.addToBottom(new HealAction(owner, owner, END_OF_TURN_HEAL));
    }

    /**
     * 浅眠移除时:
     *   1) 通过 RemoveSpecificPowerAction 触发蓄势放电(由 ChargePower.onRemove 自管)
     *   2) 广播 onDozeExited 给所有实现 IDozeExitListener 的同主 power (警惕等)
     */
    @Override
    public void onRemove() {
        if (owner == null) return;
        if (owner.hasPower(ChargePower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
                    owner, owner, ChargePower.POWER_ID));
        }
        for (AbstractPower p : owner.powers) {
            if (p instanceof IDozeExitListener) {
                ((IDozeExitListener) p).onDozeExited();
            }
        }
    }
}
