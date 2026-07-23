package sts.kroos.characters;

import basemod.abstracts.CustomEnergyOrb;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import sts.kroos.KroosMod;
import sts.kroos.cards.attack.DoubleShot;
import sts.kroos.cards.attack.Strike;
import sts.kroos.cards.skill.Defend;
import sts.kroos.cards.skill.PreparedShot;
import sts.kroos.character.KroosSpineHelper;
import sts.kroos.patches.KroosEnum;
import sts.kroos.relics.KroosBadge;

import java.util.ArrayList;

public class Kroos extends CustomPlayer {

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 72;
    public static final int MAX_HP = 72;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    public static final String CHAR_ID = "KROOS";
    private static final CharacterStrings CHAR_STRINGS =
            CardCrawlGame.languagePack.getCharacterString(CHAR_ID);
    public static final String NAME = CHAR_STRINGS.NAMES[0];
    public static final String DESCRIPTION = CHAR_STRINGS.TEXT[0];

    // Spine animation paths
    private static final String SPINE_ATLAS = KroosMod.RES_ROOT + "char/kroos/char_1021_kroos2.atlas";
    private static final String SPINE_JSON = KroosMod.RES_ROOT + "char/kroos/char_1021_kroos2.json";
    private static final float SPINE_SCALE = 2.0f;

    // Selection screen resources
    private static final String SHOULDER_1 = KroosMod.RES_ROOT + "char/shoulder.png";
    private static final String SHOULDER_2 = KroosMod.RES_ROOT + "char/shoulder2.png";

    // Energy orb config
    private static final String[] ORB_TEX = {
            KroosMod.RES_ROOT + "ui/energy/layer1.png",
            KroosMod.RES_ROOT + "ui/energy/layer2.png",
            KroosMod.RES_ROOT + "ui/energy/layer3.png",
            KroosMod.RES_ROOT + "ui/energy/layer4.png",
            KroosMod.RES_ROOT + "ui/energy/layer5.png",
            KroosMod.RES_ROOT + "ui/energy/layer0.png",
            KroosMod.RES_ROOT + "ui/energy/layer1d.png",
            KroosMod.RES_ROOT + "ui/energy/layer2d.png",
            KroosMod.RES_ROOT + "ui/energy/layer3d.png",
            KroosMod.RES_ROOT + "ui/energy/layer4d.png",
            KroosMod.RES_ROOT + "ui/energy/layer5d.png"
    };
    private static final String ORB_VFX = KroosMod.RES_ROOT + "ui/energy/vfx.png";
    private static final float[] LAYER_SPEED = {
            -20.0F, 20.0F, -40.0F, 40.0F, 360.0F,
            -10.0F, 8.0F, -5.0F, 5.0F, 0.0F
    };

    // Colors
    private final Color cardRenderColor = new Color(0.69F, 0.77F, 0.87F, 1.0F);
    private final Color cardTrailColor = new Color(0.69F, 0.77F, 0.87F, 1.0F);
    private final Color slashAttackColor = new Color(0.69F, 0.77F, 0.87F, 1.0F);

    // Spine helper
    private KroosSpineHelper spineHelper;

    public Kroos(String name) {
        super(name, KroosEnum.KROOS,
                new CustomEnergyOrb(ORB_TEX, ORB_VFX, LAYER_SPEED),
                null, null);

        initializeClass(null, SHOULDER_2, SHOULDER_1, null,
                getLoadout(),
                20.0F, -20.0F, 200.0F, 250.0F,
                new EnergyManager(ENERGY_PER_TURN));

        this.dialogX = this.drawX + 0.0F * Settings.scale;
        this.dialogY = this.drawY + 220.0F * Settings.scale;

        // Load Spine animation
        spineHelper = new KroosSpineHelper(SPINE_ATLAS, SPINE_JSON, SPINE_SCALE);
        spineHelper.playIdle();
    }

    @Override
    public void render(SpriteBatch sb) {
        this.stance.render(sb);

        if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                || AbstractDungeon.getCurrRoom() instanceof MonsterRoom)
                && !this.isDead) {
            renderHealth(sb);

            if (!this.orbs.isEmpty()) {
                for (AbstractOrb o : this.orbs) {
                    o.render(sb);
                }
            }
        }

        if (!(AbstractDungeon.getCurrRoom() instanceof RestRoom)) {
            renderPlayerImage(sb);
            this.hb.render(sb);
            this.healthHb.render(sb);
        } else {
            sb.setColor(Color.WHITE);
            renderShoulderImg(sb);
        }
    }

    @Override
    public void renderPlayerImage(SpriteBatch sb) {
        if (spineHelper == null) return;

        spineHelper.update();
        spineHelper.applyAnimation();

        spineHelper.setPosition(
                this.drawX + this.animX,
                this.drawY + this.animY
        );

        spineHelper.setColor(this.tint.color);
        spineHelper.setFlip(this.flipHorizontal, this.flipVertical);

        spineHelper.render(sb);
    }

    @Override
    public void playDeathAnimation() {
        if (spineHelper != null) {
            spineHelper.playDeath();
        }
    }

    @Override
    public void useFastAttackAnimation() {
        if (spineHelper != null) {
            spineHelper.playAttack();
        }
    }

    @Override
    public void useSlowAttackAnimation() {
        if (spineHelper != null) {
            spineHelper.playAttack();
        }
    }

    @Override
    public void useStaggerAnimation() {
        if (spineHelper != null) {
            spineHelper.playIdle();
        }
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> deck = new ArrayList<>();
        deck.add(Strike.ID);
        deck.add(Strike.ID);
        deck.add(Strike.ID);
        deck.add(Strike.ID);
        deck.add(Defend.ID);
        deck.add(Defend.ID);
        deck.add(Defend.ID);
        deck.add(Defend.ID);
        deck.add(DoubleShot.ID);
        deck.add(PreparedShot.ID);
        return deck;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> relics = new ArrayList<>();
        relics.add(KroosBadge.ID);
        UnlockTracker.markRelicAsSeen(KroosBadge.ID);
        return relics;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                NAME, DESCRIPTION,
                STARTING_HP, MAX_HP, ORB_SLOTS,
                STARTING_GOLD, CARD_DRAW, this,
                getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAME;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return KroosEnum.KROOS_COLOR;
    }

    @Override
    public Color getCardRenderColor() {
        return this.cardRenderColor;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new DoubleShot();
    }

    @Override
    public Color getCardTrailColor() {
        return this.cardTrailColor;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return com.megacrit.cardcrawl.helpers.FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAME;
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Kroos(name);
    }

    @Override
    public String getSpireHeartText() {
        return "...";
    }

    @Override
    public Color getSlashAttackColor() {
        return this.slashAttackColor;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        };
    }

    @Override
    public String getVampireText() {
        return com.megacrit.cardcrawl.events.city.Vampires.DESCRIPTIONS[1];
    }
}
