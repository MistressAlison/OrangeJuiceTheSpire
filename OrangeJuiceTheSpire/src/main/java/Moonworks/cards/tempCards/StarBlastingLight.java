package Moonworks.cards.tempCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.BlastingLightPower;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class StarBlastingLight extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(StarBlastingLight.class.getSimpleName());
    public static final String IMG = makeCardPath("StarBlastingLight.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static String TALK_TEXT;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;

    private static final int MIN_HITS = 3;
    private static final int UPGRADE_PLUS_MIN_HITS = 1;
    private static final int MAX_HITS = 5;
    private static final int UPGRADE_PLUS_MAX_HITS = 2;

    // /STAT DECLARATION/


    public StarBlastingLight() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        //damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MIN_HITS;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = MAX_HITS;
        this.purgeOnUse = true;
        this.setDisplayRarity(CardRarity.RARE);
        this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        //this.bannerColor = BANNER_COLOR_RARE.cpy();
        //this.imgFrameColor = IMG_FRAME_COLOR_RARE.cpy();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int bonus = 0;
        switch (getNormaLevel()) {
            case 5: bonus++;
            case 4: bonus++;
            case 3: bonus++;
            case 2: bonus++;
            case 1: bonus++;
            default:
        }
        TALK_TEXT = cardStrings.EXTENDED_DESCRIPTION[AbstractDungeon.cardRandomRng.random(0, 2)];
        this.addToBot(new TalkAction(true, TALK_TEXT, 4.0f, 2.0f));
        this.addToBot(new VFXAction(p, new ScreenOnFireEffect(), 1.0F));
        int hits = AbstractDungeon.cardRandomRng.random(magicNumber+bonus, defaultSecondMagicNumber+bonus);
        for (int i = 0 ; i < hits ; i++) {
            for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters){
                if (!aM.isDeadOrEscaped()) {
                    this.addToBot(new ApplyPowerAction(aM, p, new BlastingLightPower(aM, 1)));
                }
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPGRADE_PLUS_MIN_HITS);
            upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_MAX_HITS);
            initializeDescription();
        }
    }
}