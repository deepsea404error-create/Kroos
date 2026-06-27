package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 寒芒余韵 Power。
 *
 * 含义: 下回合获得 X 层寒芒。
 *
 * 触发: 下一玩家回合开始抽牌后 (atStartOfTurnPostDraw) — 此时不会与本回合刚施加冲突,
 * 因为本 power 是在本回合内施加, 而 atStartOfTurnPostDraw 仅在回合切换后才触发。
 */
public class FrostEchoPower extends AbstractPower {

    public static final String POWER_ID = KroosMod.MOD_ID + ":FrostEcho";
    private static final PowerStrings STRINGS =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = STRINGS.NAME;
    public static final String[] DESCRIPTIONS = STRINGS.DESCRIPTIONS;

    private static final String ICON_LARGE = KroosMod.RES_ROOT + "powers/frost_echo_large.png";
    private static final String ICON_SMALL = KroosMod.RES_ROOT + "powers/frost_echo_small.png";

    public FrostEchoPower(AbstractCreature owner, int amount) {
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
    public void atStartOfTurnPostDraw() {
        if (owner == null || this.amount <= 0) return;
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                owner, owner, new FrostPower(owner, this.amount), this.amount));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
                owner, owner, POWER_ID));
    }
}
