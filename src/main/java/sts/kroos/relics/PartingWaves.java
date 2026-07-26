package sts.kroos.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 分浪 — 稀有。
 * 拾起时升级所有攻击牌 (主卡组内所有 ATTACK 型卡牌)。
 */
public class PartingWaves extends CustomRelic {
    public static final String ID = KroosMod.MOD_ID + ":PartingWaves";
    private static final String IMG = KroosMod.RES_ROOT + "relics/parting_waves.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/parting_waves_outline.png";

    public PartingWaves() {
        super(ID, TextureLoader.getTexture(IMG), RelicTier.RARE, LandingSound.FLAT);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public void onEquip() {
        if (AbstractDungeon.player == null) return;
        List<AbstractCard> upgraded = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.type == AbstractCard.CardType.ATTACK && c.canUpgrade()) {
                c.upgrade();
                upgraded.add(c);
            }
        }
        for (AbstractCard c : upgraded) {
            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
        }
    }

    @Override
    public AbstractRelic makeCopy() { return new PartingWaves(); }
}
