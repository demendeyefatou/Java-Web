package istia.st.articles.domain;
// Imports
import istia.st.articles.dao.Article;

import java.util.ArrayList; import java.util.List;
public abstract interface IArticlesDomain
{
    // Méthodes
    void acheter(Panier panier);
    List getAllArticles();
    Article getArticleById(int idArticle); ArrayList getErreurs();
}

