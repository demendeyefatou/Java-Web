package istia.st.articles.tests.domain;
import java.util.List;
import junit.framework.TestCase; import istia.st.articles.dao.Article;
import istia.st.articles.dao.IArticlesDao; import istia.st.articles.domain.Achat;
import istia.st.articles.domain.IArticlesDomain; import istia.st.articles.domain.Panier;

import org.springframework.beans.factory.xml.XmlBeanFactory; import org.springframework.core.io.ClassPathResource;
// test de la classe ArticlesDaoSqlMap
public class JunitModeleDomainArticles extends TestCase {
// une instance de la classe d'accès au domaine
    private IArticlesDomain articlesDomain;

    // une instance de la classe d'accès aux données
     private IArticlesDao articlesDao;
    protected void setUp() throws Exception {
// récupère une instance d'accès au domaine
articlesDomain = (IArticlesDomain) (new XmlBeanFactory(new ClassPathResource("spring-config-test-domain.xml"))).getBean("articlesDomain");
// récupère une instance d'accès aux données
        articlesDao = (IArticlesDao) (new XmlBeanFactory(new ClassPathResource("spring-config-test-domain.xml"))).getBean("articlesDao");
    }
    // récupération d'un article particulier
    public void testGetArticleById() {
// suppression du contenu de ARTICLES articlesDao.clearAllArticles();
// lit la table ARTICLES
    List articles = articlesDao.getAllArticles(); assertEquals(0, articles.size());
// insertion
articlesDao.ajouteArticle(new Article(3, "article3", 30, 30, 3));
articlesDao.ajouteArticle(new Article(4, "article4", 40, 40, 4));
// lit la table ARTICLES
    articles = articlesDomain.getAllArticles(); assertEquals(2, articles.size());
    // getById
    Article unArticle = articlesDomain.getArticleById(3); assertEquals(unArticle.getNom(), "article3"); unArticle = (Article) articlesDao.getArticleById(4); assertEquals(unArticle.getNom(), "article4");
}
    // affichage écran
    private void listArticles() {
// lit la table ARTICLES
        List articles = articlesDomain.getAllArticles();
// on affiche les articles lus
        for (int i = 0; i < articles.size(); i++) { System.out.println(((Article) articles.get(i)).toString());
        }
    }
    // achats d'articles
    public void testAchatPanier(){
// suppression du contenu de ARTICLES articlesDao.clearAllArticles();
// lit la table ARTICLES
        List articles = articlesDao.getAllArticles(); assertEquals(0, articles.size());
// insertion
        Article article3=new Article(3, "article3", 30, 30, 3); articlesDao.ajouteArticle(article3);
        Article article4=new Article(4, "article4", 40, 40, 4); articlesDao.ajouteArticle(article4);
// lit la table ARTICLES
        articles = articlesDomain.getAllArticles(); assertEquals(2, articles.size());
// création d'un panier avec deux achats
Panier panier=new Panier(); panier.ajouter(new Achat(article3,10)); panier.ajouter(new Achat(article4,10));
// vérifications
assertEquals(700.0,panier.getTotal(),1e-6); assertEquals(2,panier.getAchats().size());
// validation panier
articlesDomain.acheter(panier);
// vérifications
assertEquals(0,articlesDomain.getErreurs().size()); assertEquals(0,panier.getAchats().size());
// rechercher article n° 3
article3=articlesDomain.getArticleById(3); assertEquals(20,article3.getStockActuel());
// rechercher article n° 4
article4=articlesDomain.getArticleById(4); assertEquals(30,article4.getStockActuel());
// nouveau panier
        panier.ajouter(new Achat(article3,100));
// validation panier
articlesDomain.acheter(panier);
// vérifications - on a trop acheté
// on doit avoir une erreur
assertEquals(1,articlesDomain.getErreurs().size());
// rechercher article n° 3 a
article3=articlesDomain.getArticleById(3);
// son stock n'a pas du changer
    assertEquals(20,article3.getStockActuel());
    }
    // retirer des achats
    public void testRetirerAchats(){
// suppression du contenu de ARTICLES
    articlesDao.clearAllArticles();
        // lit la table ARTICLES
        List articles = articlesDao.getAllArticles(); assertEquals(0, articles.size());
// insertion
        Article article3=new Article(3, "article3", 30, 30, 3); articlesDao.ajouteArticle(article3);
        Article article4=new Article(4, "article4", 40, 40, 4); articlesDao.ajouteArticle(article4);
// lit la table ARTICLES
        articles = articlesDomain.getAllArticles(); assertEquals(2, articles.size());
// création d'un panier avec deux achats
Panier panier=new Panier(); panier.ajouter(new Achat(article3,10)); panier.ajouter(new Achat(article4,10));
// vérifications
assertEquals(700.0,panier.getTotal(),1e-6); assertEquals(2,panier.getAchats().size());
// ajouter un article déjà acheté
panier.ajouter(new Achat(article3,10));
// vérifications
// le total doit être passé à 1000
assertEquals(1000.0,panier.getTotal(),1e-6);
// toujours 2 articles dans le panier
assertEquals(2,panier.getAchats().size());
// qté article 3 a du passer à 20
        Achat achat=(Achat)panier.getAchats().get(0); assertEquals(20,achat.getQte());
// on retire l'article 3 du panier p
panier.enlever(3);
// vérifications
// le total doit être passé à 400
assertEquals(400.0,panier.getTotal(),1e-6);
// 1 seul article dans le panier
assertEquals(1,panier.getAchats().size());
// ce doit être l'article n° 4
assertEquals(4,((Achat)panier.getAchats().get(0)).getArticle().getId());
    }
}

