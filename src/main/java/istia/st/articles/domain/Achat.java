package istia.st.articles.domain;

import istia.st.articles.dao.Article;

public class Achat {
    // Champs
    private Article article; private int qte;
    // Constructeurs
    public Achat(Article article, int qte) {

        this.article = article;
        this.qte = qte;

    }
    // Méthodes
    public double getTotal() {
        return article.getPrix() * qte;
    }
    public Article getArticle() {
        return article;
    }
    public void setArticle(Article article) {
        this.article = article;


    }
    public int getQte() {
        return qte;

    }
    public void setQte(int i) {

        this.qte = qte;

    }
    public String toString() {
        return "[" + this.article.toString() + "," + this.qte + "]";
    }
}
