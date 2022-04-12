package Moonworks.augments;

import CardAugments.cardmods.AbstractAugment;
import Moonworks.OrangeJuiceMod;
import Moonworks.actions.ApplyAndUpdateMemoriesAction;
import Moonworks.powers.BookOfMemoriesPower;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class SealedMod extends AbstractAugment {
    public static final String ID = OrangeJuiceMod.makeID("SealedMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    @Override
    public boolean validCard(AbstractCard card) {
        return OrangeJuiceMod.enableChimeraCrossover && BookOfMemoriesPower.getViability(card) && cardCheck(card, c -> notExhaust(c));
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TEXT[0] + cardName + TEXT[1];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + TEXT[2];
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        card.purgeOnUse = true;
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        card.purgeOnUse = false;
                        this.addToTop(new ApplyAndUpdateMemoriesAction(card));
                        this.isDone = true;
                    }
                });
                this.isDone = true;
            }
        });
    }

    @Override
    public void onDrawn(AbstractCard card) {
    }

    @Override
    public AbstractAugment.AugmentRarity getModRarity() {
        return AugmentRarity.UNCOMMON;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SealedMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
