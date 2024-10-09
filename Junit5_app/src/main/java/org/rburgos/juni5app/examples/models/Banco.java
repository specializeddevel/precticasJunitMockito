package org.rburgos.juni5app.examples.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Banco {
    List<Cuenta> cuentas;
    String nombre;

    public Banco() {
        cuentas = new ArrayList<Cuenta>();
    }

    public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto) {

        origen.debito(monto);
        destino.credito(monto);
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public void addCuenta(Cuenta cuenta) {
        cuenta.setBanco(this);
        //cuenta.setPersona(cuenta.getPersona()+"-Acc");
        cuentas.add(cuenta);

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
