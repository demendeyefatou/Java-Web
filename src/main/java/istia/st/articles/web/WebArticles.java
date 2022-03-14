package istia.st.articles.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import istia.st.articles.domain.Achat;
import istia.st.articles.dao.Article;
import istia.st.articles.domain.IArticlesDomain;
import istia.st.articles.domain.Panier;
import istia.st.articles.exception.UncheckedAccessArticlesException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author ST
 */
public class WebArticles extends HttpServlet {
    // champs privés
    private ArrayList erreurs = new ArrayList();
    private IArticlesDomain articlesDomain = null;
    private final String URL_MAIN = "urlMain";
    private final String URL_ERREURS = "urlErreurs";
    private final String URL_LISTE = "urlListe";
    private final String URL_INFOS = "urlInfos";
    private final String URL_PANIER = "urlPanier";
    private final String URL_PANIER_VIDE = "urlPanierVide";
    private final String URL_DEBUG = "urlDebug";
    private final String SPRING_CONFIG_FILENAME = "springConfigFileName";
    private final String[] parameters =
            {
                    URL_MAIN, URL_ERREURS, URL_LISTE, URL_INFOS, URL_PANIER, URL_PANIER_VIDE, URL_DEBUG,
                    SPRING_CONFIG_FILENAME};
    private ServletConfig config;
    private final String ACTION_LISTE = "liste";
    private final String ACTION_PANIER = "panier";
    private final String ACTION_ACHAT = "achat";
    private final String ACTION_INFOS = "infos";
    private final String ACTION_RETIRER_ACHAT = "retirerachat";
    private final String ACTION_VALIDATION_PANIER = "validationpanier";
    private String urlActionListe;
    private final String lienActionListe = "Liste des articles";
    private String urlActionPanier;
    private final String lienActionPanier = "Voir le panier";
    private String urlActionValidationPanier;
    private final String lienActionValidationPanier = "Valider le panier";
    private Hashtable hActionListe = new Hashtable(2);
    private Hashtable hActionPanier = new Hashtable(2);
    private Hashtable hActionValidationPanier = new Hashtable(2);

    public void init() {
// on récupère les paramètres d'initialisation de la servlet
        config = getServletConfig();
        String param = null;
        for (int i = 0; i < parameters.length; i++) {
            param = config.getInitParameter(parameters[i]);
            if (param == null) {
// on mémorise l'erreur
                erreurs.add(
                        "Paramètre ["
                                + parameters[i]
                                + "] absent dans le fichier [web.xml]");
            }
        }
// des erreurs ?
        if (erreurs.size() != 0) {
            return;
        }
// on crée un objet IArticlesDomain d'accès à la couche métier
        try {
            articlesDomain = (IArticlesDomain)
                    (
                            new XmlBeanFactory(
                                    new ClassPathResource(
                                            (String) config.getInitParameter(SPRING_CONFIG_FILENAME)))).getBean(
                            "articlesDomain");
        } catch (
                Exception ex) {
// on mémorise l'erreur
            erreurs.add(
                    "Erreur de configuration de l'accès aux données : "
                            + ex.toString());
            return;
        }
// on mémorise certaines url de l'application
        hActionListe.put("href", "?action=" + ACTION_LISTE);
        hActionListe.put("lien", lienActionListe);
        hActionPanier.put("href", "?action=" + ACTION_PANIER);
        hActionPanier.put("lien", lienActionPanier);
        hActionValidationPanier.put(
                "href",
                "?action=" + ACTION_VALIDATION_PANIER);
        hActionValidationPanier.put("lien", lienActionValidationPanier);
// c'est fini
        return;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

// on vérifie comment s'est passée l'initialisation de la servelet
        if (erreurs.size() != 0) {
// a-t-on l'url de la page d'erreurs ?
            if (config.getInitParameter(URL_ERREURS) == null) {
                throw new ServletException(erreurs.toString());
            }
// on affiche la page des erreurs
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[]{
            });
            getServletContext()
                    .getRequestDispatcher(config.getInitParameter(URL_ERREURS))
                    .forward(request, response);
// fin
            return;
        }
        // on traite l'action
        String action = request.getParameter("action");
        if (action == null) {
// liste des articles
            doListe(request, response);
            return;
        }
        if (action.equals(ACTION_LISTE)) {
// liste des articles
            doListe(request, response);
            return;
        }
        if (action.equals(ACTION_INFOS)) {
// infos sur un article
            doInfos(request, response);
            return;
        }
        if (action.equals(ACTION_ACHAT)) {
// achat d'un article
            doAchat(request, response);
            return;
        }
        if (action.equals(ACTION_PANIER)) {
// affichage du panier
            doPanier(request, response);
            return;
        }
        if (action.equals(ACTION_RETIRER_ACHAT)) {
// suppression d'un article du panier
            doRetirerAchat(request, response);
            return;
        }
        if (action.equals(ACTION_VALIDATION_PANIER)) {
// validation du panier
            doValidationPanier(request, response);
            return;
        }
        // action inconnue
        ArrayList erreurs = new ArrayList();
        erreurs.add("action [" + action + "] inconnue");
// on affiche la page des erreurs
        request.setAttribute("actions", new Hashtable[]
                {
                        hActionListe
                });
        afficheErreurs(request, response, erreurs);
// fin
        return;
    }

    private void doValidationPanier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
// l'acheteur a confirmé son panier
        Panier panier = (Panier) request.getSession().getAttribute("panier");
// on valide ce panier
        try {
            articlesDomain.acheter(panier);
        } catch (UncheckedAccessArticlesException ex) {
// pas normal
            erreurs.add("Erreur d'accès aux données [" + ex.toString() + "]");
            request.setAttribute("actions", new Hashtable[]{hActionListe});
            afficheErreurs(request, response, erreurs);
            return;
        }
        // on récupère les erreurs
        ArrayList erreurs = articlesDomain.getErreurs();
        if (erreurs.size() != 0) {
            request.setAttribute("actions",
                    new Hashtable[]{hActionListe, hActionPanier});
            afficheErreurs(request, response, erreurs);
            return;


        }

        // on affiche la liste des articles
        request.setAttribute("message", "Votre panier a été validé");
        doListe(request, response);
// fin
        return;
    }

    private void doRetirerAchat(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

// on retire un achat du panier
        try {
            Panier panier =
                    (Panier) request.getSession().getAttribute("panier");
            String strIdAchat = request.getParameter("id");
            panier.enlever(Integer.parseInt(strIdAchat));
        } catch (NumberFormatException ignored) {
        } catch (NullPointerException ignored) {
        }
// on affiche le panier
        doPanier(request, response);
    }

    private void doPanier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
// on affiche le panier
        Panier panier = (Panier) request.getSession().getAttribute("panier");
// panier vide ?
        if (panier == null || panier.getAchats().size() == 0) {
            request.setAttribute("actions", new Hashtable[]{hActionListe});
            getServletContext()
                    .getRequestDispatcher(config.getInitParameter(URL_PANIER_VIDE))
                    .forward(request, response);
// fin
            return;
        }
// il y a qq chose dans le panier
        request.setAttribute("panier", panier);
        request.setAttribute(
                "actions",
                new Hashtable[]{hActionListe, hActionValidationPanier});
        getServletContext()
                .getRequestDispatcher(config.getInitParameter(URL_PANIER))
                .forward(request, response);
// fin
        return;
    }

    private void doAchat(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
// achat d'un article
// on récupère la quantité
        int qté = 0;
        try {
            qté = Integer.parseInt(request.getParameter("qte"));
            if (qté <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException ex) {
// qté erronée
            request.setAttribute("msg", "Quantité incorrecte");
            request.setAttribute("qte", request.getParameter("qte"));
            String url =
                    config.getInitParameter(URL_MAIN)
                            + "?action=infos&id="
                            + request.getParameter("id");
            getServletContext().getRequestDispatcher(url).forward(
                    request, response);
// fin
            return;
        }
// on récupère la session du client
        HttpSession session = request.getSession();
// on crée l'achat
        Article article = (Article) session.getAttribute("article");
        Achat achat = new Achat(article, qté);
// on ajoute l'achat au panier du client
        Panier panier = (Panier) session.getAttribute("panier");
        if (panier == null) {
            panier = new Panier();
            session.setAttribute("panier", panier);
        }
        panier.ajouter(achat);
// on revient à la liste des articles
        String url = config.getInitParameter(URL_MAIN) + "?action=liste";
        getServletContext().getRequestDispatcher(url).forward(
                request, response);
// fin
        return;
    }

    private void afficheDebugInfos(HttpServletRequest request, HttpServletResponse response, ArrayList infos)
            throws ServletException, IOException {

// on affiche la liste des articles
        request.setAttribute("infos", infos);
        getServletContext()
                .getRequestDispatcher(config.getInitParameter(URL_DEBUG))
                .forward(request, response);
// fin
        return;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

// idem get
        doGet(request, response);
    }

    private void doInfos(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
// la liste des erreurs
        ArrayList erreurs = new ArrayList();
// on récupère l'id demandé
        String strId = request.getParameter("id");
// qq chose ?
        if (strId == null) {
// pas normal
            erreurs.add("action incorrecte([infos,id=null]");
            request.setAttribute("actions", new Hashtable[]{hActionListe});
            afficheErreurs(request, response, erreurs);
            return;
        }
// on transforme strId en entier
        int id = 0;
        try {
            id = Integer.parseInt(strId);
        } catch (Exception ex) {
// pas normal
            erreurs.add("action incorrecte([infos,id=" + strId + "]");
            request.setAttribute("actions", new Hashtable[]{hActionListe});
            afficheErreurs(request, response, erreurs);
            return;
        }
// on demande l'article de clé id
        Article article = null;
        try {
            article = articlesDomain.getArticleById(id);
        } catch (UncheckedAccessArticlesException ex) {
// pas normal
            erreurs.add("Erreur d'accès aux données [" + ex.toString() + "]");
            request.setAttribute("actions", new Hashtable[]{hActionListe});
            afficheErreurs(request, response, erreurs);
            return;
        }
        if (article == null) {
// pas normal
            erreurs.add("Article de clé [" + id + "] inexistant");
            request.setAttribute("actions", new Hashtable[]{hActionListe});
            afficheErreurs(request, response, erreurs);
            return;
        }
// on met l'article dans la session
        request.getSession().setAttribute("article", article);
// on affiche la page d'infos
        request.setAttribute("actions", new Hashtable[]{hActionListe});
        request.setAttribute("urlMain", config.getInitParameter(URL_MAIN));
        getServletContext()
                .getRequestDispatcher(config.getInitParameter(URL_INFOS))
                .forward(request, response);
// fin
        return;
    }

    private void afficheErreurs(HttpServletRequest request, HttpServletResponse response, ArrayList erreurs)
            throws ServletException, IOException {

// on affiche la page des erreurs
        request.setAttribute("erreurs", erreurs);
        getServletContext()
                .getRequestDispatcher(config.getInitParameter(URL_ERREURS))
                .forward(request, response);
// fin
        return;
    }

    private void doListe(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
// la liste des erreurs
        ArrayList erreurs = new ArrayList();
// on demande la liste des articles
        List articles = null;
        try {
            articles = articlesDomain.getAllArticles();
        } catch (UncheckedAccessArticlesException ex) {
// on mémorise l'erreur
            erreurs.add(
                    "Erreur lors de l'obtention de tous les articles : "
                            + ex.toString());
        }
// des erreurs ?
        if (erreurs.size() != 0) {
// on affiche la page des erreurs
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[]{hActionListe});
            getServletContext()
                    .getRequestDispatcher(config.getInitParameter(URL_ERREURS))
                    .forward(request, response);
// fin
            return;
        }
// on affiche la liste des articles
        request.setAttribute("listarticles", articles);
        request.setAttribute("message", "");
        request.setAttribute("actions", new Hashtable[]{hActionPanier});
        getServletContext()
                .getRequestDispatcher(config.getInitParameter(URL_LISTE))
                .forward(request, response);
// fin
        return;
    }
    /**
     *	suivi console pour débogage
     *	@param message : le message à afficher
     */
    private void affiche(String message) { System.out.println(message);
    }


}

