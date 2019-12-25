
public final class Company {

    private final Long id;
    private final String shortName;

    public Company(final Long id, final String shortName) {
        this.id = id;
        this.shortName = shortName;
    }

    public Long getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

}