import java.util.LinkedList;

public class Chemin {
    private LinkedList<AssociationTempsCase> chemin;

    public LinkedList<AssociationTempsCase> getChemin() {
        return chemin;
    }

    public void addElement(Case caseSuiv, int date){
        this.chemin.add(new AssociationTempsCase(caseSuiv, date));
    }

    public AssociationTempsCase getElem(int index) {
        return this.chemin.get(index);
    }

}

class AssociationTempsCase{
    private Case _case;
    private int t;
    
    public AssociationTempsCase(Case _case, int t) {
        this._case = _case;
        this.t = t;
    }

    public Case get_case() {
        return _case;
    }

    public int getT() {
        return t;
    }
}