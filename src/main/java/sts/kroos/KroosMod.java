package sts.kroos;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.OnStartBattleSubscriber;
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
import sts.kroos.cards.attack.Awareness;
import sts.kroos.cards.attack.BayonetCharge;
import sts.kroos.cards.attack.DoubleShot;
import sts.kroos.cards.attack.DreamDisturb;

import sts.kroos.cards.attack.ExplosiveArrow;
import sts.kroos.cards.attack.FinalCurtain;
import sts.kroos.cards.attack.FlawShot;
import sts.kroos.cards.attack.FlowingCloudArrow;
import sts.kroos.cards.attack.FrostPierce;
import sts.kroos.cards.attack.GlitteringEdge;
import sts.kroos.cards.attack.HalfAwake;
import sts.kroos.cards.attack.Hunt;
import sts.kroos.cards.attack.PiercingCloud;
import sts.kroos.cards.attack.PrecisionShot;
import sts.kroos.cards.attack.RainbowArrow;
import sts.kroos.cards.attack.RapidFire;
import sts.kroos.cards.attack.ReinforcedArrow;
import sts.kroos.cards.attack.RiftStrike;
import sts.kroos.cards.attack.Rouse;
import sts.kroos.cards.attack.SkyVolley;
import sts.kroos.cards.attack.SlowBurn;
import sts.kroos.cards.attack.Strike;
import sts.kroos.cards.attack.ThroatSeal;
import sts.kroos.cards.attack.Volley;
import sts.kroos.cards.attack.WarningShot;
import sts.kroos.cards.attack.Yawn;
import sts.kroos.cards.colorless.A1_Fen;
import sts.kroos.cards.colorless.A1_Furong;
import sts.kroos.cards.colorless.A1_Migelu;
import sts.kroos.cards.colorless.A1_Yanrong;
import sts.kroos.cards.colorless.NormalArrow;
import sts.kroos.cards.power.A1SquadBond;
import sts.kroos.cards.power.ArrowImprovement;
import sts.kroos.cards.power.Burst;
import sts.kroos.cards.power.DreamShadow;
import sts.kroos.cards.power.HellForge;
import sts.kroos.cards.power.FrostForm;
import sts.kroos.cards.power.FlawConfirm;
import sts.kroos.cards.power.HeartScar;
import sts.kroos.cards.power.Heartbeat;
import sts.kroos.cards.power.Insomnia;
import sts.kroos.cards.power.PhantomCamo;
import sts.kroos.cards.power.Resonance;
import sts.kroos.cards.power.Scatter;
import sts.kroos.cards.power.SkyfireFlame;
import sts.kroos.cards.power.SupplyDrop;
import sts.kroos.cards.power.Transparent;
import sts.kroos.cards.power.Unlit;
import sts.kroos.cards.power.Vigilance;
import sts.kroos.cards.skill.AimForVital;
import sts.kroos.cards.skill.Brace;
import sts.kroos.cards.skill.Collapse;
import sts.kroos.cards.skill.CrossbowModify;
import sts.kroos.cards.skill.Defend;
import sts.kroos.cards.skill.EmergencyEvasion;
import sts.kroos.cards.skill.EmergencyReload;
import sts.kroos.cards.skill.EquipmentUpgrade;
import sts.kroos.cards.skill.FrostStar;
import sts.kroos.cards.skill.GoodDream;
import sts.kroos.cards.skill.Hide;
import sts.kroos.cards.skill.HoldMode;
import sts.kroos.cards.skill.HundredForge;
import sts.kroos.cards.skill.MechanicalSight;
import sts.kroos.cards.skill.MemoryWound;
import sts.kroos.cards.skill.OneInstant;
import sts.kroos.cards.skill.OpenEyes;
import sts.kroos.cards.skill.PreparedShot;
import sts.kroos.cards.skill.RapidShift;
import sts.kroos.cards.skill.RapidShoot;
import sts.kroos.cards.skill.Rest;
import sts.kroos.cards.skill.Scout;
import sts.kroos.cards.skill.SimpleFortification;
import sts.kroos.cards.skill.Solitude;
import sts.kroos.cards.skill.SupportOrder;
import sts.kroos.cards.skill.TacticalShift;
import sts.kroos.cards.skill.Traceless;
import sts.kroos.cards.skill.TruthAndDare;
import sts.kroos.cards.skill.Unstoppable;
import sts.kroos.cards.skill.WindUp;
import sts.kroos.characters.Kroos;
import sts.kroos.patches.KroosEnum;
import sts.kroos.relics.A1GroupPhoto;
import sts.kroos.relics.A1SquadSupport;
import sts.kroos.relics.ArrowImprovementRelic;
import sts.kroos.relics.Bedrock;
import sts.kroos.relics.BrokenCrossbow;
import sts.kroos.relics.EmergencyMindConcentrate;
import sts.kroos.relics.EmergencyMindReinforce;
import sts.kroos.relics.KroosBadge;
import sts.kroos.relics.OperatorBadge;
import sts.kroos.relics.OriginStone;
import sts.kroos.relics.PartingWaves;
import sts.kroos.relics.PracticeTarget;
import sts.kroos.relics.ProofOfGrowth;
import sts.kroos.relics.Scope;

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
        OnStartBattleSubscriber,
        PostInitializeSubscriber {

    public static final String MOD_ID = "kroosmod";

    // === 配色 (寒芒：偏冷的银白色) ===
    public static final com.badlogic.gdx.graphics.Color KROOS_COLOR_AWT =
            new com.badlogic.gdx.graphics.Color(176/255F, 196/255F, 222/255F, 1F);

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
                ATTACK_BG, SKILL_BG, POWER_BG, ENERGY_ORB,
                ATTACK_BG_P, SKILL_BG_P, POWER_BG_P, ENERGY_ORB_P,
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
        // 初始牌
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new Defend());
        BaseMod.addCard(new DoubleShot());
        BaseMod.addCard(new PreparedShot());
        // 白卡攻击
        BaseMod.addCard(new RapidFire());
        BaseMod.addCard(new Hunt());
        BaseMod.addCard(new FrostPierce());
        BaseMod.addCard(new FlawShot());
        BaseMod.addCard(new Volley());
        BaseMod.addCard(new WarningShot());
        BaseMod.addCard(new Yawn());
        BaseMod.addCard(new FlowingCloudArrow());
        BaseMod.addCard(new DreamDisturb());
        BaseMod.addCard(new HalfAwake());
        // 白卡技能
        BaseMod.addCard(new SimpleFortification());
        BaseMod.addCard(new EmergencyEvasion());
        BaseMod.addCard(new Rest());
        BaseMod.addCard(new OpenEyes());
        BaseMod.addCard(new WindUp());
        BaseMod.addCard(new AimForVital());
        BaseMod.addCard(new GoodDream());
        BaseMod.addCard(new Scout());
        BaseMod.addCard(new EmergencyReload());
        BaseMod.addCard(new EquipmentUpgrade());
        BaseMod.addCard(new MechanicalSight());
        // 蓝卡能力
        BaseMod.addCard(new Insomnia());
        BaseMod.addCard(new FlawConfirm());
        BaseMod.addCard(new HeartScar());
        BaseMod.addCard(new Transparent());
        BaseMod.addCard(new Unlit());
        BaseMod.addCard(new Resonance());
        BaseMod.addCard(new SupplyDrop());
        BaseMod.addCard(new DreamShadow());
        BaseMod.addCard(new Vigilance());
        BaseMod.addCard(new SkyfireFlame());
        BaseMod.addCard(new ArrowImprovement());
        BaseMod.addCard(new PhantomCamo());
        BaseMod.addCard(new Heartbeat());
        BaseMod.addCard(new Scatter());
        BaseMod.addCard(new Burst());
        // 蓝卡攻击
        BaseMod.addCard(new SkyVolley());
        BaseMod.addCard(new ExplosiveArrow());
        BaseMod.addCard(new ReinforcedArrow());
        BaseMod.addCard(new RainbowArrow());
        BaseMod.addCard(new GlitteringEdge());
        BaseMod.addCard(new SlowBurn());
        BaseMod.addCard(new Awareness());
        BaseMod.addCard(new PrecisionShot());
        BaseMod.addCard(new RiftStrike());

        // 蓝卡技能
        BaseMod.addCard(new RapidShoot());
        BaseMod.addCard(new Solitude());
        BaseMod.addCard(new Collapse());
        BaseMod.addCard(new HundredForge());
        BaseMod.addCard(new TruthAndDare());
        BaseMod.addCard(new Unstoppable());
        BaseMod.addCard(new FrostStar());
        BaseMod.addCard(new MemoryWound());
        BaseMod.addCard(new SupportOrder());
        BaseMod.addCard(new CrossbowModify());
        BaseMod.addCard(new HoldMode());
        BaseMod.addCard(new Hide());
        // 金卡攻击
        BaseMod.addCard(new ThroatSeal());
        BaseMod.addCard(new BayonetCharge());
        BaseMod.addCard(new Rouse());
        BaseMod.addCard(new PiercingCloud());
        BaseMod.addCard(new FinalCurtain());
        // 金卡技能
        BaseMod.addCard(new RapidShift());
        BaseMod.addCard(new Traceless());
        BaseMod.addCard(new TacticalShift());
        BaseMod.addCard(new Brace());
        BaseMod.addCard(new OneInstant());
        // 金卡能力
        BaseMod.addCard(new A1SquadBond());
        BaseMod.addCard(new HellForge());
        BaseMod.addCard(new FrostForm());
        // 衍生无色
        BaseMod.addCard(new NormalArrow());
        BaseMod.addCard(new A1_Fen());
        BaseMod.addCard(new A1_Migelu());
        BaseMod.addCard(new A1_Furong());
        BaseMod.addCard(new A1_Yanrong());

        UnlockTracker.unlockCard(Strike.ID);
        UnlockTracker.unlockCard(Defend.ID);
        UnlockTracker.unlockCard(DoubleShot.ID);
        UnlockTracker.unlockCard(PreparedShot.ID);
        UnlockTracker.unlockCard(RapidFire.ID);
        UnlockTracker.unlockCard(Hunt.ID);
        UnlockTracker.unlockCard(FrostPierce.ID);
        UnlockTracker.unlockCard(FlawShot.ID);
        UnlockTracker.unlockCard(Volley.ID);
        UnlockTracker.unlockCard(WarningShot.ID);
        UnlockTracker.unlockCard(Yawn.ID);
        UnlockTracker.unlockCard(FlowingCloudArrow.ID);
        UnlockTracker.unlockCard(DreamDisturb.ID);
        UnlockTracker.unlockCard(HalfAwake.ID);
        UnlockTracker.unlockCard(RiftStrike.ID);

        UnlockTracker.unlockCard(FinalCurtain.ID);
        UnlockTracker.unlockCard(SimpleFortification.ID);
        UnlockTracker.unlockCard(EmergencyEvasion.ID);
        UnlockTracker.unlockCard(Rest.ID);
        UnlockTracker.unlockCard(OpenEyes.ID);
        UnlockTracker.unlockCard(WindUp.ID);
        UnlockTracker.unlockCard(AimForVital.ID);
        UnlockTracker.unlockCard(GoodDream.ID);
        UnlockTracker.unlockCard(Scout.ID);
        UnlockTracker.unlockCard(EmergencyReload.ID);
        UnlockTracker.unlockCard(EquipmentUpgrade.ID);
        UnlockTracker.unlockCard(MechanicalSight.ID);
        UnlockTracker.unlockCard(Insomnia.ID);
        UnlockTracker.unlockCard(FlawConfirm.ID);
        UnlockTracker.unlockCard(HeartScar.ID);
        UnlockTracker.unlockCard(Transparent.ID);
        UnlockTracker.unlockCard(Unlit.ID);
        UnlockTracker.unlockCard(Resonance.ID);
        UnlockTracker.unlockCard(SupplyDrop.ID);
        UnlockTracker.unlockCard(DreamShadow.ID);
        UnlockTracker.unlockCard(Vigilance.ID);
        UnlockTracker.unlockCard(SkyfireFlame.ID);
        UnlockTracker.unlockCard(ArrowImprovement.ID);
        UnlockTracker.unlockCard(PhantomCamo.ID);
        UnlockTracker.unlockCard(Heartbeat.ID);
        UnlockTracker.unlockCard(Scatter.ID);
        UnlockTracker.unlockCard(Burst.ID);
        UnlockTracker.unlockCard(SkyVolley.ID);
        UnlockTracker.unlockCard(ExplosiveArrow.ID);
        UnlockTracker.unlockCard(ReinforcedArrow.ID);
        UnlockTracker.unlockCard(RainbowArrow.ID);
        UnlockTracker.unlockCard(GlitteringEdge.ID);
        UnlockTracker.unlockCard(SlowBurn.ID);
        UnlockTracker.unlockCard(Awareness.ID);
        UnlockTracker.unlockCard(PrecisionShot.ID);
        UnlockTracker.unlockCard(RapidShoot.ID);
        UnlockTracker.unlockCard(Solitude.ID);
        UnlockTracker.unlockCard(Collapse.ID);
        UnlockTracker.unlockCard(HundredForge.ID);
        UnlockTracker.unlockCard(TruthAndDare.ID);
        UnlockTracker.unlockCard(Unstoppable.ID);
        UnlockTracker.unlockCard(FrostStar.ID);
        UnlockTracker.unlockCard(MemoryWound.ID);
        UnlockTracker.unlockCard(SupportOrder.ID);
        UnlockTracker.unlockCard(CrossbowModify.ID);
        UnlockTracker.unlockCard(HoldMode.ID);
        UnlockTracker.unlockCard(Hide.ID);
        UnlockTracker.unlockCard(ThroatSeal.ID);
        UnlockTracker.unlockCard(BayonetCharge.ID);
        UnlockTracker.unlockCard(Rouse.ID);
        UnlockTracker.unlockCard(PiercingCloud.ID);
        UnlockTracker.unlockCard(RapidShift.ID);
        UnlockTracker.unlockCard(Traceless.ID);
        UnlockTracker.unlockCard(TacticalShift.ID);
        UnlockTracker.unlockCard(Brace.ID);
        UnlockTracker.unlockCard(OneInstant.ID);
        UnlockTracker.unlockCard(A1SquadBond.ID);
        UnlockTracker.unlockCard(HellForge.ID);
        UnlockTracker.unlockCard(FrostForm.ID);
        UnlockTracker.unlockCard(NormalArrow.ID);
        UnlockTracker.unlockCard(A1_Fen.ID);
        UnlockTracker.unlockCard(A1_Migelu.ID);
        UnlockTracker.unlockCard(A1_Furong.ID);
        UnlockTracker.unlockCard(A1_Yanrong.ID);
    }

    // === 遗物注册 ===
    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new KroosBadge(), KroosEnum.KROOS_COLOR);
        BaseMod.addRelicToCustomPool(new PracticeTarget(), KroosEnum.KROOS_COLOR);
        BaseMod.addRelicToCustomPool(new BrokenCrossbow(), KroosEnum.KROOS_COLOR);
        BaseMod.addRelicToCustomPool(new Scope(), KroosEnum.KROOS_COLOR);
        BaseMod.addRelicToCustomPool(new A1GroupPhoto(), KroosEnum.KROOS_COLOR);
        BaseMod.addRelicToCustomPool(new EmergencyMindReinforce(), KroosEnum.KROOS_COLOR);
        BaseMod.addRelicToCustomPool(new ArrowImprovementRelic(), KroosEnum.KROOS_COLOR);
        BaseMod.addRelicToCustomPool(new EmergencyMindConcentrate(), KroosEnum.KROOS_COLOR);
        BaseMod.addRelicToCustomPool(new PartingWaves(), KroosEnum.KROOS_COLOR);
        BaseMod.addRelicToCustomPool(new Bedrock(), KroosEnum.KROOS_COLOR);
        BaseMod.addRelicToCustomPool(new ProofOfGrowth(), KroosEnum.KROOS_COLOR);
        BaseMod.addRelicToCustomPool(new OriginStone(), KroosEnum.KROOS_COLOR);
        BaseMod.addRelicToCustomPool(new OperatorBadge(), KroosEnum.KROOS_COLOR);
        BaseMod.addRelicToCustomPool(new A1SquadSupport(), KroosEnum.KROOS_COLOR);

        UnlockTracker.markRelicAsSeen(KroosBadge.ID);
        UnlockTracker.markRelicAsSeen(PracticeTarget.ID);
        UnlockTracker.markRelicAsSeen(BrokenCrossbow.ID);
        UnlockTracker.markRelicAsSeen(Scope.ID);
        UnlockTracker.markRelicAsSeen(A1GroupPhoto.ID);
        UnlockTracker.markRelicAsSeen(EmergencyMindReinforce.ID);
        UnlockTracker.markRelicAsSeen(ArrowImprovementRelic.ID);
        UnlockTracker.markRelicAsSeen(EmergencyMindConcentrate.ID);
        UnlockTracker.markRelicAsSeen(PartingWaves.ID);
        UnlockTracker.markRelicAsSeen(Bedrock.ID);
        UnlockTracker.markRelicAsSeen(ProofOfGrowth.ID);
        UnlockTracker.markRelicAsSeen(OriginStone.ID);
        UnlockTracker.markRelicAsSeen(OperatorBadge.ID);
        UnlockTracker.markRelicAsSeen(A1SquadSupport.ID);
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
                // Keyword 类只有 NAMES 和 DESCRIPTION 字段, 无 PROPER_NAME
                // NAMES[0] 即为显示名 (与 JSON 中 PROPER_NAME 对应)
                BaseMod.addKeyword(MOD_ID, k.NAMES[0], k.NAMES, k.DESCRIPTION);
            }
        }
    }

    @Override
    public void receivePostInitialize() {
        // 预留：badge / mod 设置面板
    }

    @Override
    public void receiveOnBattleStart(com.megacrit.cardcrawl.rooms.AbstractRoom room) {
        // 战斗开始时清空战斗范围计数器 (强化箭/精准射击等持续计数依赖)
        sts.kroos.util.BattleCounters.resetAll();
    }
}
