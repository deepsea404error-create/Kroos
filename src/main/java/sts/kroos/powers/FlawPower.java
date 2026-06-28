package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 破绽 Power (敌方负面)。
 *
 * 含义: 当破绽 >= 5 层时, 消耗 5 层, 施加 1 层中的。
 * 通常情况下数值在 1-4 之间, 表示尚未形成中的;
 * 一旦累积到 5 立即转换为 1 层 [中的]。
 *
 * 触发时机:
 *   - stackPower (任何来源叠加破绽)
 *   - atStartOfTurn (敌方回合开始, 兜底)
 */
public class FlawPower extends AbstractPower {

    public static final String POWER_ID = KroosMod.MOD_ID + ":Flaw";
    private static final PowerStrings STRINGS =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = STRINGS.NAME;
    public static final String[] DESCRIPTIONS = STRINGS.DESCRIPTIONS;

    private static final int CONVERT_COST = 5;
    private static final int CONVERT_GAIN = 1;

    private static final String ICON_LARGE = KroosMod.RES_ROOT + "powers/flaw_large.png";
    private static final String ICON_SMALL = KroosMod.RES_ROOT + "powers/flaw_small.png";

    public FlawPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        loadIcons();
        updateDescription();
    }

    private void loadIcons() {
        Texture large = TextureLoader.getTexture(ICON_LARGE);
        Texture small = TextureLoader.getTexture(ICON_SMALL);
        if (large != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(large, 0, 0, 84, 84);
        if (small != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(small, 0, 0, 32, 32);
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

    private void tryConvert() {
        if (this.amount < CONVERT_COST) return;
        int conversions = this.amount / CONVERT_COST;
        int costTotal   = conversions * CONVERT_COST;
        int gainTotal   = conversions * CONVERT_GAIN;

        AbstractDungeon.actionManager.addToBottom(
                new ReducePowerAction(owner, owner, POWER_ID, costTotal));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner,
                        new HitPower(owner, gainTotal), gainTotal));
    }
}
