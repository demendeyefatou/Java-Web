package istia.st.articles.tests;
import istia.st.articles.dao.IArticlesDao; public class ThreadMajStock extends Thread {
    /**
     *	nom du thread
     */
    private String name;
    /**
     *	objet d'accès aux données
     */
    private IArticlesDao articlesDao;
    /**
     *
     *	@param name
     *	le nom du thread afin de l'identifier
     *	@param articlesDao
     *	l'objet d'accès aux données du sgbd
     */
    public ThreadMajStock(String name, IArticlesDao articlesDao) { this.name = name;
        this.articlesDao = articlesDao;
    }
    /**
     *	décrémente le stock de l'article 3 d'une unité fait un suivi écran des
     *	opérations
     */
    public void run() {
// suivi
        System.out.println(name + " lancé");
// modification stock article 3 articlesDao.changerStockArticle(3, -1);
// suivi
        System.out.println(name + " terminé");
    }
}
