package istia.st.articles.domain;

import istia.st.articles.dao.Article;

import java.util.ArrayList;

public class Panier {
    /**
     * liste des achats
     */
    private ArrayList achats = new ArrayList();

    /**
     * @return Returns the achats.
     */
    public ArrayList getAchats() {
        return achats;
    }

    /**
     * ajoute un achat
     *
     * @param unAchat
     */
    public void ajouter(Achat unAchat) {
        // on ajoute l'achat
        // on cherche si l'article a déjà été acheté
        int idArticle = unAchat.getArticle().getId();
        Article article = null;
        Achat achat = null;
        boolean trouve = false;
        for (int i = 0; !trouve && i < achats.size(); i++) {
            achat = (Achat) achats.get(i);
            article = (Article) achat.getArticle();
            if (article.getId() == idArticle) {
                achat.setQte(achat.getQte() + unAchat.getQte());
                trouve = true;
            }
        }
        // article trouvé ?
        if (!trouve) {
            // pas encore acheté - on ajoute l'achat
            achats.add(unAchat);
        }
    }


    public void enlever(int idArticle) {
        // on enlève l'achat d'id idAchat
        for (int i = 0; i < achats.size(); i++) {
            if (((Achat) achats.get(i)).getArticle().getId() == idArticle) {
                achats.remove(i);
            }
        }
    }

    /**
     * @return le total à payer
     */
    public double getTotal() {
        // on fait le total des achats
        double total = 0;
        for (int i = 0; i < achats.size(); i++) {
            total += ((Achat) achats.get(i)).getTotal();
        }
        return total;
    }

    /**
     * @return une chaîne représentant le panier
     */
    public String toString() {
        return this.achats.toString();
    }
}