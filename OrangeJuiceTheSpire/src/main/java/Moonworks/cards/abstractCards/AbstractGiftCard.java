package Moonworks.cards.abstractCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.RecoverExhaustedGiftAction;
import Moonworks.actions.WitherExhaustImmediatelyAction;
import Moonworks.relics.GoldenDie;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGiftCard extends AbstractNormaAttentiveCard {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    protected final String spentDescription;
    private static ArrayList<TooltipInfo> GiftTooltip;
    //protected boolean active;
    public boolean checkedGolden;
    public final boolean ignoreGolden;
    public static final float GOLDEN_BUFF = 1.5F;
    public boolean hasUses;

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses) {

        this(id, img, cost, type, color, rarity, target, uses, currentUses, false, false, null);
    }

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses, final boolean checkedGolden) {

        this(id, img, cost, type, color, rarity, target, uses, currentUses, checkedGolden, false, null);
    }

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses, final boolean checkedGolden, final boolean ignoreGolden) {

        this(id, img, cost, type, color, rarity, target, uses, currentUses, checkedGolden, ignoreGolden, null);
    }

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses, Integer[] normaLevels) {

        this(id, img, cost, type, color, rarity, target, uses, currentUses, false, false, normaLevels);
    }

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses, final boolean checkedGolden, Integer[] normaLevels) {

        this(id, img, cost, type, color, rarity, target, uses, currentUses, checkedGolden, false, normaLevels);
    }

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses, final boolean checkedGolden, final boolean ignoreGolden, Integer[] normaLevels) {

        super(id, img, cost, type, color, rarity, target, normaLevels);
        CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(id);
        spentDescription = cardStrings.EXTENDED_DESCRIPTION[0];
        this.hasUses = currentUses > 0;
        if (currentUses <= 0) {
            rawDescription = spentDescription;
        } else {
            rawDescription = DESCRIPTION;
        }
        this.secondMagicNumber = currentUses;
        this.baseSecondMagicNumber = uses;
        if (uses != currentUses) {
            this.isSecondMagicNumberModified = true;
        }
        this.checkedGolden = checkedGolden;
        this.ignoreGolden = ignoreGolden;
        this.selfRetain = true; //Let it retain N times
        //this.isEthereal = true; //Then it is ethereal
        setBackgroundTexture(OrangeJuiceMod.GIFT_WHITE_ICE, OrangeJuiceMod.GIFT_WHITE_ICE_PORTRAIT);
        initializeDescription();
    }

    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add(BaseMod.getKeywordTitle("moonworks:gift"));
        tags.addAll(super.getCardDescriptors());
        return tags;
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (GiftTooltip == null)
        {
            GiftTooltip = new ArrayList<>();
            GiftTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:gift"), BaseMod.getKeywordDescription("moonworks:gift")));
        }
        List<TooltipInfo> compoundList = new ArrayList<>(GiftTooltip);
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }

    @Override
    public void triggerWhenDrawn() {
        checkGolden(); //This will be the main way we check for the buff
        initializeDescription();
        super.triggerWhenDrawn();
    }

    @Override
    public void onMoveToDiscard() {
        checkGolden(); //This will happen if the card is generated or transformed, and then we play it to discard it
        super.onMoveToDiscard();
    }

    @Override
    public void moveToDiscardPile() {
        checkGolden(); //May as well check both cases
        super.moveToDiscardPile();
    }

    @Override
    public void onRetained() {
        checkGolden(); //This will happen if the card is generated or transformed, and then not played to discard it
        super.onRetained();
    }

    public boolean isActive() {
        return isActive(false);
    }

    public boolean isActive(boolean onDrawFlag) {
        return isInHand(onDrawFlag) && (secondMagicNumber >= 1);
    }

    public boolean isInHand() {
        return isInHand(false);
    }

    public boolean isInHand(boolean onDrawFlag) {
        if (AbstractDungeon.player == null) {
            return false;
        }
        if (onDrawFlag) {
            return true;
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c == this) return true;
        }
        return false;
    }

    public void modifyUses(int uses) {
        this.secondMagicNumber += uses;
        if (secondMagicNumber != baseSecondMagicNumber) {
            this.isSecondMagicNumberModified = true;
        }
        this.hasUses = secondMagicNumber > 0;
        this.selfRetain = hasUses; //Retain while we have uses
        this.exhaust = !hasUses; //Exhaust and Ethereal if we dont. This is for if we pull it back from the exhaust pile with no uses
        this.isEthereal = !hasUses;
        if (!hasUses) { //If we hit 0, or below 0 somehow, exhaust immediately
            rawDescription = this.spentDescription;
            initializeDescription(); //Initialize before we move to exhaust
            this.unhover();
            this.untip();
            this.stopGlowing();
            AbstractDungeon.actionManager.removeFromQueue(this);
            this.addToTop(new WitherExhaustImmediatelyAction(this)); //Hijack this wither code I wrote before, lol
        } else {
            //We dont set active is true here, since it might not be in our hand, and shouldnt be active if it isnt
            rawDescription = this.DESCRIPTION;
            initializeDescription();
        }
    }

    public void checkGolden() {
        if (!ignoreGolden && !checkedGolden) {
            boolean goldenDie = AbstractDungeon.player.hasRelic(GoldenDie.ID);
            this.secondMagicNumber *= (goldenDie ? GOLDEN_BUFF : 1);
            //this.defaultBaseSecondMagicNumber += (goldenDie ? GOLDEN_BUFF : 0);
            if (goldenDie) {
                AbstractDungeon.player.getRelic(GoldenDie.ID).flash();
                this.isSecondMagicNumberModified = true;
            }
            checkedGolden = true;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        initializeDescription();
    }

    public void applyEffect() { //If we need additional effects, they can be defined on a card by card basis, and simply call this to know to decrement the uses
        //Maybe make the card flash or something nifty?
        this.flash();
        modifyUses(-1); //Reduce our uses by 1 each time the effect happens
        initializeDescription();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m);
    }

    public static ArrayList<AbstractGiftCard> getExhaustedGifts() {
        ArrayList<AbstractGiftCard> exhaustedGifts = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card instanceof AbstractGiftCard) {
                exhaustedGifts.add((AbstractGiftCard)card);
            }
        }
        return exhaustedGifts;
    }

    public static AbstractGiftCard getRandomExhaustedGift() {
        ArrayList<AbstractGiftCard> exhaustedGifts = getExhaustedGifts();
        if (exhaustedGifts.isEmpty()) {
            return null;
        } else {
            return exhaustedGifts.get(AbstractDungeon.cardRandomRng.random(0, exhaustedGifts.size()-1));
        }
    }

    public static boolean purgeRandomExhaustedGift() {
        AbstractGiftCard gift = getRandomExhaustedGift();
        if (gift != null) {
            AbstractDungeon.player.exhaustPile.removeCard(gift);
            return true;
        } else {
            return false;
        }
    }

    public static void restoreGiftUses(AbstractGiftCard gift, int amount) {
        gift.modifyUses(amount);
        if (gift.hasUses) {
            gift.unhover();
            gift.unfadeOut();
            AbstractDungeon.player.exhaustPile.moveToDeck(gift, true);
            //AbstractDungeon.player.drawPile.addToRandomSpot(gift);
            AbstractDungeon.player.exhaustPile.removeCard(gift);
        }
    }

    public static void restoreAllGiftUses(AbstractGiftCard gift) {
        gift.secondMagicNumber = gift.baseSecondMagicNumber;
        gift.checkedGolden = false; // Since we reset to base values, we want to check golden again
        restoreGiftUses(gift, 0); //Just do a 0 call here since we dont both checking for non 0 anywhere, this ensures we stay at max capacity
    }

    public static void bottledOrangeHelper(AbstractGiftCard gift) {
        gift.secondMagicNumber = gift.baseSecondMagicNumber;
        gift.checkedGolden = false; // Since we reset to base values, we want to check golden again
        gift.modifyUses(0);
        gift.unhover();
        gift.unfadeOut();
    }

    public static void recoverRandomExhaustedGift(int recoverAmount) {
        AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
            public void update() {
                for (int i = 0 ; i < recoverAmount ; i++) {
                    AbstractGiftCard gift = getRandomExhaustedGift();
                    if(gift != null) {
                        restoreAllGiftUses(gift);
                    }
                }
                this.isDone = true;
            }
        });
    }

    public static void recoverSpecificExhaustedGift(int recoverAmount) {
        AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
            public void update() {
                AbstractDungeon.actionManager.addToBottom(new RecoverExhaustedGiftAction(recoverAmount));
                this.isDone = true;
            }
        });
    }

    //Dont mess up Gift Uses. I should make a new dynvar unique to Gifts tbh
    @Override
    public void resetAttributes() {
        int currentUses = secondMagicNumber;
        super.resetAttributes();
        secondMagicNumber = currentUses;
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
    }
}