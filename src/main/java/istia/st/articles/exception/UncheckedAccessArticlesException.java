package istia.st.articles.exception;
public class UncheckedAccessArticlesException
        extends RuntimeException {
    public UncheckedAccessArticlesException() {
        super();
    }
    public UncheckedAccessArticlesException(String mesg) {
        super(mesg);
    }
    public UncheckedAccessArticlesException(String mesg, Throwable th) {
        super(mesg, th);
    }
}
