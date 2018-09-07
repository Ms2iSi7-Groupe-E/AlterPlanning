package fr.nantes.eni.alterplanning.model.simplebean;

public class PromotionBean {

    private String codePromotion;
    private String libellePromotion;

    public PromotionBean() {
    }

    public PromotionBean(String codePromotion, String libellePromotion) {
        this.codePromotion = codePromotion;
        this.libellePromotion = libellePromotion;
    }

    public String getCodePromotion() {
        return codePromotion;
    }

    public void setCodePromotion(String codePromotion) {
        this.codePromotion = codePromotion;
    }

    public String getLibellePromotion() {
        return libellePromotion;
    }

    public void setLibellePromotion(String libellePromotion) {
        this.libellePromotion = libellePromotion;
    }
}
