package istia.st.articles.dao;
import istia.st.articles.exception.UncheckedAccessArticlesException;
/**
 * @author ST - ISTIA
 *
 */
public class Article {
    private int id;
    private String nom;
    private double prix;
    private int stockActuel;
    private int stockMinimum;

    /**
     * constructeur par défaut
     */
    public Article() {
    }

    public Article(int id, String nom, double prix, int stockActuel,
                   int stockMinimum) {
        // init attributs d'instance
        setId(id);
        setNom(nom);
        setPrix(prix);
        setStockActuel(stockActuel);
        setStockMinimum(stockMinimum);
    }

    // getters - setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        // id valide ?
        if (id < 0)
            throw new UncheckedAccessArticlesException("id[" + id + "] invalide");
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        // nom valide ?
        if (nom == null || nom.trim().equals("")) {
            throw new UncheckedAccessArticlesException("Le nom est [null] ou vide");
        }
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        // prix valide ?
        if (prix < 0) throw new UncheckedAccessArticlesException("Prix[" + prix + "]invalide");
        this.prix = prix;
    }

    public int getStockActuel() {
        return stockActuel;
    }
    public void setStockActuel(int stockActuel) {
        // stock valide ?
        if (stockActuel < 0)
            throw new UncheckedAccessArticlesException("stockActuel[" + stockActuel + "] invalide");
        this.stockActuel = stockActuel;
    }
    public int getStockMinimum() {
        return stockMinimum;
    }
    public void setStockMinimum(int stockMinimum) {
        // stock valide ?
        if (stockMinimum < 0)
            throw new UncheckedAccessArticlesException("stockMinimum[" + stockMinimum + "] invalide");
        this.stockMinimum = stockMinimum;
    }
    public String toString() {
        return "[" + id + "," + nom + "," + prix + "," + stockActuel + ","
                + stockMinimum + "]";
    }
}
