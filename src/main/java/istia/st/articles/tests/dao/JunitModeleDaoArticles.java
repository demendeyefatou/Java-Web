
package istia.st.articles.tests.dao;
import java.util.List;

import istia.st.articles.tests.ThreadMajStock;
import junit.framework.TestCase;
import istia.st.articles.dao.IArticlesDao; import istia.st.articles.dao.Article;
import org.springframework.beans.factory.xml.XmlBeanFactory; import org.springframework.core.io.ClassPathResource;
// test de la classe ArticlesDaoSqlMap
public class JunitModeleDaoArticles extends TestCase {
    // une instance de la classe testée
    private IArticlesDao articlesDao;
    protected void setUp() throws Exception {
// récupère une instance d'accès aux données
        articlesDao = (IArticlesDao) (new XmlBeanFactory(new ClassPathResource("spring-config-test-dao.xml"))).getBean("articlesDao");
    }
    public void testGetAllArticles() {
// affiche les articles
listArticles();
    }
    public void testClearAllArticles() {
// vide la table des articles
articlesDao.clearAllArticles();
// lit la table ARTICLES
        List articles = articlesDao.getAllArticles(); assertEquals(0, articles.size());
    }
    public void testAjouteArticle() {
// suppression du contenu de ARTICLES
articlesDao.clearAllArticles();
// lit la table ARTICLES
        List articles = articlesDao.getAllArticles(); assertEquals(0, articles.size());
// insertion
        articlesDao.ajouteArticle(new Article(3, "article3", 30, 30, 3));
        articlesDao.ajouteArticle(new Article(4, "article4", 40, 40, 4));
// lit la table ARTICLES
        articles = articlesDao.getAllArticles(); assertEquals(2, articles.size());
//l'affiche
listArticles();
    }
    public void testSupprimeArticle() {
// suppression du contenu de ARTICLES articlesDao.clearAllArticles();
// lit la table ARTICLES
        List articles = articlesDao.getAllArticles(); assertEquals(0, articles.size());
// insertion
        articlesDao.ajouteArticle(new Article(3, "article3", 30, 30, 3));
        articlesDao.ajouteArticle(new Article(4, "article4", 40, 40, 4));
// lit la table ARTICLES
        articles = articlesDao.getAllArticles(); assertEquals(2, articles.size());
// suppression
articlesDao.supprimeArticle(4);
// lit la table ARTICLES
        articles = articlesDao.getAllArticles(); assertEquals(1, articles.size());
// affiche la table
listArticles();
    }
    public void testModifieArticle() {
// suppression du contenu de ARTICLES
articlesDao.clearAllArticles();
// lit la table ARTICLES
        List articles = articlesDao.getAllArticles(); assertEquals(0, articles.size());
// insertion
        articlesDao.ajouteArticle(new Article(3, "article3", 30, 30, 3));
        articlesDao.ajouteArticle(new Article(4, "article4", 40, 40, 4));
// lit la table ARTICLES
        articles = articlesDao.getAllArticles(); assertEquals(2, articles.size());
// getById
        Article unArticle = (Article) articlesDao.getArticleById(3); assertEquals(unArticle.getNom(), "article3");
        unArticle = (Article) articlesDao.getArticleById(4);
        assertEquals(unArticle.getNom(), "article4");
// modification
        articlesDao.modifieArticle(new Article(4, "article4", 44, 44, 44));
// getById
        unArticle = (Article) articlesDao.getArticleById(4); assertEquals(unArticle.getPrix(), 44, 1e-6);
// affiche la table
listArticles();
    }
    public void testGetArticleById() {
// suppression du contenu de ARTICLES
articlesDao.clearAllArticles();
// lit la table ARTICLES
        List articles = articlesDao.getAllArticles(); assertEquals(0, articles.size());
// insertion
        articlesDao.ajouteArticle(new Article(3, "article3", 30, 30, 3));
        articlesDao.ajouteArticle(new Article(4, "article4", 40, 40, 4));
// lit la table ARTICLES
        articles = articlesDao.getAllArticles();
        assertEquals(2, articles.size());
// getById
        Article unArticle = (Article) articlesDao.getArticleById(3); assertEquals(unArticle.getNom(), "article3");
        unArticle = (Article) articlesDao.getArticleById(4);
        assertEquals(unArticle.getNom(), "article4");
    }
    private void listArticles() {
// lit la table ARTICLES
        List articles = articlesDao.getAllArticles();
// on affiche les articles lus
        for (int i = 0; i < articles.size(); i++) { System.out.println(((Article) articles.get(i)).toString());
        }
    }
    public void testChangerStockArticle() throws InterruptedException {
// suppression du contenu de ARTICLES
articlesDao.clearAllArticles();
// insertion
        int nbArticles = articlesDao.ajouteArticle(new Article(3, "article3", 30, 101, 3));
        assertEquals(nbArticles, 1);
        nbArticles = articlesDao.ajouteArticle(new Article(4, "article4", 40, 40, 4));
        assertEquals(nbArticles, 1);
// création de 100 threads de mise à jour du stock de l'article 3
Thread[] taches = new Thread[100];
        for (int i = 0; i < taches.length; i++) {
            taches[i] = new ThreadMajStock("thread-" + i, articlesDao); taches[i].start();
        }
// on attend la fin des threads
        for (int i = 0; i < taches.length; i++) { taches[i].join();
        }
// récupérer l'article 3 et vérifier son stock
Article unArticle = (Article) articlesDao.getArticleById(3); assertEquals(unArticle.getNom(), "article3"); assertEquals(1, unArticle.getStockActuel());
// modification stock article 4
boolean erreur = false;
        int nbLignes = articlesDao.changerStockArticle(4, -100); assertEquals(0, nbLignes);
// affiche la table
listArticles();
    }
}


