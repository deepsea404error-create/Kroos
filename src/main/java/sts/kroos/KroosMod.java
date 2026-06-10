package sts.kroos;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.google.gson.Gson;
import sts.kroos.cards.attack.Strike;
import sts.kroos.cards.attack.DoubleShot;
import sts.kroos.cards.skill.Defend;
import sts.kroos.cards.skill.PreparedShot;
import sts.kroos.characters.Kroos;
import sts.kroos.patches.KroosEnum;
import sts.kroos.relics.KroosBadge;

/**
 * 寒芒克洛丝 mod 主入口。
 * 负责注册颜色、角色、卡牌、遗物以及本地化资源。
 */
@SpireInitializer
public class KroosMod implements
        EditCharactersSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber {

    public static final String MOD_ID = "kroosmod";

    // === 配色 (寒芒：偏冷的银白色) ===
    public static final java.awt.Color KROOS_COLOR_AWT = new java.awt.Color(176, 196, 222, 255);

    // === 资源路径 (统一前缀, 不使用占位符) ===
    public static final String RES_ROOT = "kroosmod/images/";
    // 卡牌底纹
    public static final String ATTACK_BG       = RES_ROOT + "cardback/bg_attack_kroos.png";
    public static final String SKILL_BG        = RES_ROOT + "cardback/bg_skill_kroos.png";
    public static final String POWER_BG        = RES_ROOT + "cardback/bg_power_kroos.png";
    public static final String ATTACK_BG_P     = RES_ROOT + "cardback/bg_attack_kroos_p.png";
    public static final String SKILL_BG_P      = RES_ROOT + "cardback/bg_skill_kroos_p.png";
    public static final String POWER_BG_P      = RES_ROOT + "cardback/bg_power_kroos_p.png";
    // 能量球
    public static final String ENERGY_ORB      = RES_ROOT + "ui/energy_orb_kroos.png";
    public static final String ENERGY_ORB_P    = RES_ROOT + "ui/energy_orb_kroos_p.png";
    public static final String CARD_ENERGY_ORB = RES_ROOT + "ui/card_energy_orb_kroos.png";
    public static final String CHAR_BTN        = RES_ROOT + "char/charSelect_button.png";
    public static final String CHAR_BG         = RES_ROOT + "char/charSelect_bg.png";

    public KroosMod() {
        BaseMod.subscribe(this);
        BaseMod.addColor(
                KroosEnum.KROOS_COLOR,
                KROOS_COLOR_AWT,
                ATTACK_BG, SKILL_BG, POWER_BG,
                ENERGY_ORB,
                ATTACK_BG_P, SKILL_BG_P, POWER_BG_P,
                ENERGY_ORB_P,
                CARD_ENERGY_ORB
        );
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        new KroosMod();
    }

    // === 角色注册 ===
    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(
                new Kroos(CardCrawlGame.playerName),
                CHAR_BTN,
                CHAR_BG,
                KroosEnum.KROOS
        );
    }

    // === 卡牌注册 ===
    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new Defend());
        BaseMod.addCard(new DoubleShot());
        BaseMod.addCard(new PreparedShot());

        UnlockTracker.unlockCard(Strike.ID);
        UnlockTracker.unlockCard(Defend.ID);
        UnlockTracker.unlockCard(DoubleShot.ID);
        UnlockTracker.unlockCard(PreparedShot.ID);
    }

    // === 遗物注册 ===
    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new KroosBadge(), KroosEnum.KROOS_COLOR);
        UnlockTracker.markRelicAsSeen(KroosBadge.ID);
    }

    // === 本地化资源 ===
    @Override
    public void receiveEditStrings() {
        String lang = "zh_CN";
        String dir = "kroosmod/localization/" + lang + "/";
        BaseMod.loadCustomStringsFile(CardStrings.class,      dir + "Cards-strings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class,     dir + "Relics-strings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class,     dir + "Powers-strings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, dir + "Character-strings.json");
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal("kroosmod/localization/zh_CN/Keywords-strings.json").readString("UTF-8");
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword k : keywords) {
                BaseMod.addKeyword(MOD_ID, k.PROPER_NAME, k.NAMES, k.DESCRIPTION);
            }
        }
    }

    @Override
    public void receivePostInitialize() {
        // 预留：badge / mod 设置面板
    }

}
