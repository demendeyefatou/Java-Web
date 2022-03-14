package istia.st.articles.dao;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.ibatis.common.resources.*;
import com.ibatis.sqlmap.client.*;
import istia.st.articles.dao.Article;
import istia.st.articles.exception.*;

/**
 * @author ST-ISTIA
 */
public class ArticlesDaoSqlMap implements IArticlesDao
{
    /**
     * sqlMap objet permettant l'accès aux données d'un SGBD
     * est construit à partir d'un fichier de configuration
     * exécute des requêtes SQL elles-aussi enregistrées dans un fichier de configuration
     */
    private SqlMapClient sqlMap = null;

    /**
     * @return l'objet sqlMap d'accès aux données
     */
    public SqlMapClient getSqlMap()
    {
        return sqlMap;
    }

    /**
     * @param sqlMap l'objet sqlMap d'accès aux données
     */
    public void setSqlMap(SqlMapClient sqlMap) {
        this.sqlMap = sqlMap;
    }

    /**
     * @param sqlMapConfigFileName le nom du fichier de configuration de SqlMap
     * @throws UncheckedAccessArticlesException si pb quelconque
     */
    public ArticlesDaoSqlMap(String sqlMapConfigFileName)
    {
        Reader reader = null;
        UncheckedAccessArticlesException ex = null;
        try
        {
            reader = Resources.getResourceAsReader(sqlMapConfigFileName);
            sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
        } catch (Exception ex1)
        {
            ex = new UncheckedAccessArticlesException(
                    "Erreur lors de la construction de l'objet [sqlMap] à partir du fichier de configuration ["
                            + sqlMapConfigFileName + "] : ", ex1);
        } finally {
            try {
                reader.close();
            } catch (Exception ex2) {
                if (ex != null) {
                    ex = new UncheckedAccessArticlesException(
                            "Erreur lors de la fermeture de l'objet [reader] à partir du fichier de configuration ["
                                    + sqlMapConfigFileName + "] : ", ex2);
                }
            }
        }
        // exception à lancer ?
        if (ex != null) {
            throw ex;
        }
    }

    /**
     * @return la liste de tous les articles de type Article
     */
    public synchronized List getAllArticles()
    {
        try
        {
            return sqlMap.queryForList("getAllArticles", null);
        } catch (SQLException ex)
        {
            throw new UncheckedAccessArticlesException("Echec de l'obtention de tous les articles : [" + ex + "]", ex);
        }
    }


    /**
     * @param unArticle l'article à ajouter
     * @return le nombre d'articles ajoutés
     * @throws UncheckedAccessArticlesException si échec
     */
    @Override
    public synchronized int ajouteArticle(Article unArticle) {
        try {
            // on prépare les paramètres
            Map paramètres = new HashMap(5);
            paramètres.put("id", new Integer(unArticle.getId()));
            paramètres.put("nom", unArticle.getNom());
            paramètres.put("prix", new Double(unArticle.getPrix()));
            paramètres.put("stockactuel", new Integer(unArticle.getStockActuel()));
            paramètres.put("stockminimum", new Integer(unArticle.getStockMinimum()));

            // on joue la requête
            return sqlMap.update("insertArticle", paramètres);
        } catch (SQLException ex) {
            throw new UncheckedAccessArticlesException(
                    "Echec de l'ajout de l'article [" + unArticle + "] : [" + ex + "]",
                    ex);
        }
    }

    /**
     * @param idArticle id de l'article à supprimer
     * @return le nombre d'articles supprimés
     * @throws UncheckedAccessArticlesException si échec
     */
    public synchronized int supprimeArticle(int idArticle) {
        try {
            // on prépare les paramètres
            Map paramètres = new HashMap(1);
            paramètres.put("id", new Integer(idArticle));
            // on joue la requête
            return sqlMap.update("deleteArticle", paramètres);
        } catch (SQLException ex) {
            throw new UncheckedAccessArticlesException(
                    "Erreur lors de la suppression de l'article d'id [" + idArticle
                            + "] : [" + ex + "]", ex);
        }
    }


    /**
     * @param unArticle l'article modifié qui doit remplacer l'ancien de même id
     * @return le nombre d'articles modifiés
     * @throws UncheckedAccessArticlesException si échec
     */
    @Override
    public synchronized int modifieArticle(Article unArticle) {
        try {
            // on prépare les paramètres
            Map paramètres = new HashMap(5);
            paramètres.put("id", new Integer(unArticle.getId()));
            paramètres.put("nom", unArticle.getNom());
            paramètres.put("prix", new Double(unArticle.getPrix()));
            paramètres.put("stockactuel", new Integer(unArticle.getStockActuel()));
            paramètres.put("stockminimum", new Integer(unArticle.getStockMinimum()));
            // on joue la requête
            return sqlMap.update("modifyArticle", paramètres);
        } catch (SQLException ex) {
            throw new UncheckedAccessArticlesException(
                    "Erreur lors de la mise à jour de l'article [" + unArticle +
                            "] : ["
                            + ex + "]", ex);
        }
    }

    /**
     * @param idArticle id de l'article cherché
     * @return Article : l'article trouvé ou null
     * @throws UncheckedAccessArticlesException si échec
     */
    public synchronized Article getArticleById(int idArticle) {
        try {
            // on prépare les paramètres
            Map paramètres = new HashMap(1);
            paramètres.put("id", new Integer(idArticle));
            // on joue la requête
            Article article = (Article) sqlMap.queryForObject("getArticleById",
                    paramètres);
            // on rend le résultat
            return article;
        } catch (SQLException ex) {
            throw new UncheckedAccessArticlesException(
                    "Erreur lors de la recherche de l'article d'id [" + idArticle
                            + "] : [" + ex + "]", ex);
        }
    }

    /**
     * vide la table des articles
     *
     * @throws UncheckedAccessArticlesException si échec
     */
    public synchronized void clearAllArticles() {
        try {
            sqlMap.update("clearAllArticles", null);
        } catch (SQLException ex) {
            throw new UncheckedAccessArticlesException(
                    "Erreur lors de l'effacement de la table des articles : [" + ex +
                            "]",
                    ex);
        }
    }

    /**
     * @param pourcentage le pourcentage d'augmenttaion en entier
     * @throws UncheckedAccessArticlesException si échec
     */
    public synchronized void augmenterTousLesPrix(int pourcentage) {
        try {
            // on prépare les paramètres
            Map paramètres = new HashMap(1);
            paramètres.put("pourcentage", new Double(0.01 * pourcentage));
            // on joue la requête
            sqlMap.update("augmentationPrix", paramètres);
        } catch (SQLException ex) {
            throw new UncheckedAccessArticlesException(
                    "Erreur l'augmentation des prix : [" + ex + "]", ex);
        }
    }

    /**
     * méthode de test
     */
    public void badOperationThrowsException() {
        // lance artificiellement une exception
        throw new UncheckedAccessArticlesException();
    }

    /**
     * @param articles Article[] liste d'articles à insérer dans une même transaction
     * @throws UncheckedAccessArticlesException si échec
     */
    public synchronized void doInsertionsInTransaction(Article[] articles) {
        // on insère tous les articles comme un tout
        UncheckedAccessArticlesException ex = null;
        int i = 0;
        Connection connexion = null;
        try {
            // on démarre la transaction
            sqlMap.startTransaction();
            // on fait les insertions
            for (; i < articles.length; i++) {
                ajouteArticle(articles[i]);
            }
            // on committe la transaction
            sqlMap.commitTransaction();
        } catch (Exception ex1) {
            // on encapsule l'exception
            ex = new UncheckedAccessArticlesException(
                    "doInsertionsInTransaction, erreur d'accès à la base : [" + ex1 +
                            "]",
                    ex1);
        } finally {
            try {
                sqlMap.endTransaction();
            } catch (SQLException ex2) {
                if (ex != null) {
                    ex = new UncheckedAccessArticlesException(
                            "doInsertionsInTransaction, échec du close : [" + ex2 + "]", ex2);
                }
            }
        }
        // exception à lancer ?
        if (ex != null) {
            throw ex;
        }
    }

    /**
     * @param idArticle id de l'article dont on change le stock
     * @param mouvement mouvement de stock
     */
    public synchronized int changerStockArticle(int idArticle, int mouvement) {
        try {
            // on prépare les paramètres
            Map paramètres = new HashMap(2);
            paramètres.put("id", new Integer(idArticle));
            paramètres.put("mouvement", new Integer(mouvement));
            // on joue la requête
            return sqlMap.update("changerStockArticle", paramètres);
        } catch (SQLException e1) {
            throw new UncheckedAccessArticlesException(
                    "Erreur lors du changement de stock [idArticle=" + idArticle +
                            ", mouvement=" + mouvement + "] : [" + e1 + "]", e1);
        }
    }
}




