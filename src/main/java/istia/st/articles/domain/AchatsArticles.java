package istia.st.articles.domain;

import java.util.ArrayList;
import java.util.List;

import istia.st.articles.dao.Article;
import istia.st.articles.dao.IArticlesDao;

public class AchatsArticles implements IArticlesDomain {
    private IArticlesDao articlesDao;
    private Object synchro = new Object();
    private ArrayList erreurs;

    /**
     * @return Returns the erreurs.
     */
    public ArrayList getErreurs() {
        return erreurs;
    }

    /**
     * @param articlesDao leservice d'accès aux données
     */
    public AchatsArticles(IArticlesDao articlesDao) {
        this.articlesDao = articlesDao;
    }

    public List getAllArticles() {
        return articlesDao.getAllArticles();
    }

    public Article getArticleById(int id) {
        return articlesDao.getArticleById(id);
    }

    public void acheter(Panier panier) {
// on parcourt les achats
        ArrayList achats = panier.getAchats();
        erreurs = new ArrayList();
        Article article = null;
        Achat achat = null;
        for (int i = achats.size() - 1; i >= 0; i--) {
// on récupère l'achat
            achat = (Achat) achats.get(i);
// on tente de modifier le stock de l'article dans la base
            int nbarticles =
                    articlesDao.changerStockArticle(
                            achat.getArticle().getId(),
                            -achat.getQte());
// a-t-on réussi ?
            if (nbarticles != 0) {

                achats.remove(i);
            } else {
                erreurs.add(
                        "Achat article ["
                                + achat.getArticle()
                                + ","
                                + achat.getQte()
                                + "] impossible - Vérifiez son stock");
            }
        }
    }
}