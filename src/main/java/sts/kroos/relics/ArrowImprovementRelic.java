package sts.kroos.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sts.kroos.KroosMod;
import sts.kroos.powers.ArrowImprovementPower;
import sts.kroos.util.TextureLoader;

/**
 * 箭矢改良 — 稀有 (遗物, 与同名 power 区分)。
 * 箭矢牌伤害 +2。
 *
 * 实现: 战斗开始时施加 ArrowImprovementPower(2), 沿用已有箭矢加成机制。
 *      多份效果(同名 power)在 stack 时自然相加, 无需特殊处理。
 */
public class ArrowImprovementRelic extends CustomRelic {
    public static final String ID = KroosMod.MOD_ID + ":ArrowImprovementRelic";
    private static final String IMG = KroosMod.RES_ROOT + "relics/arrow_improvement.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/arrow_improvement_outline.png";

    public static final int BONUS = 2;

    public ArrowImprovementRelic() {
        super(ID, (Texture) null, RelicTier.RARE, LandingSound.FLAT);
        this.img = TextureLoader.getTexture(IMG);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player,
                new ArrowImprovementPower(AbstractDungeon.player, BONUS), BONUS));
    }

    @Override
    public AbstractRelic makeCopy() { return new ArrowImprovementRelic(); }
}
