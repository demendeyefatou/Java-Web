package istia.st.articles.dao;
import java.util.List;
/**
 * @author serge
 *
 */
public interface IArticlesDao {
    /**
     * @return : liste de tous les articles
     */
    public List getAllArticles();
    /**
     * @param unArticle :
     * l'article à ajouter
     */
    public int ajouteArticle(Article unArticle);
    /**
     * @param idArticle :
     * id de l'article à supprimer
     */
    public int supprimeArticle(int idArticle);
    /**
     * @param unArticle :
     * l'article à modifier
     */
    public int modifieArticle(Article unArticle);
    /**
     * @param idArticle :
     * id de l'article cherché
     * @return : l'article trouvé ou null
     */
    public Article getArticleById(int idArticle);
    /**
     * vide la table des articles
     */
    public void clearAllArticles();
    /**
     *
     * @param idArticle id de l'article dont on change le stock
     * @param mouvement valeur à ajouter au stock (valeur signée)
     */
    public int changerStockArticle(int idArticle, int mouvement);
}
