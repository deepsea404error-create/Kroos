package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 专注 Power。
 *
 * 含义: 当专注 >= 3 层时, 消耗 3 层专注, 获得 1 层暴击。
 * 转换发生在两个时机:
 *   - 任何来源叠加专注后 (stackPower)
 *   - 玩家回合开始时 (atStartOfTurn) — 处理战斗开始遗物施加等场景
 *
 * 设计文档规则4: 专注 power 只对外施加 CriticalPower, 不关心暴击内部如何实现。
 */
public class FocusPower extends AbstractPower {

    public static final String POWER_ID = KroosMod.MOD_ID + ":Focus";
    private static final PowerStrings STRINGS =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = STRINGS.NAME;
    public static final String[] DESCRIPTIONS = STRINGS.DESCRIPTIONS;

    private static final int CONVERT_COST = 3;
    private static final int CONVERT_GAIN = 1;

    private static final String ICON_LARGE = KroosMod.RES_ROOT + "powers/focus_large.png";
    private static final String ICON_SMALL = KroosMod.RES_ROOT + "powers/focus_small.png";

    public FocusPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
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
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        tryConvert();
    }

    @Override
    public void atStartOfTurn() {
        tryConvert();
    }

    /** 触发条件: 当前层数 >= 3, 一次结算尽可能多的转换 */
    private void tryConvert() {
        if (this.amount < CONVERT_COST) return;
        int conversions = this.amount / CONVERT_COST;
        int costTotal   = conversions * CONVERT_COST;
        int gainTotal   = conversions * CONVERT_GAIN;

        // 消耗专注 + 施加暴击。仅作用相关 power, 不耦合内部逻辑。
        AbstractDungeon.actionManager.addToBottom(
                new ReducePowerAction(owner, owner, POWER_ID, costTotal));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner,
                        new CriticalPower(owner, gainTotal), gainTotal));
    }
}
