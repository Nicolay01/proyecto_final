/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.bollo;

/**
 *
 * @author decar
 */
public class BolloAngelito extends Bollo {

    public BolloAngelito() {
    }
    
    public BolloAngelito(int bolloTraido, int bolloQuedado) {
        super(bolloTraido, bolloQuedado);
    }

    @Override
    public int costoBollo() {
        return 2000;
    }

    @Override
    public int total() {
        return (super.getBolloTraido() - super.getBolloQuedado()) * this.costoBollo();
    }
}
