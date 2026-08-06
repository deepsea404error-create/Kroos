package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
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
 * 触发: 由 DozePower.onRemove() 调用 discharge() 方法直接触发放电效果。
 *       所有效果均使用 addToTop, 退出浅眠后立即执行 (本回合可用)。
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
        if (large != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(large, 0, 0, 84, 84);
        if (small != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(small, 0, 0, 36, 36);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount
                + DESCRIPTIONS[1] + this.amount
                + DESCRIPTIONS[2] + (this.amount * 2)
                + DESCRIPTIONS[3];
    }

    /**
     * 蓄势放电: 抽 N 牌 + 获得 N 能量 (本回合) + 临时 +2N 力量。
     *
     * 由 DozePower.onRemove() 在退出浅眠时直接调用。
     * 使用 addToTop 保证所有效果紧跟当前行动之后立即执行 (本回合可用)。
     * 注意: addToTop 需反序添加, 后添加的先执行:
     *   添加顺序: LoseStrength → Strength → GainEnergy → DrawCard
     *   执行顺序: DrawCard → GainEnergy → Strength → LoseStrength
     */
    public void discharge() {
        int n = this.amount;
        if (n <= 0 || owner == null) return;
        this.flash();
        // addToTop 反序添加, 保证执行顺序为: DrawCard → GainEnergy → Strength → LoseStrength
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
                owner, owner, new LoseStrengthPower(owner, n * 2), n * 2));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
                owner, owner, new StrengthPower(owner, n * 2), n * 2));
        AbstractDungeon.actionManager.addToTop(new GainEnergyAction(n));
        AbstractDungeon.actionManager.addToTop(new DrawCardAction(owner, n));
    }
}
