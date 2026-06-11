package sts.kroos.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 基岩 — 稀有。
 * 拾起时升级所有技能牌 (主卡组内所有 SKILL 型卡牌)。
 */
public class Bedrock extends CustomRelic {
    public static final String ID = KroosMod.MOD_ID + ":Bedrock";
    private static final String IMG = KroosMod.RES_ROOT + "relics/bedrock.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/bedrock_outline.png";

    public Bedrock() {
        super(ID, (Texture) null, RelicTier.RARE, LandingSound.FLAT);
        this.img = TextureLoader.getTexture(IMG);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public void onEquip() {
        if (AbstractDungeon.player == null) return;
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.type == AbstractCard.CardType.SKILL && c.canUpgrade()) {
                c.upgrade();
            }
        }
    }

    @Override
    public AbstractRelic makeCopy() { return new Bedrock(); }
}
