package modelo.bollo;

/**
 *
 * @author Nicolay, David, Joshep, Sebastian.
 */
public class BolloYucaDulce extends Bollo {

    public BolloYucaDulce() {}
    
    public BolloYucaDulce(int bolloTraido, int bolloQuedado) {
        super(bolloTraido, bolloQuedado);
    }

    @Override
    public int costoBollo() {
        return 2500;
    }

    @Override
    public int total() {
        return (super.getBolloTraido() - super.getBolloQuedado()) * this.costoBollo();
    }   
}
