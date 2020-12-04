package Moonworks.cards.tempCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class EvilSpyWorkExecution extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(EvilSpyWorkExecution.class.getSimpleName());
    public static final String IMG = makeCardPath("EvilSpyWorkExecution.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int DRAW = 1;
    private static final int UPGRADE_PLUS_DRAW = 1;

    // /STAT DECLARATION/


    public EvilSpyWorkExecution() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        AutoplayField.autoplay.set(this, true);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DRAW;
        this.purgeOnUse = true;
        this.isMultiDamage = true;
        this.setDisplayRarity(CardRarity.UNCOMMON);
        setBackgroundTexture(OrangeJuiceMod.TEMP_ATTACK_WHITE_ICE, OrangeJuiceMod.TEMP_ATTACK_WHITE_ICE_PORTRAIT);
    }
    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add("Special");
        return tags;
    }

    private static ArrayList<TooltipInfo> specialTooltip;
    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (specialTooltip == null)
        {
            specialTooltip = new ArrayList<>();
            specialTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:Special"), BaseMod.getKeywordDescription("moonworks:Special")));
        }
        return specialTooltip;
    }

    @Override
    public float getTitleFontSize() {
        return 17F;
    }
    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //this.purgeOnUse = getNormaLevel() < 3 || AbstractDungeon.cardRandomRng.random(1, 2) != 1;
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        this.addToBot(new DrawCardAction(magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}