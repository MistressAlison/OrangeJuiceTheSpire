package Moonworks.cards;

import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.powers.TemporaryDexterityPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class LulusLuckyEgg extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(LulusLuckyEgg.class.getSimpleName());
    public static final String IMG = makeCardPath("LulusLuckyEgg.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String SPENT_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int BASE_BLOCK = 5;

    private static final int BONUS_EFFECT = 2;
    private static final int UPGRADE_PLUS_BONUS_EFFECT = 1;

    // /STAT DECLARATION/


    public LulusLuckyEgg() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.block = this.baseBlock = BASE_BLOCK;
        this.magicNumber = this.baseMagicNumber = BONUS_EFFECT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = AbstractDungeon.cardRandomRng.random(1, 3); //Choose a random number for the effect
        switch (effect) { //Switches allow us to run code based on the value in the switch.
            case 1: //Big Block. We multiply add block 1 or 2 more times here
                //Loop for 1 less than the magic number, since we still block normally after
                for (int i = 0 ; i < magicNumber - 1 ; i++) {
                    this.addToBot(new GainBlockAction(p, p, block));
                }
                break; //Break after each case since we dont want it to then look at the other cases
            case 2: //Draw 2 or 3 cards, as this is stored in magicNumber
                this.addToBot(new DrawCardAction(magicNumber));
                break;
            case 3: //Buff 2 or 3 stacks of temp dex
                this.addToBot(new ApplyPowerAction(p, p, new TemporaryDexterityPower(p, magicNumber)));
                break;
        }
        this.addToBot(new GainBlockAction(p, p, block)); //Do our block action.
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BONUS_EFFECT);
            initializeDescription();
        }
    }
}
