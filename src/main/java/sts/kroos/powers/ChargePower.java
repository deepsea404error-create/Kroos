package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 蓄势 Power。
 *
 * 含义: 退出浅眠时消耗本 power, 抽 N 张牌, 获得 N 点能量, 获得 N×2 层临时力量。
 *       (临时力量 = StrengthPower(2N) + LoseStrengthPower(2N), 回合结束消除)
 *
 * 触发: onRemove() — DozePower 退出时, 通过 RemoveSpecificPowerAction 移除本 power
 *       从而触发 onRemove, 在此结算放电效果。
 *
 * 设计文档规则4: DozePower 不直接计算蓄势效果, 只负责 Remove 本 power, 放电由本 power 自管。
 */
public class ChargePower extends AbstractPower {

    public static final String POWER_ID = KroosMod.MOD_ID + ":Charge";
    private static final PowerStrings STRINGS =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = STRINGS.NAME;
    public static final String[] DESCRIPTIONS = STRINGS.DESCRIPTIONS;

    private static final String ICON_LARGE = KroosMod.RES_ROOT + "powers/charge_large.png";
    private static final String ICON_SMALL = KroosMod.RES_ROOT + "powers/charge_small.png";

    public ChargePower(AbstractCreature owner, int amount) {
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
        this.description = DESCRIPTIONS[0] + this.amount
                + DESCRIPTIONS[1] + this.amount
                + DESCRIPTIONS[2] + (this.amount * 2)
                + DESCRIPTIONS[3];
    }

    /**
     * 退出浅眠 (RemoveSpecificPowerAction) 触发本 power 移除时:
     * 抽 N 牌 + 获得 N 能量 + 临时 +2N 力量。
     */
    @Override
    public void onRemove() {
        int n = this.amount;
        if (n <= 0 || owner == null) return;
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(owner, n));
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(n));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                owner, owner, new StrengthPower(owner, n * 2), n * 2));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                owner, owner, new LoseStrengthPower(owner, n * 2), n * 2));
    }
}
